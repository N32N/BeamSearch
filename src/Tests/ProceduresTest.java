package Tests;

import Instance.Instance;
import Resolution.*;
import Solution.Solution;
import java.util.Arrays;

/**
 * Created by Maxterfike on 19/02/2016.
 */
public class ProceduresTest {
    public static void main(String[] args) {
        Solver solver = new Solver(new Instance("3M_5F.txt"));
        Solution mere = solver.firstSolution();

        System.out.println("TEST de RANDOM");
        Solution[] listSol = Procedures.random(mere);
        mere.printShort();
        Arrays.sort(listSol, new SolutionComparator());
        listSol[0].printShort();

        System.out.println("TEST de BMP");
        Solution[] list = Procedures.bmp(mere);
        mere.printShort();
        Arrays.sort(list, new SolutionComparator());
        list[0].printShort();
    }
}
