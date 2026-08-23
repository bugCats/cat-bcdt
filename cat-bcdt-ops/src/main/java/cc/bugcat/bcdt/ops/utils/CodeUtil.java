package cc.bugcat.bcdt.ops.utils;

import cc.bugcat.acore.framework.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Random;


/**
 * @author bugcat-cc
 * */
public class CodeUtil {

    /**
     * 字典 21 7678 2336 种可能
     * */
    private final static char[] dicts = "Kh2VZe3If07dtagC8o5Pmb4u6XjQ1Wry9LSn".toCharArray(); // 36进制
    private final static int[] indexs = new int[] {3, 6, 1, 8, 5, 0, 2, 4, 7};
    
    /**
     * 锚点
     * */
    private final static LocalDate START = DateUtil.ss.from("2000-10-01");  // 星期日
    
    
    
    /**
     * 数据库子账号、密码
     * 默认是 7 天变动一次；此处根据实际需求，设置变动频率；
     * */
    public static String userCode(String account, String realname){
        
        // 相距天数
        long days = DateUtil.getDayDiff(LocalDate.now(), START);
        
        // 相距周数
        long weeks = days / 7; //每7天变一次

        // 周数3位数，可用100年
        long bitWeeks = weeks % 10000; // 0 ~ 9999
        
        // 账号 + 姓名 hashCode
        long nameHashCode = Math.abs((account + realname).hashCode());
        long bitHashCode = ( nameHashCode * weeks ) % 10000000000L;

        String nameHash = new StringBuilder(StringUtils.leftPad(String.valueOf(bitWeeks), 4, "0")).reverse().toString();
        String bitHash = new StringBuilder(StringUtils.leftPad(String.valueOf(bitHashCode), 10, "0")).reverse().toString();

        long bigUserCode = Long.parseLong(nameHash + bitHash);
        
        int scale = dicts.length;
        char[] chars = new char[9];
        for(int idx : indexs){
            int mod = (int)(bigUserCode % scale);
            chars[idx] = dicts[mod];
            bigUserCode = bigUserCode / scale;
        }

        String userCode = new String(chars);
        return userCode;
    }
    
    
    
    /**
     * 得到授权码 9位，每次都不一样
     * */
    public static String authCode() {

        long subSec = System.currentTimeMillis() / 1000;
        String bitSec = String.valueOf(subSec % 1000000000);
        
        Random random = new Random();
        String bitCode = String.valueOf(random.nextInt(100000));
        
        String bigCode = new StringBuilder(
                StringUtils.leftPad(bitSec, 9, "0") +
                StringUtils.leftPad(bitCode, 5, "0")
                ).reverse().toString();

        long authCode = Long.parseLong(bigCode);
        int scale = dicts.length;
        char[] chars = new char[9];
        for(int idx = 0; idx < 9; idx ++ ){
            int mod = (int)(authCode % scale);
            chars[idx] = dicts[mod];
            authCode = authCode / scale;
        }
        return new String(chars);
    }

    
}
