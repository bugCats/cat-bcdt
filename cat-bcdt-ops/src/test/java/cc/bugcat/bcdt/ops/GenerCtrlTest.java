package cc.bugcat.bcdt.ops;

import cc.bugcat.acore.framework.constants.Const;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;


/**
 * 生成 openapi 接口
 * */
public class GenerCtrlTest {


    private static final String ctrl = "ctrl";
    private static final String vi = "vi";
    private static final String vo = "vo";

    private static final String baseCtrl = "cc.bugcat.acore.webmvc.web.framework.AbstractOpenApiCtrl";
    private static final String baseService = "cc.bugcat.acore.webmvc.web.framework.AbstractOpenApiService";
    private static final String requestContext = "cc.bugcat.acore.webmvc.web.framework.RequestContext";

    
    private static final Class<?> mainClass = BcdtOpsApplication.class;
    

    public static void main(String[] args) {

        /**
         * 生成的文件位置
         * 相对于 openapi 的目录
         * eg：org.user  =>   最终为：cc.bugcat.xxxx.openapi.org.user
         * */
        String pkg = "dbProxy.status";

        /**
         * {@code 接口调用方式 接口的名称}
         * eg：[post|get] login ===>  最终文件名为：LoginCtrl
         * */
        String name = "post dbProxyStatus";

        makeFile(pkg, name);
    }



    private static void makeFile(String pkg, String name){

        String base = mainClass.getPackage().getName() + ".openapi";
        pkg = pkg.replace(base + ".", "");
        String basePkg = base + "." + pkg;

        String[] names = StringUtils.split(name, " ");
        if ( names.length == 1 ) {
            names = new String[]{"post", name};
        }

        String capName = StringUtils.capitalize(names[1]);
        String method = names[0].toUpperCase();

        String ctrlName = capName + StringUtils.capitalize(ctrl);
        String viName = capName + StringUtils.capitalize(vi);
        String voName = capName + StringUtils.capitalize(vo);
        String serviceName = capName + "Service";

        //api包
        FileBuild apiFile = new FileBuild(basePkg, ctrlName);

        // vi包
        FileBuild viFile = new FileBuild(basePkg + "." + vi, viName);

        // vo包
        FileBuild voFile = new FileBuild(basePkg + "." + vo, voName);

        FileBuild serviceFile = new FileBuild(basePkg, serviceName);

        makeCtrl(name, method, apiFile, serviceFile, viFile, voFile);
        makeVi(viFile);
        makeVo(voFile);

        makeService(serviceFile, viFile, voFile);

    }


    private static void makeCtrl(String name, String method, FileBuild apiFile, FileBuild serviceFile, FileBuild viFile, FileBuild voFile) {

        String methodAnnotation = null;
        String paramAnnotation = null;
        String methodImport = null;
        String paramImport = null;

        if ( "GET".equals(method) ) {
            methodAnnotation = "@GetMapping";
            paramAnnotation = "@ModelAttribute";
            methodImport = "import org.springframework.web.bind.annotation.GetMapping;";
            paramImport = "import org.springframework.web.bind.annotation.ModelAttribute;" + Const.NEW_LINE + "import org.springframework.web.bind.annotation.RequestParam;";
        } else {
            methodAnnotation = "@PostMapping";
            paramAnnotation = "@RequestBody";
            methodImport = "import org.springframework.web.bind.annotation.PostMapping;";
            paramImport = "import org.springframework.web.bind.annotation.RequestBody;";
        }


        StringBuild sb = new StringBuild();
        sb.append("package " + apiFile.pkg + ";");
        sb.newLine();
        sb.append("import org.springframework.validation.annotation.Validated;");
        sb.append("import io.swagger.v3.oas.annotations.tags.Tag;");
        sb.append("import io.swagger.v3.oas.annotations.Operation;");
        sb.append("import org.springframework.web.bind.annotation.RestController;");
        sb.append(methodImport);
        sb.append(paramImport);
        sb.append("import cc.bugcat.acore.framework.annotation.ACS;");
        sb.append("import cc.bugcat.acore.framework.entry.ResultEntity;");
        sb.append("import java.util.List;");

        sb.append("import " + baseCtrl + ";");
        sb.append("import " + viFile.pkg + "." + viFile.fileName + ";");
        sb.append("import " + voFile.pkg + "." + voFile.fileName + ";");

        sb.newLine();
        sb.newLine();

        sb.append("@Tag(name = \"文档描述\" )");
        sb.append("@RestController");
        sb.append("public class " + apiFile.fileName + " extends AbstractOpenApiCtrl {");
        sb.newLine();
        sb.newLine();
        sb.tab().append("@Operation(summary = \"api简述\")");
        sb.tab().append( methodAnnotation + "(\"\")");
        sb.tab().append("@ACS(any = true)");
        sb.tab().append("public ResultEntity<" + voFile.fileName + "> openApi(@Validated " + paramAnnotation + " " + viFile.fileName + " req) {");
        sb.tab().tab().append("return super.execute(" + serviceFile.fileName + ".class, req);");
        sb.tab().append("}");
        sb.newLine();
        sb.newLine();
        sb.append("}");
        apiFile.write(sb);
    }

