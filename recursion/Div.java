import java.util.HashMap;

public class Div {
    public static int divTimes = 0;

    public static void main(String[] args) {
        System.out.println(div(10, 3));
    }

    public static int div(int numerator, int denom) {
        if (numerator <= denom) {
            System.out.println("Remainder is: " + numerator);
            return divTimes;
        }

        divTimes++;
        return div(numerator - denom, denom);
    }
}
