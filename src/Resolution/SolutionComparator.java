package Resolution;

import Solution.Solution;

import java.util.Comparator;

/**
 * Created by Maxterfike on 17/02/2016.
 */
public class SolutionComparator implements Comparator<Solution> {

    /**
     * @param sol1
     * @param sol2
     * @return the difference (sol1.cost - sol2.cost)
     */
    public int compare(Solution sol1, Solution sol2){
        return (int) (sol1.getCost() - sol2.getCost());
    }
}
