package Tests;

import Solution.Solution;
import Solution.ScheduledJob;
import Instance.Instance;

/**
 * Created by n on 08/02/16.
 */
public class SolutionTest {

    public static void main(String[] args) {
        Solution test = new Solution(new Instance());
        test.print();
        test.addJob(new ScheduledJob(32,1,1,1,1),2,2);
        test.print();
        System.out.println("last second 2 : "+test.getLastJobSecondFloor(2));
        System.out.println("last second 3 : "+test.getLastJobSecondFloor(3));
        System.out.println("last first 1 : "+test.getLastJobFirstFloor(1));

        test.printShort();

        System.out.println("piege : lest first 3 : "+test.getLastJobFirstFloor(3));
    }
}
