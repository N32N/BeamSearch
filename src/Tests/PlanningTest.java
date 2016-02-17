package Tests;
import Resolution.Solver;
import Solution.*;
import Instance.Instance;
import com.sun.org.apache.xpath.internal.SourceTree;

/**
 * Created by n on 09/02/16.
 */
public class PlanningTest {

    public static void main(String[] args) {
        Solution s = new Solution(new Instance());
        s.addJob(new ScheduledJob(s.getInstance().getJob(1), s.getLastJobFirstFloor(1).getNext()), 1, 1);
        s.addJob(new ScheduledJob(s.getInstance().getJob(1), s.getLastJobSecondFloor(2).getNext()), 2, 2);
        Planning test = new Planning(new Instance(), s);
        test.print();


        System.out.println("----");


        Solver solver = new Solver(new Instance("1M_3F.txt"));
    }
}
