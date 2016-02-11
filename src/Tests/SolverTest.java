package Tests;

import Instance.Instance;
import Resolution.Solver;
import Solution.Solution;

/**
 * Created by Maxterfike on 11/02/2016.
 */
public class SolverTest {

    public static void main(String[] args){
        Solver solver = new Solver(new Instance());
        Solution solution = solver.firstSolution();
        Solution solution2 = solution.clone();
        solution.print();
        solution2.print();
    }
}
