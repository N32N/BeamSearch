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
        Beam b = set();
        //testLeger(b);
        //testLourd(b);
        testPSet(b);
    }

    public static Beam set(){
        Instance i = new Instance("unknown.txt");
        Solver s = new Solver(i);
        Solution sol = s.firstSolution();
        Beam b = new Beam(100, sol);

        System.out.println("FIRSTSOL");
        sol.print();
        return b;
    }

    public static void testPSet(Beam b){
        b.procedureSet(20);
        b.print();
    }

    public static void testLourd(Beam b){
        b.procedure("random", 200);
        b.procedure("bmp", 200);
        b.procedure("neh", 200);
        b.procedure("random", 200);
        b.procedure("bmp", 200);
        b.procedure("neh", 200);
        System.out.println("---------------------Best solution---------------------------");
        b.bestSolution().printPlanning();
    }

    public static void testLeger(Beam b){
        b.procedure("random", 20);
        System.out.println("-------------------Apr�s random-----------------------");
        b.print();
        b.procedure("bmp", 20);
        System.out.println("----------------------Apr�s bmp--------------------------");
        b.print();
        b.procedure("neh", 20);
        System.out.println("----------------------Apr�s neh--------------------------");
        b.print();
    }
}
