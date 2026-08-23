package cc.bugcat.bcdt.ops.service.mysql.logger;


import java.security.MessageDigest;
import java.util.regex.Pattern;


/**
 * @author bugcat
 * */
public class ByteUtil {
    
    private static final Pattern EMPTY = Pattern.compile("\\s+");
    
    
    /**
     * 获取包长度
     * @param offset 偏移量
     * @param length 读取长度   3
     * */
    public static int bytesToInt(byte[] src, int offset, int length) {
        int value = 0x00;
        switch ( length ) {
            case 4: value = (value | ((src[offset + 3] & 0xFF) << 24));
            case 3: value = (value | ((src[offset + 2] & 0xFF) << 16));
            case 2: value = (value | ((src[offset + 1] & 0xFF) << 8));
            case 1: value = (value | (src[offset] & 0xFF));
            default: break;
        }
        return value;
    }
    
    /**
     * 包长度转字节
     * @param length 生成字节长度 3
     * */
    public static byte[] intToBytes(int integer, int length){
        byte[] bytes = new byte[length];
        switch ( length ) {
            case 4: bytes[3] = (byte) (integer >> 24 & 0xff);
            case 3: bytes[2] = (byte) (integer >> 16 & 0xff);
            case 2: bytes[1] = (byte) (integer >> 8 & 0xff);
            case 1: bytes[0] = (byte) (integer & 0xff);
            default: break;
        }
        return bytes;
    }
    

    /**
     * 二进制数组转十六进制字符串
     * */
    public static String byteToHex(byte[] bytes) {
        StringBuilder sbr = new StringBuilder(bytes.length * 3 + 32);
        for ( byte b : bytes ) {
            sbr.append("," + byteToHex(b));
        }
        return sbr.length() > 0 ? sbr.deleteCharAt(0).toString() : "";
    }

    
    /**
     * 二进制转十六进制
     * */
    public static String byteToHex(byte bt) {
        String hex = Integer.toHexString(bt & 0xFF);
        if ( hex.length() < 2 ) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 十六进制数组转二进制数组
     * */
    public static byte[] hexToByte(String[] hex) {
        int byteLen = hex.length; // 每两个字符描述一个字节
        byte[] ret = new byte[byteLen];
        for ( int i = 0; i < byteLen; i++ ) {
            ret[i] = (byte) Integer.parseInt(hex[i], 16);
        }
        return ret;
    }

    /**
     * 十六进制数组转二进制数组
     * */
    public static byte[] hexToByte(String hexs) {
        return hexToByte(hexs.split(","));
    }

    /**
     * 判断2个byte[]内容是否相等
     * */
    public static boolean equals(byte[] bytes1, byte[] bytes2){
        if( bytes1.length != bytes2.length ){
            return false;
        }
        for(int idx = 0; idx < bytes1.length; idx ++ ){
            if( bytes1[idx] != bytes2[idx] ){
                return false;
            }
        }
        return true;
    }

    public static boolean notEquals(byte[] bytes1, byte[] bytes2) {
        return !equals(bytes1, bytes2);
    }
    
    /**
     * 追加
     * */
    public static byte[] concat(byte[]... bytes){
        if( bytes == null || bytes.length == 0 ){
            return new byte[0];
        }
        int size = 0;
        for (byte[] bit : bytes ) {
            size = size + bit.length;
        }
        byte[] result = new byte[size];
        int start = 0, length = 0;
        for (byte[] bit : bytes ) {
            length = bit.length;
            System.arraycopy(bit, 0, result, start, length);
            start = start + length;
        }
        return result;
    }
    
    
    /**
     * 去除换行、空白符
     * */
    public static String trim(String text){
        return EMPTY.matcher(text).replaceAll(" ");
    }

    /**
     * 从offset位置开始读取，
     * 从非null开始，直到下一个null为止
     * */
    public static ReaderEntry readToNull(byte[] bytes, int offset){
        ReaderEntry reader = new ReaderEntry();
        int length = bytes.length;
        int start = -1;
        int end = -1;
        for(int idx = offset; idx < length; idx ++ ){
            if ( start < 0 && bytes[idx] != 0x00 ) {
                start = idx;
            }
            if( start >= 0 && bytes[idx] == 0x00 ){
                end = idx;
                break;
            }
        }
        reader.start = start;
        reader.end = end;

        if( start > -1 && end >= start ) {
            int readSize = end - start;
            byte[] read = new byte[readSize];
            System.arraycopy(bytes, start, read, 0, readSize);
            reader.bytes = read;
        } else {
            reader.bytes = new byte[0];
        }
        
        return reader;
    }

    /**
     * 从offset位置开始读取，
     * 从null开始，直到下一个非null
     * */
    public static ReaderEntry readNull(byte[] bytes, int offset){
        ReaderEntry reader = new ReaderEntry();
        int length = bytes.length;
        int start = -1;
        int end = -1;
        for(int idx = offset; idx < length; idx ++ ){
            if ( start < 0 && bytes[idx] == 0x00 ) {
                start = idx;
            }
            if( start >= 0 && bytes[idx] != 0x00 ){
                end = idx;
                break;
            }
        }
        reader.start = start;
        reader.end = end;

        if( start > -1 && end >= start ) {
            int readSize = end - start;
            byte[] read = new byte[readSize];
            System.arraycopy(bytes, start, read, 0, readSize);
            reader.bytes = read;
        } else {
            reader.bytes = new byte[0];
        }
        return reader;
    }
    
    /**
     * mysql 密码加密：sha1(password) XOR sha1("20-bytes random data from server" + sha1(sha1(password)))
     * mysql_native_password 
     * */
    public static byte[] mysqlNativePassword(byte[] pass, byte[] seed) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1D1 = md.digest(pass);
            md.reset();
            byte[] sha1D2 = md.digest(sha1D1);
            md.reset();

            md.update(seed);
            md.update(sha1D2);
            byte[] toBeXord = md.digest();
            for (int idx = 0; idx < toBeXord.length; idx++) {
                toBeXord[idx] = (byte) (toBeXord[idx] ^ sha1D1[idx]);
            }
            return toBeXord;
        } catch ( Exception ex ) {
            return pass;
        }
    }

    
    
    public static class ReaderEntry {
        private int start;
        private int end;
        private byte[] bytes;

        public int getStart() {
            return start;
        }
        public int getEnd() {
            return end;
        }
        public byte[] getBytes() {
            return bytes;
        }
    }
}
