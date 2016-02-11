package Solution;

import Instance.Instance;

/**
 * Created by n on 11/02/16.
 */
public class Stock {
    private int[] stockByType;
    private int[] cap;

    /**
     * Stocks of 1 machine at 2nd floor, ordered by product Type
     * @param instance
     * @param cap
     */
    public Stock(Instance instance, int[] cap) {
        stockByType = new int[instance.getNbTypes()];
        for (int i = 0; i < stockByType.length; i++) stockByType[i] = 0;
        this.cap = cap;
    }

    /**
     * @return true if the current state of this stocks respects the capacity constraints
     */
    public boolean isValid() {
        int numberOfDifferentTypes = 0;
        int total = 0;
        for(int i = 0; i<stockByType.length; i++){
            total+=stockByType[i];
            if(stockByType[i]>0)
                numberOfDifferentTypes++;
        }
        int totalCap = 0;
        for(int c = 0; c< cap.length; c++)  totalCap+=cap[c];

        switch(numberOfDifferentTypes){
            case 0: return true;
            case 1: return total<=totalCap;
            case 2: {
                int[] st = new int[2];
                int cursorSt = 0;
                int cursorType = 0;
                while(cursorSt<2){
                    if(stockByType[cursorType]>0){
                        st[cursorSt]=stockByType[cursorType];
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
                    if(stockByType[cursorType]>0){
                        st[cursorSt]=stockByType[cursorType];
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

    public void add (int quantity, int type){
        stockByType[type]+=quantity;
    }

    public static void order(int[] st, int a, int b){
        if(st[b]<st[a]){
            int loc = st[b];
            st[b]=st[a];
            st[a]=loc;
        }
    }
}