    private static void makeVi(FileBuild viFile){
        StringBuild sb = new StringBuild();
        sb.append("package " + viFile.pkg + ";");
        sb.newLine();
        sb.append("import com.fasterxml.jackson.annotation.JsonProperty;");
        sb.append("import io.swagger.v3.oas.annotations.media.Schema;");
        sb.append("import java.util.List;");
        sb.append("import lombok.Data;");
        sb.newLine();
        sb.newLine();
        sb.append("@Data");
        sb.append("@Schema");
        sb.append("public class " + viFile.fileName + " {");
        sb.newLine();
        sb.newLine();
        sb.append("}");
        viFile.write(sb);
    }

    private static void makeVo(FileBuild voFile){
        StringBuild sb = new StringBuild();
        sb.append("package " + voFile.pkg + ";");
        sb.newLine();
        sb.append("import com.fasterxml.jackson.annotation.JsonProperty;");
        sb.append("import io.swagger.v3.oas.annotations.media.Schema;");
        sb.append("import java.util.List;");
        sb.append("import lombok.Data;");
        sb.newLine();
        sb.newLine();
        sb.append("@Data");
        sb.append("@Schema");
        sb.append("public class " + voFile.fileName + " {");
        sb.newLine();
        sb.newLine();
        sb.append("}");
        voFile.write(sb);
    }

    private static void makeService(FileBuild serviceFile, FileBuild viFile, FileBuild voFile){
        StringBuild sb = new StringBuild();
        sb.append("package " + serviceFile.pkg + ";");
        sb.newLine();
        sb.append("import org.springframework.stereotype.Service;");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;");
        sb.append("import org.springframework.transaction.annotation.Isolation;");
        sb.append("import org.springframework.transaction.annotation.Propagation;");
        sb.append("import org.springframework.transaction.annotation.Transactional;");
        sb.append("import java.util.List;");
        sb.append("import cc.bugcat.acore.framework.exception.BusinessException;");

        sb.append("import " + requestContext + ";");
        sb.append("import " + baseService + ";");
        sb.append("import " + viFile.pkg + "." + viFile.fileName + ";");
        sb.append("import " + voFile.pkg + "." + voFile.fileName + ";");

        sb.newLine();
        sb.newLine();

        sb.append("@Service");
        sb.append("public class " + serviceFile.fileName + " extends AbstractOpenApiService<" + viFile.fileName + ", " + voFile.fileName + "> {");
        sb.newLine();
        sb.newLine();
        sb.tab().append("@Override");
        sb.tab().append("@Transactional(readOnly = true)");
        sb.tab().append("//@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)");
        sb.tab().append("public " + voFile.fileName + " execute (" + viFile.fileName + " req, RequestContext context) {");
        sb.tab().tab().newLine();
        sb.tab().tab().newLine();
        sb.tab().tab().append("return null;");
        sb.tab().append("}");
        sb.append("}");
        serviceFile.write(sb);
    }

    private static class FileBuild {

        private static final String javaDir;
        static {
            String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            javaDir = new File(classPath).getParentFile().getParentFile().getPath() + "/src/main/java/";
        }

        private File file;
        private String pkg;
        private String fileName;

        public FileBuild(String basePkg, String fileName) {
            this.pkg = basePkg;
            this.fileName = fileName;
            File dir = new File((javaDir + pkg).replace(".", "/"));
            dir.mkdirs();
            this.file = new File(dir.getPath() + "/" + fileName + ".java");
        }
        public void write(StringBuild sb){
            try {
                if ( file.exists() ) {
                    return;
                }
                FileUtils.writeStringToFile(file, sb.toString(), "utf-8");
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    private static class StringBuild {

        private StringBuilder sbr = new StringBuilder();

        public StringBuild append(String text){
            sbr.append(text + Const.NEW_LINE);
            return this;
        }

        public StringBuild newLine(){
            sbr.append(Const.NEW_LINE);
            return this;
        }

        public StringBuild tab(){
            sbr.append("\t");
            return this;
        }

        public String toString(){
            return sbr.toString();
        }
    }

}
