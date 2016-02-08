import Resolution.Solver;
import Instance.Instance;

/**
 * Created by n on 08/02/16.
 */
public class Main {

    public static void main(String[] arg) {
        System.out.println("Hello");
        Solver solver = new Solver(new Instance());
        solver.solve(5);
    }
}
