package Resolution;

import Solution.Solution;

import java.util.Comparator;

/**
 * Created by Maxterfike on 17/02/2016.
 */
class SolutionComparator implements Comparator<Solution> {

    @Override
    /**
     * return the difference (sol1.cost - sol2.cost)
     */
    public int compare(Solution sol2, Solution sol1){
        return (int) (sol1.getCost() - sol2.getCost());
    }
}
