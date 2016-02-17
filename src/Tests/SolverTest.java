package Tests;

import Instance.Instance;
import Resolution.Solver;
import Solution.Solution;

/**
 * Created by Maxterfike on 11/02/2016.
 */
public class SolverTest {

    public static void main(String[] args){
        Solver s = new Solver(new Instance());
        Solution solution = s.firstSolution();
        Solution solution2 = solution.clone();
        solution.print();
        solution2.print();

        System.out.println("-----");

        System.out.println("Hello");
        Solver solver = new Solver(new Instance("1M_3F.txt"));
        solver.solve(5);
        solver.getSolution().print();
        solver.getSolution().printPlanning();
        System.out.println(solver.getSolution().getCost());
        System.out.println(solver.getSolution().isValid());
    }
}
