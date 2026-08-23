package cc.bugcat.bcdt.ops.model.zentao;

import cc.bugcat.bcdt.ops.model.ext.BcTalkExt;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * BcTalkExt 扩展类
 * bc_talk:消息
 * @author auto
 * 2026-07-30 16:05:55
 * */
@Getter
@Setter
@TableName("bc_talk")
public class BcTalk extends BcTalkExt {

    public enum TextType {

        /**
         * 站内消息
         * */
        web_sms(113),
        
        /**
         * 数据库授权
         * */
        db_auth(110),

        /**
         * 禅道消息
         * */
        zen_tao(114),

        /**
         * gitea、gitlab消息
         * */
        git(122),

        /**
         * sonar 代码检查消息
         * */
        sonar(118),

        ;

        TextType(int code) {
            this.code = code;
        }

        public final int code;

    }



    private Date dateStart;

    private Date dateEnd;

    private Long total;

    private Long unread;

    private Integer count;
    
}
