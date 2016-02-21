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
        Solver solver = new Solver(new Instance("known.txt"));
        Solution mere = solver.firstSolution();
        mere.print();
        Tneh(mere);
    }

    public static void Tr(Solution mere){
        System.out.println("TEST de RANDOM");
        Solution[] listSol = Procedures.random(mere);
        Arrays.sort(listSol, new SolutionComparator());
        for(Solution s : listSol)s.printShort();
    }

    public static void Tneh(Solution mere){
        System.out.println("TEST de NEH");
        Solution[] list = Procedures.neh(mere);
        Arrays.sort(list, new SolutionComparator());
        for(Solution s: list)s.printShort();
    }

    public static void Tbmp(Solution mere){
        System.out.println("TEST de BMP");
        Solution[] list = Procedures.bmp(mere);
        Arrays.sort(list, new SolutionComparator());
        for(Solution s : list)s.printShort();    }
}
