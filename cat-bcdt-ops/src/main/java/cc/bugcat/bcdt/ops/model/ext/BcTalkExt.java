package cc.bugcat.bcdt.ops.model.ext;

import java.lang.Long;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * bc_talk:消息
 * @author auto
 * 2026-07-30 16:05:55
 * */
@Getter
@Setter
public class BcTalkExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Long id;	// 数据id

    @TableField("`account`")
	private String account;	// 消息归属人账号

    @TableField("`realname`")
	private String realname;	// 消息归属人姓名

    @TableField("`sendaccount`")
	private String sendAccount;	// 发送人账号

    @TableField("`sendrealname`")
	private String sendRealname;	// 发送人姓名

    @TableField("`content`")
	private String content;	// 消息内容

    @TableField("`msgtype`")
	private Integer msgType;	// 消息类型

    @TableField("`msgtags`")
	private String msgTags;	// 消息标记

    @TableField("`read`")
	private String read;	// 是否已读：0未读；1已读

    @TableField("`savedate`")
	private Date saveDate;	// 最后修改时间


	/**
	 * 传入属性名，获取到对象属性值
	 * */
	public Object theValue(String property){
		Object result = null;
		switch ( property ){
			case "id":
				result = this.getId();
				break;
			case "account":
				result = this.getAccount();
				break;
			case "realname":
				result = this.getRealname();
				break;
			case "sendAccount":
				result = this.getSendAccount();
				break;
			case "sendRealname":
				result = this.getSendRealname();
				break;
			case "content":
				result = this.getContent();
				break;
			case "msgType":
				result = this.getMsgType();
				break;
			case "msgTags":
				result = this.getMsgTags();
				break;
			case "read":
				result = this.getRead();
				break;
			case "saveDate":
				result = this.getSaveDate();
				break;
			default:
				break;
		}
		return result;
    }

}