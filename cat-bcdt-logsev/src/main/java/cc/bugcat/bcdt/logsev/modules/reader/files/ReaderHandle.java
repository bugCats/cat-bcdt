package cc.bugcat.bcdt.logsev.modules.reader.files;

import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.files.line.ReaderLineManage;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author bugcat-cc
 * */
public class ReaderHandle {
    
    /**
     * 每次读行时，向前、向后的缓存大小 1M
     * */ 
    public static final long line_size = 1024 * 1024;
    
    /**
     * 每次 2M
     * */
    public static final long read_size = 2 * 1024 * 1024;
    
    /**
     * 迭代整个文件时，每次读取的缓存大小：16M
     * */
    public static final long iterator_size = 16 * 1024 * 1024;

    
    
    /**
     * 从指定位置，向前寻找换行符
     * @param point 指定读取的绝对索引位置
     * @param fileSize 当前文件大小
     * */
    public static ReadLineInfo findPrevEnter(FileChannel channel, long point, long fileSize) throws Exception {
        return ReaderHandle.findPrevEnter(1, channel, point, fileSize);
    }

    /**
     * 从指定位置，向前寻找rows个换行符
     * @param rows 换行符数量
     * @param point 指定读取的绝对索引位置
     * @param fileSize 当前文件大小
     * */
    public static ReadLineInfo findPrevEnter(int rows, FileChannel channel, long point, long fileSize) throws Exception {
        ReadLineInfo line = new ReadLineInfo(fileSize);
        line.setPoint(point);
        line.setBufferSize(line_size);
        long start = ReaderHandle.findPrevEnter(channel, line, rows);
        line.setLineStart(start);
        return line;
    }

    private static long findPrevEnter(FileChannel channel, ReadLineInfo line, int rows) throws Exception {
        
        final long point = line.getPoint();
        if (point <= 0L) {
            return 0L;
        }
        
        //缓存的开始位置
        long start = point - line.getBufferSize();
        if (start <= 0L) {
            start = 0L;
        }
        
        byte[] bytes = ReaderHandle.readByte(channel, start, point);
        int begin = ReaderHandle.trimBegin(bytes);
        
        long index = -1L;
        for (int idx = begin; idx >= 0; idx --) {
            if (bytes[idx] == 10) { //换行符
                index = idx + 1;
                rows -- ;
                if ( rows == 0 ) {
                    break;
                };
            }
        }

        //在当前缓存bytes中没有足够的换行，移动窗口，再次寻找
        if (rows > 0) { 
            line.setPoint(start);
            line.setBufferSize(line.getBufferSize() << 1);
            return ReaderHandle.findPrevEnter(channel, line, rows);
        }
        
        return start + index;
    }


    /**
     * 从指定位置，向后寻找换行符
     * @param point 指定读取的绝对索引位置
     * @param fileSize 当前文件大小
     * */
    public static ReadLineInfo findNextEnter(FileChannel channel, long point, long fileSize) throws Exception {
        return ReaderHandle.findNextEnter(1, channel, point, fileSize);
    }


    /**
     * 从指定位置，向后寻找rows个换行符
     * @param rows 换行符数量
     * @param point 指定读取的绝对索引位置
     * @param fileSize 当前文件大小
     * */
    public static ReadLineInfo findNextEnter(int rows, FileChannel channel, long point, long fileSize) throws Exception {
        ReadLineInfo line = new ReadLineInfo(fileSize);
        line.setPoint(point);
        line.setBufferSize(line_size);
        long end = ReaderHandle.findNextEnter(channel, line, rows);
        line.setLineEnd(end);
        return line;
    }

    private static long findNextEnter(FileChannel channel, ReadLineInfo line, int rows) throws Exception {
        
        final long size = line.getFileSize();
        final long point = line.getPoint();
        if (point >= size) {
            return size;
        }
        
        long end = point + line.getBufferSize();
        if (end >= size) {
            end = size;
        }
        
        byte[] bytes = ReaderHandle.readByte(channel, point, end);
        
        long index = -1L;
        int find = 0;
        for (int idx = 0; idx < bytes.length; idx ++) {
            if (bytes[idx] == 10) { //换行符
                index = idx + 1;
                rows -- ;
                if ( rows == 0 ) {
                    break;
                };
            };
        }

        //在当前缓存bytes中没有找齐换行，移动窗口，再次寻找
        if (rows > 0) { 
            line.setPoint(end);
            line.setBufferSize(line.getBufferSize() << 1);
            return ReaderHandle.findNextEnter(channel, line, rows);
        }
        
        return point + index;
    }

    
    /**
     * 从后向前寻找换行符；
     * abcd\r\nefg\r\nhig 从h开始向前，需要先去掉h之前的\r\n
     * */
    public static int trimBegin(byte[] bytes) {
        return ReaderHandle.trimBegin(bytes, bytes.length - 1);
    }
    public static int trimBegin(byte[] bytes, int begin) {
        while (begin >= 0 && (bytes[begin] == 10 || bytes[begin] == 13)) {
            --begin;
        }
        return begin;
    }

    
    /**
     * 读取内容
     * @param start 开始索引
     * @param end 结束索引
     * */
    public static byte[] readByte(FileChannel channel, long start, long end) throws Exception {
        MappedByteBuffer readBuffer = channel.map(FileChannel.MapMode.READ_ONLY, start, end - start);
        int capacity = readBuffer.capacity();
        byte[] bytes = new byte[capacity];
        readBuffer.get(bytes, 0, capacity);
        return bytes;
    }

    
    /**
     * 读取内容
     * @param start 开始索引
     * @param end 结束索引
     * */
    public static List<ReadLineInfo> readLines(LogFileBean fileBean, long start, long end) throws Exception {
        byte[] bytes = ReaderHandle.readByte(fileBean.getChannel(), start, end);
        ReadLine readLine = new ReadLine();
        readLine.setBytes(bytes);
        readLine.setPoint(start);
        readLine.setLogInfo(fileBean);
        return ReaderHandle.readLines(readLine, 0, bytes.length);
    }

    
    /**
     * 读取内容
     * @param offsetStart 相对定位开始索引
     * @param offsetEnd 相对定位结束索引
     * */
    public static List<ReadLineInfo> readLines(ReadLine readLine, int offsetStart, int offsetEnd) {
        // 当前读取缓存
        final byte[] bytes = readLine.getBytes();
        // 绝对定位索引值
        final long start = readLine.getPoint();
        // 当前读取的文件
        final LogFileBean logInfo = readLine.getLogInfo();

        List<ReadLineInfo> lines = new LinkedList<>();
        int begin = offsetStart;
        int max = Math.min(offsetEnd, bytes.length) - 1;
        for (int idx = offsetStart; idx <= max; ++idx) {
            if (bytes[idx] != 10 && idx != max) continue;
            int end = idx + 1;
            ReadLineInfo item = new ReadLineInfo();
            item.setLineStart(start + begin);
            item.setLineEnd(start + end);
            item.setText(new String(bytes, begin, end - begin, logInfo.getCharset()));
            lines.add(item);
            begin = end;
        }
        return lines;
    }

    
    
    public static List<LineInfo> toLineInfo(List<ReadLineInfo> list) {
        List<LineInfo> lines = new ArrayList<>(list.size());
        for ( ReadLineInfo item : list) {
            LineInfo line = new LineInfo();
            line.setLineStart(item.getLineStart());
            line.setLineEnd(item.getLineEnd());
            line.setText(item.getText());
            lines.add(line);
        }
        return lines;
    }
}

