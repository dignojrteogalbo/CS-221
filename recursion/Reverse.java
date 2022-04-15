public class Reverse {
    public static void main(String[] args) {
        String orignal = "!oohooW";
        String reversed = rev(orignal);

        System.out.println(orignal);
        System.out.println(reversed);
    }

    public static String rev(String str) {
        if (str.length() == 0) {
            return "";
        }

        return rev(str.substring(1)) + str.charAt(0);
        
    }
}
