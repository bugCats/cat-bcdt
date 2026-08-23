package cc.bugcat.bcdt.ops.openapi.common;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;
import cc.bugcat.bcdt.ops.openapi.common.vi.SonarQubeSonarQubeVi;
import cc.bugcat.bcdt.ops.service.inter.BcTalkService;
import cc.bugcat.bcdt.ops.service.inter.ZtUserService;
import cc.bugcat.bcdt.ops.web.Payloads;
import cc.bugcat.bcdt.ops.web.WebAction;
import cc.bugcat.bcdt.ops.web.WebTopic;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * git 钩子控制
 * @author bugcat-cc
 */
@Tag(name = "sonarQube 管理")
@RestController
public class SonarQubeCtrl {

	private final Logger logger = LoggerFactory.getLogger(SonarQubeCtrl.class);
	
	@Autowired
	private BcTalkService talkService;
	
	@Autowired
	private ZtUserService ztUserService;
	
	
	/**
	 * 代码检查指派
	 * */
	@ACS(any = true)
	@RequestMapping(value = "/sonarQube/hooks/assignee", method = {RequestMethod.POST,RequestMethod.GET})
	public void pushAfter(@RequestParam(value = "account", required = false) String account){

		if( StringUtils.isBlank(account) ){
			return;
		}

		ZtUser ztUser = ztUserService.findByAccount(account);
		if( ztUser == null ){
			logger.error("未知的sonarQube账号：" + account);
			return;
		}

		String title = "代码检查消息";
		String comment = "您有新的SonarQube代码检查消息";

		BcTalk talk = new BcTalk();
		talk.setAccount(ztUser.getAccount());
		talk.setRealname(ztUser.getRealname());
		talk.setSendAccount("sonarQube");
		talk.setSendRealname("sonarQube");
		talk.setContent(BcTalkService.markText(title, "title") + BcTalkService.markText(comment, "comment"));
		talk.setMsgType(BcTalk.TextType.sonar.code);
		talk.setRead("0");
		talk.setSaveDate(new Date());
		talkService.save(talk);
		
		JsonTools payload = Payloads.notifyBuilder()
				.title("【SonarQube】" + title)
				.content(comment)
				.msgType(talk.getMsgType())
				.build();
		WebTopic.builder()
				.webSessions(talk.getAccount())
				.action(WebAction.web_alert)
				.payload(payload)
				.notify("0".equals(talk.getRead()))
				.doSend();
	}



	/**
	 * 推送代码钩子
	 * */
	@ACS(any = true)
	@PostMapping("/sonarQube/issues/assign")
	public void hooks(@RequestBody SonarQubeSonarQubeVi vi){

		if( StringUtils.isBlank(vi.getAccount()) ){
			return;
		}

		String title = "代码检查消息";
		String comment = "<span class='red'>" + vi.getMessage() + "</span> => " + vi.getComponent();

		BcTalk talk = new BcTalk();
		talk.setAccount(vi.getAccount());
		talk.setRealname(vi.getName());
		talk.setSendAccount("sonarQube");
		talk.setSendRealname("sonarQube");
		talk.setContent(BcTalkService.markText(title, "title") + BcTalkService.markText(comment, "comment"));
		talk.setMsgType(BcTalk.TextType.sonar.code);
		talk.setRead("0");
		talk.setSaveDate(new Date());
		talkService.save(talk);

		JsonTools payload = Payloads.notifyBuilder()
				.title("【SonarQube】" + title)
				.content(vi.getMessage())
				.msgType(talk.getMsgType())
				.build();
		WebTopic.builder()
				.webSessions(talk.getAccount())
				.action(WebAction.web_alert)
				.payload(payload)
				.notify("0".equals(talk.getRead()))
				.doSend();
	}


}
