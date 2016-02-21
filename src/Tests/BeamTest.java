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

        b.procedure("random", 200);
        System.out.println("-------------------Apr�s random-----------------------");
        b.print();
        b.procedure("bmp", 200);
        System.out.println("----------------------Apr�s bmp--------------------------");
        b.print();
        b.procedure("neh", 200);
        System.out.println("----------------------Apr�s neh--------------------------");
        b.print();

        b.procedure("random", 200);
        b.procedure("bmp", 200);
        b.procedure("neh", 200);
        b.procedure("random", 200);
        b.procedure("bmp", 200);
        b.procedure("neh", 200);
        System.out.println("---------------------Best solution---------------------------");
        b.bestSolution().printPlanning();

    }
}
