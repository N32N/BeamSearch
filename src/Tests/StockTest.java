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
        System.out.println(""+s.isValid());
        s.add(30);
        System.out.println("T"+(s.isValid()==true));
        s.add(50);
        System.out.println("T"+(s.isValid()==true));
        s.add(5);
        System.out.println("T"+(s.isValid()==true));
        s.remove(30);
        System.out.println("T"+(s.isValid()==true));
        s.add(30);
        System.out.println("T"+(s.isValid()==true));
        s.remove(5);
        System.out.println("T"+(s.isValid()==true));
        s.add(50);
        System.out.println("T"+(s.isValid() == true));
        s.add(30);
        System.out.println("T"+(s.isValid() == false));
        s.add(5);
        System.out.println("T"+(s.isValid() == false));
    }
}
