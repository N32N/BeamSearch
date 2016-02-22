package Solution;

import Instance.*;

import java.util.ArrayList;

/**
 * Stores a solution as two job sequences (one by stage).
 * If the first sequence is / -1 / 1 / 2 / 3 / -2 / 4 / 5, it means : Machine 1 : 1, 2, 3 ; Machine 2 : 4, 5.
 * Created by n on 08/02/16.
 */
public class Solution {
    private Instance instance;
    private ScheduledJob first;         // First floor's sequence
    private ScheduledJob second;        // Second floor sequence
    private long cost;
    private boolean isValid;
    private int[][] machineUse;        //[nbMachines][3] : first job start / last job end / number oj jobs

    /**
     * Creates a planning from the solution, and sets Solution's ( Cost, Validity, and MachineUse )
     * Always to be called when a solution has been created / modified
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

    /**
     * @param stage
     * @param machine
     * @return the time when the "machine" has finished working at stage "stage"
     */
    public int getEndUse(int stage, int machine) {
        return machineUse[machine - 1 + (stage - 1) * instance.getNbM1()][1];
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

    public ScheduledJob getLastJob(int machine, int stage){
        if(stage == 1) return getLastJobFirstFloor(machine);
        else return getLastJobSecondFloor(machine);
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
        ScheduledJob last;
        if (stage == 1)  last = getLastJobFirstFloor(machine);
        else             last = getLastJobSecondFloor(machine);
        job.setNext(last.getNext());
        last.setNext(job);
    }

    public void addJob(Job job, int stage, int machine) {
        ScheduledJob last;
        if (stage == 1)  last = getLastJobFirstFloor(machine);
        else             last = getLastJobSecondFloor(machine);
        last.setNext(new ScheduledJob(job, last.getNext()));
    }

    /**
     * @param stage
     * @return the machine which end time is the biggest
     */
    public int getBusiestMachine(int stage) {
        int nbM;
        if (stage == 1) nbM = instance.getNbM1();
        else nbM = instance.getNbM2();
        int busiest = 0;
        int end = 0;
        for (int machine = 1; machine <= nbM; machine++) {
            int e = getEndUse(stage, machine);
            if (e >= end) {
                end = e;
                busiest = machine;
            }
        }
        return busiest;
    }

    public void print() {
        this.printShort();
        for(int m = 0; m < machineUse.length; m++)
            System.out.println("- Machine "+m+" : "+machineUse[m][0]+" - "+machineUse[m][1]+" ("+machineUse[m][2]+" jobs)");
    }

    public void printShort() {
        System.out.println("--Solution--cost : " + getCost() + "--" + isValid() + "--");
        System.out.print("M ");
        ScheduledJob current = first;
        while (current != null) {
            if(current.getNumber() > 0)     System.out.print(" " + current.getNumber());
            else                            System.out.print("  /" + -current.getNumber() + " :");
            current = current.getNext();
        }
        System.out.println("");
        System.out.print("F ");
        ScheduledJob current2 = second;
        while (current2 != null) {
            if(current2.getNumber() > 0)    System.out.print(" " + current2.getNumber());
            else                            System.out.print("  /" + -current2.getNumber() + " :");
            current2 = current2.getNext();
        }
        System.out.println("");
    }

    public void printPlanning() {
        Planning p = new Planning(instance, this);
        p.print();
    }

    public Solution clone() {
        Solution clone = new Solution(instance);
        ScheduledJob current = this.first;
        ScheduledJob newSol = new ScheduledJob(this.first);
        clone.first = newSol;
        while (current.getNext() != null) {
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

        clone.machineUse = new int[machineUse.length][3];
        for (int m = 0; m < machineUse.length; m++) {
            clone.machineUse[m][0] = machineUse[m][0];
            clone.machineUse[m][1] = machineUse[m][1];
            clone.machineUse[m][2] = machineUse[m][2];
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

    public ScheduledJob[] getScheduledJobAndPrevious(int jobNumber, int stage) {
        ScheduledJob[] jobs = new ScheduledJob[2];
        ScheduledJob previous = null;
        ScheduledJob current;
        if (stage == 1) current = this.first;
        else            current = this.second;
        while (current.getNumber() != jobNumber) {
            previous = current;
            current = current.getNext();
        }
        jobs[0] = previous;
        jobs[1] = current;
        return jobs;
    }

    public boolean isNotIn(ArrayList<Solution> allSolution){
        for(Solution sol:allSolution)
            if(equals(sol)) return false;
        return true;
    }

    public boolean equals (Solution sol){
        if(this.cost != sol.getCost()) return false;
        if(this.isValid != sol.isValid()) return false;
        for(int k=0; k<this.machineUse.length;k++)
                for(int l=0; l<this.machineUse[0].length; l++)
                    if(this.machineUse[k][l] != sol.machineUse[k][l])
                        return false;
        return true;
    }

    public ScheduledJob remove(ScheduledJob previousJob,ScheduledJob job){
        previousJob.setNext(job.getNext());
        job.setNext(null);
        return job;
    }

    public ScheduledJob removeJob(int job, int stage){
        ScheduledJob[] jobs = getScheduledJobAndPrevious(job, stage);
        return remove(jobs[0], jobs[1]);
    }

    public void insertAfter(ScheduledJob job, ScheduledJob jobToInsert){
        ScheduledJob previousNext = job.getNext();
        jobToInsert.setNext(previousNext);
        job.setNext(jobToInsert);
    }

    /**
     * @param stage
     * @return the 2 first scheduled jobs of the solution at stage "stage"
     */
    public ScheduledJob[] getFirstScheduledJobs(int stage){
        ScheduledJob[] jobs = new ScheduledJob[2];
        if(stage ==1 ){
            jobs[0] = this.first;
            jobs[1] = this.first.getNext();
        }
        else {
            jobs[0] = this.second;
            jobs[1] = this.second.getNext();
        }
        return jobs;
    }
}