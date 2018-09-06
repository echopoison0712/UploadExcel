package com.echoyy.util;

/**
 * @ClassName VersionUtil
 * @Description
 * @Author echoyy
 * @Date 2018/8/29 下午6:29
 * @Version 1.0
 */
public class VersionUtil {

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

}
