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
    private int[][] machineUse;       //[nbMachines][3] : first job start / last job end / number oj jobs

    /**
     * Cr�e un planning � partir de la solution actuelle, afin de d�terminer la validit� de la solution et son co�t.
     */
    public void set() {
        Planning planning = new Planning(instance, this);
        this.isValid = planning.isValid();
        if (this.isValid) {
            this.cost = planning.objective();
            for (int m = 0; m < machineUse.length; m++) {
                machineUse[m][0] = planning.planning[m][0][1];
                machineUse[m][1] = planning.getEnd(m)[0];
                machineUse[m][2] = planning.getEnd(m)[1];
            }
        }
    }

    public void setCost(long c) {
        this.cost = c;
    }

    public long getCost() {
        return cost;
    }

    public boolean isValid() {
        return this.isValid;
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

    public int getStartUse(int stage, int machine) {
        return machineUse[machine - 1 + (stage - 1) * instance.getNbM1()][0];
    }

    public int getEndUse(int stage, int machine) {
        return machineUse[machine - 1 + (stage - 1) * instance.getNbM1()][1];
    }

    public int getNbJob(int stage, int machine) {
        return machineUse[machine - 1 + (stage - 1) * instance.getNbM1()][2];
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
        this.machineUse = new int[instance.getNbM1() + instance.getNbM2()][3];
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

    /**
     * Retourne la machine finissant de produire le plus tôt.
     *
     * @param stage
     * @return
     */
    public int getBusiestMachine(int stage) {
        int nbM;
        if (stage == 1) nbM = instance.getNbM1();
        else nbM = instance.getNbM2();
        int busiest = 0;
        int end = 0;
        for (int machine = 1; machine <= nbM; machine++) {
            int e = getEndUse(stage, machine);
            if (e > end) {
                end = e;
                busiest = machine;
            }
        }
        return busiest;
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

    public void printShort() {
        System.out.println("--Solution--cost : " + getCost() + "--" + isValid() + "--");
        System.out.print("M");
        ScheduledJob current = first;
        while (current != null) {
            System.out.print("-" + current.getNumber());
            current = current.getNext();
        }
        System.out.println("");
        System.out.print("F");
        ScheduledJob current2 = second;
        while (current2 != null) {
            System.out.print("-" + current2.getNumber());
            current2 = current2.getNext();
        }
        System.out.println("");
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

        clone.cost = cost;
        clone.isValid = isValid;
        return clone;
    }

    public void swapTwo(int job1, int job2) {
        swapTwo(job1, job2, 1);
        swapTwo(job1, job2, 2);
    }

    public void swapTwo(int job1, int job2, int stage) {
        ScheduledJob[] jobs1 = getScheduledJobAndPrevious(job1, stage);
        ScheduledJob[] jobs2 = getScheduledJobAndPrevious(job2, stage);
        jobs1[0].setNext(jobs2[1]);
        jobs2[0].setNext(jobs1[1]);
        ScheduledJob s = jobs1[1].getNext();
        jobs1[1].setNext(jobs2[1].getNext());
        jobs2[1].setNext(s);
    }

    public ScheduledJob[] getScheduledJobAndPrevious(int job, int stage) {
        ScheduledJob[] jobs = new ScheduledJob[2];
        ScheduledJob previous = null;
        ScheduledJob current;
        if (stage == 1) current = this.first;
        else current = this.second;
        while (current.getNumber() != job) {
            previous = current;
            current = current.getNext();
        }
        jobs[0] = previous;
        jobs[1] = current;
        return jobs;
    }
}