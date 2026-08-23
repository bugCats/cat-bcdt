package cc.bugcat.bcdt.ops.service.mysql.logger;

import cc.bugcat.acore.framework.constants.Const;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.config.MysqlProxyConfig;
import cc.bugcat.bcdt.ops.service.mysql.EnableMysqlProxy;
import cc.bugcat.bcdt.ops.service.mysql.spi.IDatabaseCoderService;
import cc.bugcat.bcdt.ops.service.mysql.spi.ISqlLoggerService;
import cc.bugcat.bcdt.ops.service.mysql.handler.MysqlConnContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 记录客户端执行的sql
 *
 *
 * @author bugcat-cc
 */
public class SqlLoggerServiceImpl implements ISqlLoggerService, InitializingBean, DisposableBean {

    private final BlockingQueue<QueueEntry> loggerQueue = new LinkedBlockingQueue<>();
    
    private List<BlockingQueue<LoggerEntry>> bucketQueueList;
    
    private volatile boolean isWork = true;

    //写日志文件线程
    private int threadSize; 

    
    
    @Autowired
    private MysqlProxyConfig mysqlProxyConfig;
    
    @Autowired
    private IDatabaseCoderService coderService;

    
    @Override
    public void afterPropertiesSet() {
        
        this.threadSize = mysqlProxyConfig.getWriteThread();
        this.bucketQueueList = new ArrayList<>(threadSize);
        
        Thread porter = new LoggerPorterThread(); 
        porter.start();
        
        for ( int idx = 0; idx < threadSize; idx ++ ) {
            bucketQueueList.add(new LinkedBlockingQueue<>());
            Thread writer = new LoggerWriteThread(idx);    
            writer.start();
        }
    }


    @Override
    public void destroy() {
        this.isWork = false;
    }

    
    /**
     * 主进程将所有链接tcp包全部存入loggerQueue
     * */
    @Override
    public void writeLog(MysqlConnContext context, byte[] bytes) {
        loggerQueue.offer(new QueueEntry(context, bytes));
    }
    
    
    private class LoggerPorterThread extends Thread {

        private final Map<String, LoggerEntry> entryMap = new HashMap<>();

        private LoggerPorterThread() {
            super.setName("sql-porter");
        }

        @Override
        public void run() {
            
            long checkTime = System.currentTimeMillis();
            
            while ( isWork ) { //单线程
                
                try {
                    
                    QueueEntry queueEntry = loggerQueue.poll(1, TimeUnit.SECONDS);
                    
                    if( queueEntry != null ) {
                        
                        LoggerEntry entry = getLoggerEntry(queueEntry.context);
                        entry.putToBucket(queueEntry.bytes);
                        
                        if ( entry.getBucketSize() > 20 ) { 
                            pourWater(entry);
                        }
                    }

                    long now = System.currentTimeMillis();
                    
                    //大约每隔1秒，强制将桶中内容倒入到池中。并移除无用的桶
                    if( now - checkTime > 980 ){ 
                        checkTime = now;
                        
                        Iterator<Map.Entry<String, LoggerEntry>> iter = entryMap.entrySet().iterator();
                        while ( iter.hasNext() ) {
                            LoggerEntry entry = iter.next().getValue();
                            if ( entry.getBucketSize() > 0 ) {
                                pourWater(entry);

                            } else {
                                if ( entry.isInactive(now) ) {
                                    iter.remove();
                                }
                            }
                        }
                    }
                    
                } catch ( InterruptedException ex ) {
                    isWork = false;
                } catch ( Exception ex ) {
                    EnableMysqlProxy.LOGGER.error(ex.getMessage());
                }
            }
        }
        
        private LoggerEntry getLoggerEntry(MysqlConnContext context){
            String entryId = context.getUserCode() + ":" + context.getHeadSize();
            LoggerEntry entry = entryMap.get(entryId);
            if( entry == null ){
                int threadIndex = Math.abs(context.getConnId().hashCode()) % threadSize;
                entry = new LoggerEntry(threadIndex, context);
                entryMap.put(entryId, entry);
            }
            return entry;
        }
        
        private void pourWater(LoggerEntry entry){
            entry.pourWater();
            BlockingQueue<LoggerEntry> loggerEntries = bucketQueueList.get(entry.getThreadIndex());
            loggerEntries.offer(entry);
        }
    }
    
    
    
    private class LoggerWriteThread extends Thread {
        private final int index;
        private LoggerWriteThread(int index) {
            super.setName("sql-write-" + index);
            this.index = index;
        }

        @Override
        public void run() {
            final BlockingQueue<LoggerEntry> loggerEntries = bucketQueueList.get(index);
            while ( isWork ) {
                try {
                    LoggerEntry entry = loggerEntries.poll(3, TimeUnit.SECONDS);
                    if( entry != null ){
                        List<String> lines = coderService.coder(entry);
                        if( lines.size() > 0 ){
                            String time = DateUtil.rs.format(new Date(), "HH:mm:ss.SSS");
                            StringBuilder sbr = new StringBuilder();
                            for ( String line : lines ) {
                                sbr.append(time + " >> " + ByteUtil.trim(line) + Const.NEW_LINE);
                            }
                            Consumer<String> writeLogs = entry.getContext().getWriteLogs();
                            writeLogs.accept(sbr.toString());
                        }
                    }
                } catch ( InterruptedException e ) {
                    isWork = false;
                } catch ( Exception ex ) {
                    EnableMysqlProxy.LOGGER.error(ex.getMessage());
                }
            }
        }
    }




    private static class QueueEntry {
        private final MysqlConnContext context;
        private final byte[] bytes;
        private QueueEntry(MysqlConnContext context, byte[] bytes) {
            this.context = context;
            this.bytes = bytes;
        }
    }
}
