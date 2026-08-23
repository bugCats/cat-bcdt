package cc.bugcat.bcdt.ops.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import net.sf.jsqlparser.schema.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * 
 * swagger文档
 * 
 * 原始文档 	/swagger-ui/index.html
 * 
 * knife4j	/doc.html
 * 
 * @author bugcat-cc
 */
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		return new OpenAPI()
				.openapi("3.1.0")  //显式指定 OpenAPI 版本
				.info(new Info()
						.title("bcdt后台")
						.version("1.0.0")
						.description("bugcat develop tools ops")
						.contact(new Contact()
								.name("bugcat")
								.email("bugcat-cc@qq.com"))
				);
	}


	/**
	 * 自定义过滤条件
	 */
	@Bean
	public GroupedOpenApi customFilterGroup() {
		return GroupedOpenApi.builder()
				.group("default")
				.packagesToScan("cc.bugcat.bcdt.ops.openapi")
				.build();
	}

}
