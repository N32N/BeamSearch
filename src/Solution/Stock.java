package Solution;

import Instance.Instance;

/**
 * Created by n on 11/02/16.
 */
public class Stock {
    private int[] actualStock;
    private int[] cap;

    /**
     * Stocks of 1 machine at 2nd floor : three containers with capacities
     * @param instance
     * @param cap
     */
    public Stock(Instance instance, int[] cap) {
        actualStock = new int[cap.length+3];
        for (int i = 0; i < actualStock.length; i++) actualStock[i] = 0;
        this.cap = cap;
    }

    /**
     * @return true if the current state of this stocks respects the capacity constraints
     */
    public boolean isValid() {
        int numberOfOrders = 0;
        int total = 0;
        for(int i = 0; i< actualStock.length; i++){
            total+= actualStock[i];
            if(actualStock[i]>0)
                numberOfOrders++;
        }
        int totalCap = 0;
        for(int c = 0; c< cap.length; c++)  totalCap+=cap[c];

        switch(numberOfOrders){
            case 0: return true;
            case 1: return total<=totalCap;
            case 2: {
                int[] st = new int[2];
                int cursorSt = 0;
                int cursorType = 0;
                while(cursorSt<2){
                    if(actualStock[cursorType]>0){
                        st[cursorSt]= actualStock[cursorType];
                        cursorSt++;
                    }
                    cursorType++;
                }
                order(st, 0, 1);
               return ( ((st[0]<=cap[0]) && (st[1]<=(cap[1]+cap[2]))) || ((st[0]<=cap[1]) && (st[1]<=(cap[0]+cap[2]))) || ((st[0]<=cap[2]) && (st[1]<=(cap[1]+cap[0]))) );
            }
            case 3: {
                int[] st = new int[3];
                int cursorSt = 0;
                int cursorType = 0;
                while(cursorSt<3){
                    if(actualStock[cursorType]>0){
                        st[cursorSt]= actualStock[cursorType];
                        cursorSt++;
                    }
                    cursorType++;
                }
                order(st, 1, 2);
                order(st, 0, 1);
                order(st, 1, 2);
                return (cap[0]>=st[0])&&(cap[1]>=st[1])&&(cap[2]>=st[2]);
            }
            default: return false;
        }
    }

    /**
     * Adds one order (with its quantity) in the stocks
     * @param quantity
     */
    public void add (int quantity){
        boolean added = false;
        int i = 0;
        while(!added && i<actualStock.length){
            if(actualStock[i]==0){
                actualStock[i]=quantity;
                added=true;
            }
            else i++;
        }
    }

    /**
     * Removes the order with that quantity from the stocks
     * @param quantity
     */
    public void remove (int quantity){
        boolean removed = false;
        int i = 0;
        while(!removed && i<actualStock.length){
            if(actualStock[i]==quantity){
                actualStock[i]=0;
                removed=true;
            }
            else i++;
        }
    }

    /**
     * Modifies "st" so as to obtain st[a] <= st[b]
     * @param st
     * @param a
     * @param b
     */
    public static void order(int[] st, int a, int b){
        if(st[b]<st[a]){
            int loc = st[b];
            st[b]=st[a];
            st[a]=loc;
        }
    }
}
