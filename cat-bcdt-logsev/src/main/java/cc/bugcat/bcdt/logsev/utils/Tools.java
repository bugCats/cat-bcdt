package cc.bugcat.bcdt.logsev.utils;

import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.logsev.web.vo.LogFileInfo;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;


/**
 *
 * @author bugcat-cc
 * */
public class Tools {

	private static Logger logger = LoggerFactory.getLogger(Tools.class);

	
	//监听服务器上指定目录，如果存在heapDump文件，发送钉钉通知内存溢出
	private static final String dingtalk = "https://oapi.dingtalk.com/robot/send?access_token=";


	
	public static String dateToStr(Date date) {
		if ( date == null ) {
			date = new Date();
		}
		return DateUtil.rs.format(date, "yyyy-MM-dd HH:mm:ss.SSS");
	}


	/**
	 * 转换成 KM  MB
	 */
	public static String sizeTo(long size) {
		long m = size;
		if ( size > 1048576 ) {
			m = size / 1048576;
			return m + " MB";
		}
		if ( size < 1024 ) {
			return m + " B";
		}
		m = size / 1024;
		if ( m <= 0L ) {
			m = 1;
		}
		return m + " KB";
	}


	public static void findFiles(List<File> logFiles, List<LogFileInfo> list) {
		for (File file : logFiles) {
			if (file.isDirectory()) {
				List<File> children = Arrays.stream(Optional.ofNullable(file.listFiles()).orElseGet(() -> new File[0])).toList();
				Tools.findFiles(children, list);
				continue;
			}
			LogFileInfo bean = new LogFileInfo();
			bean.setLogName(file.getPath().replace("\\", "/"));
			bean.setSize(Tools.sizeTo(file.length()));
			list.add(bean);
		}
	}


	/**
	 * 获取文件字符编码
	 * */
	public static String detectEncoding(File file) {
		try ( InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			CharsetDetector detector = new CharsetDetector();
			detector.setText(is); // 内部只会读取文件开头一小部分
			CharsetMatch match = detector.detect();
			if (match != null) {
				return match.getName();
			}
		} catch ( Exception ex ) {
			// noop
		}
		return "utf-8";
	}

	//用于获取query参数
	public static Map<String, String> getQueryMap(String query) {
		Map<String, String> queryMap = new HashMap<>();
		if ( StringUtils.isNotBlank(query) ) {
			String[] queryParams = query.split("&");
			for ( String param : queryParams ) {
				String[] kv = param.split("=", 2);
				String value = kv.length == 2 ? kv[1] : "";
				queryMap.put(kv[0], value);
			}
		}
		return queryMap;
	}


	public static void closeQuietly(Closeable... closeables) {
		for (Closeable closeable : closeables) {
			if ( closeable != null ) {
                try { closeable.close(); } catch ( Exception ex ) { }
            }
		}
	}


	public static void dingSend(String token, Markdown markdown) {
		RestTemplate rest = new RestTemplate();
		rest.postForObject(dingtalk + token, markdown.toJsonString(), String.class);
	}


	
	public static Markdown buildMarkdown(){
		return new Markdown();
	}

	/**
	 * 钉钉
	 * */
	public static class Markdown {
		private String title;
		private String subject;
		private List<String> items = new ArrayList<>();
		private boolean needAt = true;

		public Markdown title(String title){
			this.title = title;
			return this;
		}
		public Markdown subject(String subject){
			this.subject = subject;
			return this;
		}
		public Markdown items(String item){
			this.items.add(item);
			return this;
		}
		public Markdown needAt(boolean needAt){
			this.needAt = needAt;
			return this;
		}

		public String toJsonString() {
			JsonTools result = new JsonTools();
			result.put("msgtype", "markdown");
			JsonTools markdown = new JsonTools();
			markdown.put("title", title);
			StringBuilder sbr = new StringBuilder();
			sbr.append(subject + "\n");
			for (String item : items) {
				sbr.append(item + "\n");
			}
			markdown.put("text", sbr.toString());
			result.put("markdown", markdown);
			if( needAt ){
				JsonTools at = new JsonTools();
				at.put("isAtAll", true);
				result.put("at", at);
			}
			return result.toJson();
		}
	}
}
