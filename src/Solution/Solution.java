package Solution;

import Instance.*;

/**
 * Created by n on 08/02/16.
 */
public class Solution {
    private Instance instance;
    private ScheduledJob first;
    private ScheduledJob second;
    private long cost;
    private boolean isValid;

    public long getCost() {
        return cost;
    }

    public boolean isValid() {
        return this.isValid;
    }

    /**
     * Cr�e un planning � partir de la solution actuelle, afin de d�terminer la validit� de la solution et son co�t.
     */
    public void set() {
        Planning planning = new Planning(instance, this);
        this.isValid = planning.isValid();
        if (this.isValid) this.cost = planning.objective();
    }

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
        Mark m = new Mark(i, null);
        while (i > 1) {
            i--;
            m = new Mark(i, m);
        }
        this.first = m;

        int i2 = instance.getNbM2();
        Mark m2 = new Mark(i2, null);
        while (i2 > 1) {
            i2--;
            m2 = new Mark(i2, m2);
        }
        this.second = m2;

        this.isValid = false;
        this.cost = Integer.MAX_VALUE;
    }

    public ScheduledJob getLastJobFirstFloor(int machine) {
        ScheduledJob current = first;
        if (machine != getInstance().getNbM1())
            while (current.getNext().getNumber() != -(machine + 1)) current = current.getNext();
        else while (null != current.getNext()) current = current.getNext();
        return current;
    }

    public ScheduledJob getLastJobSecondFloor(int machine) {
        ScheduledJob current = second;
        if (machine != getInstance().getNbM2())
            while (current.getNext().getNumber() != -(machine + 1)) current = current.getNext();
        else while (null != current.getNext()) current = current.getNext();
        return current;
    }

    public void addJob(ScheduledJob job, int stage, int machine) {
        if (stage == 1) {
            ScheduledJob last = getLastJobFirstFloor(machine);
            job.setNext(last.getNext());
            last.setNext(job);
        }
        if (stage == 2) {
            ScheduledJob last = getLastJobSecondFloor(machine);
            job.setNext(last.getNext());
            last.setNext(job);
        }
    }

    public void addJob(Job job, int stage, int machine) {
        if (stage == 1) {
            ScheduledJob last = getLastJobFirstFloor(machine);
            last.setNext(new ScheduledJob(job, last.getNext()));
        }
        if (stage == 2) {
            ScheduledJob last = getLastJobSecondFloor(machine);
            last.setNext(new ScheduledJob(job, last.getNext()));
        }
    }

    public void print() {
        System.out.println("_____M_____");
        ScheduledJob current = first;
        while (current != null) {
            System.out.println("number " + current.getNumber());
            current = current.getNext();
        }
        System.out.println("_____F_____");
        ScheduledJob current2 = second;
        while (current2 != null) {
            System.out.println("number " + current2.getNumber());
            current2 = current2.getNext();
        }
    }

    public void printPlanning() {
        Planning p = new Planning(instance, this);
        p.print();
    }

    public Solution clone() {   //Create new ScheduledJobs (based on same jobs) and chains them
        Solution clone = new Solution(instance);
        ScheduledJob current = this.first;
        ScheduledJob newSol = new ScheduledJob(this.first);
        clone.first = newSol;
        while (current.getNext() != null) {//stage 1
            newSol.setNext(new ScheduledJob(current.getNext()));
            newSol = newSol.getNext();
            current = current.getNext();
        }

        ScheduledJob currentSecond = this.second;
        ScheduledJob newSolSecond = new ScheduledJob(this.second);
        clone.second = newSolSecond;
        while (currentSecond.getNext() != null) {
            newSolSecond.setNext(new ScheduledJob(currentSecond.getNext()));
            newSolSecond = newSolSecond.getNext();
            currentSecond = currentSecond.getNext();
        }
        return clone;
    }
}
