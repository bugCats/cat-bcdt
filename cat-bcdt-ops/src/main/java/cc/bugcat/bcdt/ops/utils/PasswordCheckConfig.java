package cc.bugcat.bcdt.ops.utils;


/**
 *
 *
 * @author bugcat-cc
 */
public class PasswordCheckConfig {

    /**
     * 是否检测密码口令长度
     */
    public static boolean CHECK_PASSWORD_LENGTH = true;
    
    /**
     * 密码最小长度，默认为8
     */
    public static int MIN_LENGTH = 8;
    
    /**
     * 密码最大长度，默认为30
     */
    public static int MAX_LENGTH = 24;


    /**
     * 是否包含数字
     * */
    public static boolean CHECK_CONTAIN_DIGIT = true;


    /**
     * 是否包含字母
     * */
    public static boolean CHECK_CONTAIN_CASE = true;

    /**
     * 是否区分大小写
     */
    public static boolean CHECK_DISTINGGUISH_CASE = false;


    /**
     * 是否包含小写字母
     */
    public static boolean CHECK_LOWER_CASE = false;


    /**
     * 是否包含大写字母
     */
    public static boolean CHECK_UPPER_CASE = false;


    /**
     * 是否包含特殊符号
     */
    public static boolean CHECK_CONTAIN_SPECIAL_CHAR = false;
    /**
     * 特殊符号集合
     */
    public static String SPECIAL_CHAR = "~!@#$%^&*()_+[]{};'\\:\"|,./<>?";


    /**
     * 是否检测键盘按键横向连续
     */
    public static boolean CHECK_HORIZONTAL_KEY_SEQUENTIAL = true;
    /**
     * 键盘物理位置横向不允许最小的连续个数
     */
    public static int LIMIT_HORIZONTAL_NUM_KEY = 4;


    /**
     * 是否检测键盘按键斜向连续
     */
    public static boolean CHECK_SLOPE_KEY_SEQUENTIAL = true;
    /**
     * 键盘物理位置斜向不允许最小的连续个数
     */
    public static int LIMIT_SLOPE_NUM_KEY = 4;


    /**
     * 是否检测逻辑位置连续
     */
    public static boolean CHECK_LOGIC_SEQUENTIAL = false;
    /**
     * 密码口令中字符在逻辑位置上不允许最小的连续个数
     */
    public static int LIMIT_LOGIC_NUM_CHAR = 4;


    /**
     * 是否检测连续字符相同
     */
    public static boolean CHECK_SEQUENTIAL_CHAR_SAME = true;
    /**
     * 密码口令中相同字符不允许最小的连续个数
     */
    public static int LIMIT_NUM_SAME_CHAR = 3;


    /**
     * 键盘横向方向规则
     */
    public static String[] KEYBOARD_HORIZONTAL_ARR = {"01234567890", "qwertyuiop", "asdfghjkl", "zxcvbnm"};
    
    /**
     * 键盘斜线方向规则
     */
    public static String[] KEYBOARD_SLOPE_ARR = {
            "1qaz", "2wsx", "3edc", "4rfv", "5tgb", "6yhn", "7ujm", "8ik,", "9ol.",
            "0p;/", "=[;.", "-pl,", "0okm", "9ijn", "8uhb", "7ygv", "6tfc", "5rdx", "4esz"};

    

    /**
     * 是否检测常用词库
     */
    public static boolean CHECK_SIMPLE_WORD = true;

    /**
     * 常用词库
     */
    public static String[] SIMPLE_WORDS = {
            "admin", "pass", "word", "root", "guest", "user", "success", 
            "test", "demo", "enter", 
            "cookie", "window", "null", "define", "love", "you", "fuck",
            "123", "456", "789", "147", "258", "369", "master", "ewq", "qwe"};
 

    
}
