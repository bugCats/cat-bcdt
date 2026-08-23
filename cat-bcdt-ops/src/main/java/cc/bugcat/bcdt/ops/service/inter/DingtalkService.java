package cc.bugcat.bcdt.ops.service.inter;

public interface DingtalkService {

    void sendMsgByUserId (String userId, String title, String body, String comment, String... strs);
}
