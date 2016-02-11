package Tests;

import Solution.Stock;
import Instance.Instance;

/**
 * Created by n on 11/02/16.
 */
public class StockTest {

    public static void main(String[] args) {
        Instance i = new Instance();
        Stock s = new Stock(i, i.getStockCapa(1));
        System.out.println("T"+s.isValid());

        s.add(30, 1);
        System.out.println("T"+s.isValid());

        s.add(50, 2);
        System.out.println("T"+s.isValid());

        s.add(5, 0);
        System.out.println("T"+s.isValid());

        s.add(-30, 1);
        System.out.println("T"+s.isValid());

        s.add(30, 2);
        System.out.println("T"+s.isValid());

        s.add(-5, 0);
        System.out.println("T"+s.isValid());

        s.add(50, 2);
        System.out.println("T"+s.isValid());

        s.add(30, 2);
        System.out.println("T"+s.isValid());

        s.add(5, 0);
        System.out.println("T"+s.isValid());
    }

}
