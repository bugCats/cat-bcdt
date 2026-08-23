package cc.bugcat.bcdt.ops.openapi.gitea.vi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * git 推送钩子
 * @author bugcat-cc
 * */
@Data
public class GiteaHooksPush {

	/**
	 * 提交代码的分支信息
	 * refs/heads/master
	 * */
	private String ref;

	/**
	 * 修改的文件信息
	 * 有多个不同人的提交
	 * */
	private List<PushCommit> commits;

	/**
	 * 仓库信息
	 * */
	private GitRepository repository;

	/**
	 * 推送人
	 * */
	private PushAuthor pusher;
	
	
	
	@Data
	public static class PushCommit {

		/**
		 * 新增的文件
		 * tmc-tsms/src/main/three/com/cetccloud/synOrder/hotel/synHotelOrderHc/SynHotelOrderHcService.java
		 * */
		private List<String> added;

		/**
		 * 修改的文件
		 * tmc-tsms/src/main/three/com/cetccloud/synOrder/hotel/synHotelOrderHc/SynHotelOrderHcService.java
		 * */
		private List<String> modified;

		/**
		 * 移除的文件
		 * tmc-tsms/src/main/three/com/cetccloud/synOrder/hotel/synHotelOrderHc/SynHotelOrderHcService.java
		 * */
		private List<String> removed;

		/**
		 * 提交人
		 * */
		private CommitAuthor author;
		
		
	}
	
	
	@Data
	public static class GitRepository {

		/**
		 * 仓库名称
		 * tsms
		 * */
		private String name;
		

	}


	/**
	 * 提交人信息
	 * */
	@Data
	public static class CommitAuthor {

		private String name;	//提交人中文
		private String username;//账号
		
	}
	
	
	/**
	 * 推送人信息
	 * */
	@Data
	public static class PushAuthor {

		private String username;//账号
		
		@JsonProperty("full_name")
		private String fullName;	//姓名

	}
	

}
