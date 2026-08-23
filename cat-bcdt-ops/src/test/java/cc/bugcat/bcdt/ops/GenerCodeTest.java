package cc.bugcat.bcdt.ops;

import cc.bugcat.acore.gener.GencoderService;
import cc.bugcat.acore.gener.GencoderVi;
import cc.bugcat.acore.gener.GencoderVo;
import cc.bugcat.acore.mariadb.config.DatabaseConfigurer;
import cc.bugcat.acore.fmark.utils.FreemarkerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * 代码生成
 * */
@SpringBootTest(classes = GenerCodeTest.GenerCodeMain.class)
public class GenerCodeTest {


    @Autowired
    private GencoderService gencoderService;

    
    /**
     * 执行代码生成
     * */
    @Test
    public void makeCode() {

        //表名
        final String tableName = "bc_db_proxy";
        
        
        final String service = "cc.bugcat.bcdt.ops.service.inter";
        
        //表对应的service实现类包名，必填！！！！
        final String serviceImpl = "cc.bugcat.bcdt.ops.service.zentao";


        //项目名
        final String projectName = "cat-bcdt-ops";
        
        //数据库名
        final String dbName = "zentao";
        
        //Mapper文件相对于/resources/所在目录名
        final String mapperDir = "mapper";


        GencoderVi vi = new GencoderVi();
        vi.setProjectName(projectName);
        vi.setMapperDir(mapperDir);

        vi.setDatabaseCode(dbName);
        vi.setTableName(tableName);
        vi.setMybatisPlus("1");
        vi.setLombok("1");
        
        vi.setSrcPath("E:\\_mygit\\cat-bcdt\\cat-bcdt-ops");
        
        vi.setDao("cc.bugcat.bcdt.ops.dao.zentao");
        vi.setDomain("cc.bugcat.bcdt.ops.model.zentao");
        vi.setExt("cc.bugcat.bcdt.ops.model.ext");
        vi.setService(service.startsWith(".")  ? "cc.bugcat.bcdt.ops.service" + service : service);
        vi.setImpl(serviceImpl.startsWith(".") ? "cc.bugcat.bcdt.ops.service" + serviceImpl : serviceImpl);

        GencoderVo resp = gencoderService.execute(vi);
        gencoderService.writeFile(vi, resp);
        
        System.exit(0);

    }





    @Import({DatabaseConfigurer.class,
            FreemarkerUtil.class,
            GencoderService.class})
    @SpringBootApplication(scanBasePackages = "cc-bugcat")
    public static class GenerCodeMain {
        public static void main(String[] args) {
            SpringApplication.run(GenerCodeMain.class, args);
        }
    }

}
