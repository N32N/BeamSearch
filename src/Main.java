import Resolution.Solver;
import Instance.Instance;

/**
 * Created by n on 08/02/16.
 */
public class Main {

    public static void main(String[] arg) {
        System.out.println("Hello");
        Solver solver = new Solver(new Instance("1M_3F.txt"));
        solver.solve(5);
        solver.getSolution().print();
        solver.getSolution().printPlanning();
        System.out.println(solver.getSolution().getCost());
        System.out.println(solver.getSolution().isValid());
    }
}
