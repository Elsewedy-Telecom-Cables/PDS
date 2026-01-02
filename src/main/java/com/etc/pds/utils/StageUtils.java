package com.etc.pds.utils;
public class StageUtils {

    private StageUtils() {

    }

    public static String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase()
                .replaceAll("[\\s]", "")        // حذف المسافات
                .replaceAll("[\\[\\]\\(\\){}]", "") // حذف الأقواس
                .replaceAll("[^a-z0-9]", "");   // حذف أي شيء غير حرف/رقم
    }

        //    public static String normalize(String s) {
        //        if (s == null) return "";
        //        return s.toLowerCase().replaceAll("[^a-z0-9]", "");
        //    }

}
