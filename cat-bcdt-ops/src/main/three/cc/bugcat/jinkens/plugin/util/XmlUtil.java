/**
 * Copyright (C), 2017-2020, 北京壹嘉商旅
 *
 * @author: 97224
 * @create: 2020/5/15 21:46
 * Desc:
 */
package cc.bugcat.jinkens.plugin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlUtil {
    
    private static final Pattern nodeValue = Pattern.compile("(<[^>]+>)(.*?)(<[^>]+>)");

    
    
    /**
     * 从标签中获取value值
     */
    public static String getValue(String xml) {
        Matcher matcher = nodeValue.matcher(xml);
        return matcher.find() ? matcher.group(2).trim() : null;
    }
    

    /**
     * 查询xml，得到标签和内容
     *
     * @param xml
     * @param tagName user name
     */
    public static String queryNode(String xml, String tagName) {
        return findAndReplaceNode(xml, tagName, null);
    }

    
    /**
     * 查询xml，得到标签中的内容
     *
     * @param xml
     * @param tagName user name
     */
    public static String queryValue(String xml, String tagName) {
        return getValue(queryNode(xml, tagName));
    }

    
    /*
     * 查询xml 得到集合，元素为标签和内容
     * @param xml
     * @param tagName user name
     * */
    public static List<String> queryNodes(String xml, String tagName) {
        String[] tags = tagName.replaceAll("\\s+", " ").split(" ");
        if ( tags.length > 1 ) {
            xml = queryNode(xml, substringBeforeLast(tagName, " "));
        }
        List<String> list = new ArrayList<>();
        String tag = tags[tags.length - 1];

        int index = -1, end = 0, pos = -1;
        while ( true ) {
            index = indexTag(xml, tag, pos + 1);
            if ( index == -1 ) {
                break;
            }
            end = xml.indexOf("</" + tag + ">", index);
            list.add(xml.substring(index, end + tag.length() + 3));
            pos = index;
        }
        return list;
    }

    /*
     * 查询xml 得到集合，元素为标签中的内容
     * @param xml
     * @param tagName user name
     * */
    public static List<String> queryValues(String xml, String tagName) {
        List<String> values = queryNodes(xml, tagName);
        int i = 0;
        for ( String value : values ) {
            values.set(i++, getValue(value));
        }
        return values;
    }


    public static String findAndReplaceNode(String xml, String tagName, String newString) {

        String[] tags = tagName.replaceAll("\\s+", " ").split(" "); // hudson views listView name

        boolean find = true;

        int index = -1, pos = -1;
        for ( String tag : tags ) {
            if ( isBlank(tag) ) {
                continue;
            }
            index = indexTag(xml, tag, pos + 1);
            if ( index == -1 ) {
                find = false;
                break;
            }
            pos = index;
        }
        if( find ){
            int lastPos = pos;
            for ( int i = tags.length - 1; i >= 0; i -- ) {
                String tag = tags[i];
                if ( isBlank(tag) ) {
                    continue;
                }
                index = xml.indexOf("</" + tag + ">", lastPos + 1);
                if ( index == -1 ) {
                    find = false;
                    break;
                }
                lastPos = index;
            }
        }

        if ( find ) {
            String last = tags[tags.length - 1];
            int end = xml.indexOf("</" + last + ">", pos);

            String node = xml.substring(pos, end + last.length() + 3);

            if ( isNotBlank(newString) ) {   //替换
                node = xml.substring(0, pos) + nodeValue.matcher(node).replaceAll("$1"+newString+"$3") + xml.substring(end + last.length() + 3, xml.length());
            }

            return node;
        } else {
            if ( isNotBlank(newString) ) {   //替换
                return xml;
            } else {
                return "";
            }
        }
    }

    

    private static int indexTag(String xml, String tag, int fromIndex){
        int length = xml.length();
        while ( length > fromIndex ) {
            int index = xml.indexOf("<" + tag, fromIndex);
            if( index == -1 ){
                // 没有找到，结束
                return -1;
            }

            //在判断标签位置后1位，是' '或者'>'
            int start = index + tag.length() + 1;   // <name
            char endchar = xml.charAt(start);
            if(endchar == ' ' || endchar == '>' ){
                return index;
            }
            fromIndex = start;
        }
        return -1;
    }




    public static String substringBeforeLast(final String str, final String separator) {
        if (isBlank(str) || isBlank(separator)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    
    public static String substringBetween(String str, String open, String close) {
        if (str != null && open != null && close != null) {
            int start = str.indexOf(open);
            if (start != -1) {
                int end = str.indexOf(close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }

            return null;
        } else {
            return null;
        }
    }
    
    
    public static final boolean isBlank(String str){
        return str == null || str.length() == 0;
    }
    
    public static final boolean isNotBlank(String str){
        return !isBlank(str);
    }

}
