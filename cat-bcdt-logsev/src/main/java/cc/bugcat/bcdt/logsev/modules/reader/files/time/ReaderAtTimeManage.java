package cc.bugcat.bcdt.logsev.modules.reader.files.time;

import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReaderHandle;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderInfoVo;
import cc.bugcat.bcdt.logsev.websocket.ReaderSocket;
import cc.bugcat.bcdt.logsev.websocket.ReplyMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * 实时显示
 *
 * @author bugcat-cc
 * */
public class ReaderAtTimeManage extends AbstractReaderManage {
    
    
    private final LogFileBean logInfo;
    private long lastEnd = 0L;

    public ReaderAtTimeManage(LogFileBean logInfo) {
        this.logInfo = logInfo;
        this.reload();
    }

    public void reload() {
        this.lastEnd = this.logInfo.reloadSize();
    }


    public void readTime(Consumer<String> sendData) {
        long newSize = this.logInfo.reloadSize();
        if (newSize > this.lastEnd) {
            try {
                List<ReadLineInfo> list = ReaderHandle.readLines(this.logInfo, this.lastEnd, newSize);
                ReaderInfoVo resp = this.toReaderInfo(list);
                ResultEntity<Object> body = ResultEntity.ok(resp);
                sendData.accept(ReaderSocket.build(ReplyMethod.readAtTime, body));
            } catch (Exception ex) {
                ResultEntity<Void> resp = ResultEntity.fail(BusinessException.builder(ex));
                sendData.accept(resp.toJson());
            } finally {
                this.lastEnd = newSize;
            }
        }
    }

    private ReaderInfoVo toReaderInfo(List<ReadLineInfo> list) {
        ReaderInfoVo resp = new ReaderInfoVo();
        resp.setAppId(this.logInfo.getAppId());
        resp.setLogName(this.logInfo.getLogName());
        resp.setAppend("tail");
        resp.setPercent(100);
        List<LineInfo> lines = new ArrayList<>(list.size());
        for ( ReadLineInfo item : list) {
            LineInfo info = new LineInfo();
            info.setLineStart(item.getLineStart());
            info.setLineEnd(item.getLineEnd());
            info.setText(item.getText());
            lines.add(info);
        }
        resp.setLines(lines);
        return resp;
    }

    @Override
    public int getReadMod() {
        return 20;
    }

    @Override
    public void closeFile() {
    }
}

