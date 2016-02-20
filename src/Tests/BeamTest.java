package Tests;

import Instance.Instance;
import Resolution.Solver;
import Resolution.Beam;
import Solution.*;

/**
 * Created by n on 08/02/16.
 */
public class BeamTest {

    public static void main(String[] args) {

        Instance i = new Instance("known.txt");
        Solver s = new Solver(i);
        Solution sol = s.firstSolution();
        Beam b = new Beam(5, sol);

        System.out.println("FIRSTSOL");
        sol.print();

        b.procedure("random", 2000);
        System.out.println("-------------------Apr�s random-----------------------");
        b.print();
        b.procedure("bmp", 20);
        System.out.println("----------------------Apr�s bmp--------------------------");
        //b.print();
        System.out.println("---------------------Best solution---------------------------");
        //b.bestSolution().printPlanning();

    }
}
