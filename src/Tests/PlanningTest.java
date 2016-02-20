package Tests;
import Resolution.Solver;
import Solution.*;
import Instance.Instance;
import Solution.Planning;

/**
 * Created by n on 09/02/16.
 */
public class PlanningTest {

    public static void main(String[] args) {
        Instance in = new Instance("known.txt");
        Solver solver = new Solver(in);
        Solution sol = solver.firstSolution();
        Planning p = new Planning(in, sol);

        sol.print();
        System.out.printf("allJobOk : "+p.allJobOk());
        //sol.printPlanning();
    }
}
