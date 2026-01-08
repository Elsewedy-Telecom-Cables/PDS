package com.etc.pds.utils;
public class StageUtils {

    private StageUtils() {

    }

//    public static String normalize(String s) {
//        if (s == null) return "";
//        return s.toLowerCase()
//                .replaceAll("[\\s]", "")        // حذف المسافات
//                .replaceAll("[\\[\\]\\(\\){}]", "") // حذف الأقواس
//                .replaceAll("[^a-z0-9]", "");   // حذف أي شيء غير حرف/رقم
//    }

//    public static String normalize(String s) {
//        if (s == null || s.isEmpty()) {
//            return "";
//        }
//
//        return s.toLowerCase()
//                .replace("\r\n", "")   // Windows newlines
//                .replace("\n", "")     // Unix/Linux newlines
//                .replace("\r", "")     // Old Mac newlines
//                .replaceAll("\\s+", "") // أي مسافات متبقية (عادية، tab، multiple spaces)
//                .replace("-", "")
//                .replace("[", "")
//                .replace("]", "")
//                .replace("(", "")
//                .replace(")", "")
//                .replace("{", "")
//                .replace("}", "")
//                .replace("/", "")
//                .replace(".", "")
//                .replace("&", "")
//                .replaceAll("[^a-z0-9]", ""); // أخيرًا: احذف أي رمز غير حرف أو رقم (يحافظ على x و الأرقام)
//    }

//    public static String normalize(String s) {
//        if (s == null || s.isEmpty()) {
//            return "";
//        }
//
//        String cleaned = s.toLowerCase().replaceAll("\\s+", "");
//
//        return cleaned
//                .replace("-", "")
//                .replace("[", "")
//                .replace("]", "")
//                .replace("(", "")
//                .replace(")", "")
//                .replace("{", "")
//                .replace("}", "")
//                .replace("/", "")
//                .replace(".", "")
//                .replace("&", "")
//                .replace(",", "")
//                .replace(":", "")
//                .replace(";", "")
//                .replace("!", "")
//                .replace("?", "")
//                .replaceAll("[^a-z0-9]", "");  // أخيرًا احذف أي حاجة غريبة تانية
//    }
//
//    public static String normalize(String s) {
//        if (s == null || s.isEmpty()) {
//            return "";
//        }
//
//        return s.toLowerCase()
//                // حذف كل أنواع whitespace بما فيها non-breaking space (  = \u00A0)
//                .replace("\u00A0", " ")  // non-breaking space → space عادي
//                .replaceAll("\\s+", "")  // كل whitespace → فارغ
//                .replace("-", "")
//                .replace("[", "")
//                .replace("]", "")
//                .replace("(", "")
//                .replace(")", "")
//                .replace("/", "")
//                .replace(".", "")
//                .replace("&", "")
//                .replaceAll("[^a-z0-9]", "");  // أي رمز غريب متبقي
//    }



    public static String normalize(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        return s.toLowerCase()
                // حذف كل أنواع whitespace و line breaks و non-breaking space من الأول خالص
                .replaceAll("\\s+", "")
                .replace("\u00A0", "")    // non-breaking space
                .replace("\u2013", "-")   // en-dash → -
                .replace("\u2014", "-")   // em-dash → -
                .replace("\u2010", "-")   // hyphen → -
                .replace("\u202F", "")    // narrow no-break space
                .replace("\u2009", "")    // thin space
                .replace("\u2007", "")    // figure space
                // حذف كل العلامات الشائعة
                .replace("-", "")
                .replace("[", "")
                .replace("]", "")
                .replace("(", "")
                .replace(")", "")
                .replace("/", "")
                .replace(".", "")
                .replace("&", "")
                .replace(",", "")
                .replace(":", "")
                .replace(";", "")
                .replace("'", "")
                .replace("\"", "")
                .replaceAll("[^a-z0-9]", "");  // أي حاجة متبقية غير حرف أو رقم تمسح
    }



}
