package Tests;

import Resolution.Solver;
import Solution.*;
import Instance.Instance;
import Solution.Planning;

/**
 * Created by n on 09/02/16.
 */
public class PlanningTest {

    public static void main(String[] args) {
        Instance in = new Instance("petite.txt");
        Solver solver = new Solver(in);
        Solution sol = solver.firstSolution();
        Planning p = new Planning(in, sol);

        sol.print();
        System.out.println("allJobOk : "+p.allJobOk());
        System.out.println("checkStock : " + p.checkStock());
        sol.printPlanning();
        //testDate(p);
        //testNeedStock(p);
        //testStockUpdate(p, in);

    }

    public static void testStockUpdate(Planning p, Instance instance){
        Stock[] stocks = new Stock[instance.getNbM2()];
        for (int s = 0; s < stocks.length; s++)
            stocks[s] = new Stock(instance, instance.getStockCapa(s));
        System.out.println(""+p.stockUpdate(stocks, 0, 0));
        System.out.println(""+p.stockUpdate(stocks, 1, 0));
    }

    public static void testNeedStock(Planning p){
        boolean[] n = p.needStock();
        for(int i = 0; i<n.length;i++) System.out.println("job "+(i+1)+" - "+n[i]);
    }

    public static void testDate(Planning p) {
        System.out.println("FIRST");
        System.out.println("1 (32) : " + p.date(0, 0));
        System.out.println("3 (133) : " + p.date(0, 4));
        System.out.println("6 (113) : " + p.date(0, 3));

        System.out.println("SECOND");
        System.out.println("1 (32) : " + p.date(1, 0));
        System.out.println("3 (133) : " + p.date(2, 1));
        System.out.println("6 (152) : " + p.date(3, 1));
    }
}