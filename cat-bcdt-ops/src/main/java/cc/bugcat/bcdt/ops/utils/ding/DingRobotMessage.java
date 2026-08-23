package cc.bugcat.bcdt.ops.utils.ding;

import cc.bugcat.acore.framework.json.JsonTools;

import java.util.ArrayList;
import java.util.List;


public class DingRobotMessage {

    private String title;

    private List<String> items = new ArrayList<>();
    
    private List<String> atPhone = new ArrayList<>(); //需要at的人的手机号
    private boolean atAll = false;

    public void setTitle(String title) {
        this.title = title;
    }
    public void setAtAll(boolean atAll) {
        this.atAll = atAll;
    }
    public void setAtPhone(List<String> atPhone) {
        this.atPhone = atPhone;
    }
    public void add(String text) {
        items.add(text);
    }
    public String getTitle() {
        return title;
    }

    /**
     * 加粗
     * */
    public static String getBoldText(String text) {
        return "**" + text + "**";
    }

    /**
     * 斜体
     * */
    public static String getItalicText(String text) {
        return "*" + text + "*";
    }

    /**
     * 超链接
     * */
    public static String getLinkText(String text, String href) {
        return "[" + text + "](" + href + ")";
    }

    /**
     * 图片
     * */
    public static String getImageText(String imageUrl) {
        return "![image](" + imageUrl + ")";
    }
    
    /**
     * h 标题
     * */
    public static String getHeaderText(int headerType, String text) {
        if (headerType < 1 || headerType > 6) {
            throw new IllegalArgumentException("headerType should be in [1, 6]");
        }

        StringBuilder numbers = new StringBuilder();
        for (int i = 0; i < headerType; i++) {
            numbers.append("#");
        }
        return numbers + " " + text;
    }

    /**
     * 引用
     * */
    public static String getReferenceText(String text) {
        return "> " + text;
    }

    /**
     * 有序列表
     * */
    public static String getOrderListText(List<String> orderItem) {
        if (orderItem.isEmpty()) {
            return "";
        }

        StringBuilder sbr = new StringBuilder();
        for (int i = 1; i <= orderItem.size() - 1; i++) {
            sbr.append(String.valueOf(i) + ". " + orderItem.get(i - 1) + DingRobot.NEW_LINE);
        }
        sbr.append(String.valueOf(orderItem.size()) + ". " + orderItem.get(orderItem.size() - 1));
        return sbr.toString();
    }

    /**
     * 无序列表
     * */
    public static String getUnorderListText(List<String> unorderItem) {
        if (unorderItem.isEmpty()) {
            return "";
        }

        StringBuilder sbr = new StringBuilder();
        for (int i = 0; i < unorderItem.size() - 1; i++) {
            sbr.append("- " + unorderItem.get(i) + DingRobot.NEW_LINE);
        }
        sbr.append("- " + unorderItem.get(unorderItem.size() - 1));
        return sbr.toString();
    }



    public String toMarkdownJson() {
        JsonTools result = new JsonTools();
        result.put("msgtype", "markdown");

        JsonTools markdown = new JsonTools();
        markdown.put("title", title);

        StringBuilder markdownText = new StringBuilder();
        for (String item : items) {
            markdownText.append(item + DingRobot.NEW_LINE);
        }

        if( atAll ){
            JsonTools at = new JsonTools();
            at.put("isAtAll", true);
            result.put("at", at);
        } else {
            if( atPhone.size() > 0 ){
                JsonTools at = new JsonTools();
                at.put("atMobiles", atPhone);
                at.put("isAtAll", false);
                result.put("at", at);
            }
        }

        markdown.put("text", markdownText.toString());
        result.put("markdown", markdown);

        return result.toJson();
    }
    
    
    public String toActionCard(List<String[]> bottonList){

        JsonTools result = new JsonTools();
        result.put("msgtype", "actionCard");

        JsonTools actionCard = new JsonTools();
        actionCard.put("title", title);

        StringBuilder markdownText = new StringBuilder();
        for (String item : items) {
            markdownText.append(item + DingRobot.NEW_LINE);
        }

        if( atAll ){
            JsonTools at = new JsonTools();
            at.put("isAtAll", true);
            result.put("at", at);
        } else {
            if( atPhone.size() > 0 ){
                JsonTools at = new JsonTools();
                at.put("atMobiles", atPhone);
                at.put("isAtAll", false);
                result.put("at", at);
            }
        }

        actionCard.put("text", markdownText.toString());
        actionCard.put("btnOrientation", "0");

        List<JsonTools> btns = new ArrayList<>();
        for ( String[] botton : bottonList ) {
            JsonTools btn = new JsonTools();
            btn.put("title", botton[0]);
            btn.put("actionURL", botton[1]);
            btns.add(btn);
        }
        actionCard.put("btns", btns);

        result.put("actionCard", actionCard);

        return result.toJson();
    }
}
