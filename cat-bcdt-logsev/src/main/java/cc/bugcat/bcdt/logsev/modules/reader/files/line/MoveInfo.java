package cc.bugcat.bcdt.logsev.modules.reader.files.line;


/**
 *
 * @author bugcat-cc
 * */
public class MoveInfo {
    
    private final int offsetStart;
    private final int offsetEnd;

    public MoveInfo(int offsetStart, int offsetEnd) {
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
    }

    public int getOffsetStart() {
        return offsetStart;
    }

    public int getOffsetEnd() {
        return offsetEnd;
    }
}
