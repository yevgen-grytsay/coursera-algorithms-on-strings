import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NonSharedSubstringBruteForce implements Runnable {


    private List<String> solve(String p, String q) {
        List<String> result = new ArrayList<>();

        int smallest = p.length() + q.length();
        for (int i = 0; i < p.length() - 1; i++) {
            for (int j = i + 1; j < p.length(); j++) {
                String substring = p.substring(i, j);
                if (!q.contains(substring) && substring.length() < smallest) {
                    result.add(substring);
                    smallest = substring.length();
                }
            }
        }

        return result;
    }


    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String p = in.readLine();
            String q = in.readLine();

            List<String> ans = solve(p, q);
            ans.forEach(System.out::println);

            System.out.println("---");

            List<String> ans2 = solve(q, p);
            ans2.forEach(System.out::println);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Thread(new NonSharedSubstringBruteForce()).start();
    }
}
