package Tests;

import Instance.Instance;
import Resolution.Procedures;
import Resolution.Solver;
import Solution.Solution;

/**
 * Created by Maxterfike on 19/02/2016.
 */
public class ProceduresTest {
    public static void main(String[] args) {
        Solver solver = new Solver(new Instance("1M_3F.txt"));
        Solution mere = solver.firstSolution();
        Solution[] listSol = Procedures.random(mere);
        mere.print();
        listSol[0].print();
    }
}
