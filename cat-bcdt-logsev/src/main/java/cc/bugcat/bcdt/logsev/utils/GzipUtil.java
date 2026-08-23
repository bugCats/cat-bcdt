package cc.bugcat.bcdt.logsev.utils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;


/**
 *
 * @author bugcat-cc
 * */
public class GzipUtil {

    public static byte[] encrypt(byte[] data) {
        if (data == null || data.length == 0) {
            return data;
        }
        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream();
              GZIPOutputStream gzipOut = new GZIPOutputStream(bos)) {
            gzipOut.write(data);
            gzipOut.finish();
            return bos.toByteArray();
        } catch ( Exception ex ) {
            return data;
        }
    }
    
    
    public static byte[] encrypt(String data) {
        if ( data == null ) {
            return null;
        }
        return encrypt(data.getBytes(StandardCharsets.UTF_8));
    }
}

