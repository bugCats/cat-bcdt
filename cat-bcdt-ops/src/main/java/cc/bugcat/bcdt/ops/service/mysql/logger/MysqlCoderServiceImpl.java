package cc.bugcat.bcdt.ops.service.mysql.logger;



import cc.bugcat.bcdt.ops.service.mysql.spi.IDatabaseCoderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 将 tcp 包 byte[] 解析成sql；
 * 
 * {@code new String(bytes)} 可以直接输出大部分SQL；如果需要 100% 可靠，可以自行写方法解析；
 * 
 * 
 * 
 * 或者联系作者 bugcat-cc@qq.com 获取。当然，这是属于付费项目；
 *
 * 
 * @author bugcat-cc
 * */
@Service
public class MysqlCoderServiceImpl implements IDatabaseCoderService {

    @Override
    public List<String> coder(LoggerEntry loggerEntry) {
        List<byte[]> nodes = loggerEntry.draining();
        List<String> lines = new ArrayList<>();
        for ( byte[] bytes : nodes ) {
            lines.add(new String(bytes));
        }
        loggerEntry.getContext().refreshActiveTime();
        return lines;
    }
}
