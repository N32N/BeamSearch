package Tests;

import  Instance.Instance;
import  Resolution.Solver;
import  Resolution.Beam;
import Solution.Solution;

/**
 * Created by n on 08/02/16.
 */
public class BeamTest {

    public static void main(String[] args) {

        Instance i = new Instance("1M_3F.txt");
        Solver s = new Solver(i);
        Solution sol = s.firstSolution();
        Beam b = new Beam(5, sol);

        System.out.println("FIRSTSOL cost : "+sol.getCost());

        for (int a = 0; a < b.beam.length; a++)
            b.beam[a].setCost(b.beam[a].getCost()+a);
        Solution s32 = sol.clone();
        s32.setCost(s32.getCost()+32);
        b.potential.add(sol.clone());
        b.potential.add(s32);

        b.select();
        b.print();
    }
}
