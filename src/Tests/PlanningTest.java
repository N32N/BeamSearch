package Tests;
import Solution.Planning;
import Solution.Solution;
import Instance.Instance;
/**
 * Created by n on 09/02/16.
 */
public class PlanningTest {

    public static void main(String[] args) {
        Planning test = new Planning(new Instance(), new Solution(new Instance()));

        test.print();
    }
}
