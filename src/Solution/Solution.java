package Solution;

import Instance.*;

/**
 * Created by n on 08/02/16.
 */
public class Solution {
    private Instance instance;
    private ScheduledJob first;
    private ScheduledJob second;

    public Instance getInstance() {
        return instance;
    }

    public ScheduledJob getFirst() {
        return first;
    }

    public ScheduledJob getSecond() {
        return second;
    }

    public Solution(Instance instance) {
        this.instance = instance;
        int i = instance.getNbM1();
        Mark mLast = new Mark(i, null);
        Mark m = mLast;
        while(i>0){
            i--;
        }
        //Mettre les marqueurs dans les deux ordos
    }

    public int getLastJobM(int machine) {
        return 0;
    }

    public void addJob(Job job, int stage, int machine) {
    }


    public void print() {
        //TODO
        System.out.println("M");
        //parcourir le premier ordo
        System.out.println("F");
        //parcourir le second ordo
    }

    /**
     * @return true if the solution does not violate the constrainsts
     */
    public boolean isValid() {
        //TODO
        return false;
    }

    public Solution clone() {
        //TODO
        return null;
    }

}
