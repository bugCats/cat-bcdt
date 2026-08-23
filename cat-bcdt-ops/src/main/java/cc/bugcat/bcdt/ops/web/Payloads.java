package cc.bugcat.bcdt.ops.web;

import cc.bugcat.acore.framework.json.JsonTools;


/**
 * websocket 包
 *
 * @author bugcat-cc
 */
public class Payloads {
    
    
    
    public static Notify notifyBuilder() {
        return new Notify();
    }
    
    
    
    
    /**
     * 浏览器弹屏消息
     * */
    public static class Notify {
        
        public String title; // 标题
        public String content; // 消息内容
        public Integer msgType;   // 消息分类
        
        
        public Notify title(String title) {
            this.title = title;
            return this;
        }
        public Notify content(String content) {
            this.content = content;
            return this;
        }
        

        public Notify msgType(Integer msgType) {
            this.msgType = msgType;
            return this;
        }
        
        public JsonTools build() {
            JsonTools payload = new JsonTools();
            payload.put("title", title);
            payload.put("content", content);
            payload.put("msgType", msgType);
            return payload;
        }
    }
    
    
    
}
