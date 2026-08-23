package cc.bugcat.bcdt.ops.constants;


import cc.bugcat.acore.framework.spi.ErrorCode;


/**
 * 异常枚举
 * 
 * @author bugcat-cc
 */
public enum  CodeErrors implements ErrorCode {



    userFail("3920", "%s"),
    
    
    
    ;


    public final String code;


    /**
     * 错误说明
     */
    public final String msg;


    /**
     * 异常级别
     * */
    public final Level level;


    CodeErrors (String code, String msg) {
        this(code, msg, Level.def);
    }
    CodeErrors (String code, String msg, Level level) {
        this.code = code;
        this.msg = msg;
        this.level = level;
    }



    @Override
    public String getCode () {
        return code;
    }

    @Override
    public String getMsg () {
        return msg;
    }

    @Override
    public Level getLevel() {
        return level;
    }

}
