import Resolution.Solver;
import Instance.Instance;
import Solution.Solution;

/**
 * Created by n on 08/02/16.
 */
public class Main {

    public static void main(String[] arg) {
        Solver s = new Solver(new Instance("big.txt"));
        Solution first = s.firstSolution();
        System.out.println("FIRST SOLUTION");
        first.print();

        s.solve(10);
        Solution best = s.getSolution();
        best.printPlanning();
    }
}
