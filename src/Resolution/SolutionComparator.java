package Resolution;

import Solution.Solution;

import java.util.Comparator;

/**
 * Created by Maxterfike on 17/02/2016.
 */
class SolutionComparator implements Comparator<Solution> {

    @Override
    public int compare(Solution sol1, Solution sol2){
        return (int) (sol1.getCost() - sol2.getCost());
    }
}
