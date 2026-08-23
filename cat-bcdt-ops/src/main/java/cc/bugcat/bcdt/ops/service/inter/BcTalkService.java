package cc.bugcat.bcdt.ops.service.inter;

import cc.bugcat.acore.mybatis.plus.IBaseService;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;

/**
 *
 * @author auto
 * 2023-06-26 15:25:18
 * */
public interface BcTalkService extends IBaseService<BcTalk> {


    /**
     * 换行
     * */
    public static String newLine(){
        return "<br>";
    }
    /**
     * 加粗
     * */
    public static String boldText(String text){
        return markText(text, "bold");
    }
    /**
     * 斜体
     * */
    public static String italicText(String text){
        return markText(text, "italic");
    }

    /**
     * 引用
     * */
    public static String referenceText(String text) {
        return markText(text, "reference");
    }

    /**
     * 自定义标记
     * */
    public static String markText(String text, String mark){
        return "<div class='" + mark + "'>" + text + "</div>";
    }

    /**
     * 超链接
     * */
    public static String linkText(String text, String href) {
        return "<span class='link'><a href='" + href + "' target='_blank'>" + text + "</a></span>";
    }

}
