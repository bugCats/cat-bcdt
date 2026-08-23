package cc.bugcat.bcdt.ops.service.mysql.logger;


import cc.bugcat.bcdt.ops.service.mysql.handler.MysqlConnContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * 一定是同一个链接的数据
 *
 * @author bugcat-cc
 */
public class LoggerEntry {

    private final int threadIndex;          //线程索引值
    private final MysqlConnContext context; //当前链接信息
    
    private final List<byte[]> buckets;
    private final BlockingDeque<byte[]> pools;
    
    private long activeTime;                //最后活跃时间
    
    
    
    public LoggerEntry(int threadIndex, MysqlConnContext context) {
        this.threadIndex = threadIndex;
        this.context = context;
        this.buckets = new ArrayList<>(30);
        this.pools = new LinkedBlockingDeque<>();
        this.activeTime = System.currentTimeMillis();
    }

    
    public LoggerEntry putToBucket(byte[] bytes) {
        buckets.add(bytes);
        activeTime = System.currentTimeMillis();
        return this;
    }

    public void pourWater() {
        for ( byte[] bytes : buckets ) {
            pools.offer(bytes);
        }
        buckets.clear();
        activeTime = System.currentTimeMillis();
    }

    
    public List<byte[]> draining() {
        int size = pools.size();
        List<byte[]> nodes = new ArrayList<>(size);
        for(int idx = 0; idx < size; idx ++ ){
            byte[] bytes = pools.poll(); //从队首获取
            nodes.add(bytes);
        }
        activeTime = System.currentTimeMillis();
        return nodes;
    }

    
    public void backFlow(byte[] backNode){
        pools.offerFirst(backNode);
        activeTime = System.currentTimeMillis();
    }
    
    
    
    /**
     * 是否有效
     * */
    public boolean isInactive(long nowTime){
        if ( nowTime - activeTime > 300_000 ) { //已经超过5分钟没有任何数据，从map中移除桶
            return true;
        } else {
            return false;
        }
    }


    public int getThreadIndex() {
        return threadIndex;
    }
    public MysqlConnContext getContext() {
        return context;
    }
    public int getBucketSize() {
        return buckets.size();
    }

}
