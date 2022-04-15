import java.util.HashMap;

public class Fib {
    public static HashMap<Integer, Integer> dp = new HashMap<>();
    public static int calls = 0;

    public static void main(String[] args) {
        dp.put(1, 0);
        dp.put(2, 1);
        dp.put(3, 1);
    
        for (int i = 20; i > 0; i--) {
            System.out.println(fib(i));
        }
    }

    public static int fib(int n) {
        if (dp.get(n) != null) {
            return dp.get(n);
        } 

        dp.put(n, fib(n-1) + fib(n-2));
        return fib(n);
    }
}