//package model;
//public class NumberToWordsConverter {
//    private static final String[] units = {
//        "", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười",
//        "mười một", "mười hai", "mười ba", "mười bốn", "mười lăm", "mười sáu", "mười bảy", "mười tám", "mười chín"
//    };
//
//    private static final String[] tens = {
//        "",        // 0
//        "",        // 1
//        "hai mươi",  // 2
//        "ba mươi",  // 3
//        "bốn mươi",  // 4
//        "năm mươi",  // 5
//        "sáu mươi",  // 6
//        "bảy mươi",  // 7
//        "tám mươi",  // 8
//        "chín mươi"   // 9
//    };
//
//    public static String convert(final long n) {
//        if (n < 0) {
//            return "âm " + convert(-n);
//        }
//
//        if (n < 20) {
//            return units[(int) n];
//        }
//
//        if (n < 100) {
//            return tens[(int) n / 10] + ((n % 10 != 0) ? " " : "") + units[(int) n % 10];
//        }
//
//        if (n < 1000) {
//            return units[(int) n / 100] + " trăm" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
//        }
//
//        if (n < 1000000) {
//            return convert(n / 1000) + " nghìn" + ((n % 1000 != 0) ? " " : "") + convert(n % 1000);
//        }
//
//        if (n < 1000000000) {
//            return convert(n / 1000000) + " triệu" + ((n % 1000000 != 0) ? " " : "") + convert(n % 1000000);
//        }
//
//        return convert(n / 1000000000) + " tỷ" + ((n % 1000000000 != 0) ? " " : "") + convert(n % 1000000000);
//    }
//
//    public static String convert(double n) {
//        if (n < 0) {
//            return "âm " + convert(-n);
//        }
//
//        long integerPart = (long) n;
//        long fractionalPart = Math.round((n - integerPart) * 100); // Lấy 2 chữ số thập phân
//
//        String integerPartWords = convert(integerPart);
//        String fractionalPartWords = convert(fractionalPart);
//        if (fractionalPartWords.length()<1){
//            return integerPartWords;
//        }
//        else {
//            return integerPartWords + " phẩy " + fractionalPartWords;
//        }
//    }
//}
