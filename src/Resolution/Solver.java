package Resolution;

import Instance.*;
import Solution.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by n on 08/02/16.
 */
public class Solver {
    private Instance instance;
    private Solution solution;

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public Solution getSolution() {
        return this.solution;
    }

    public Instance getInstance() {
        return instance;
    }

    public Solver(Instance i) {
        instance = i;
        solution = new Solution(i);
    }

    public void solve(int beamValue) {
        solution = firstSolution();
        solution = beamSearch(beamValue);
    }

    public Solution beamSearch(int beamValue) {
        Beam beam = new Beam(beamValue, solution);
        beam.procedure("random", 5);
        //process : mix of procedures

        return beam.bestSolution();
    }


    /**
     * firstSolution uses "first", "second", "getMinIndex" to generate a first solution
     *
     * @return Solution firstSolution
     */
    public Solution firstSolution() {
        Solution s = new Solution(this.instance);
        Job[] jobs = this.instance.getJobs();
        this.EDD(s, jobs, 1); //EDD stage 1
        this.EDD(s, jobs, 2); //EDD stage 2
        return s;
    }
    public void EDD(Solution s, Job[] jobs, int stage){
        int[] dd = new int[jobs.length];
        for (int j = 0; j < dd.length; j++) dd[j] = jobs[j].getDueDate();

        int[] dispo = null;
        if(stage == 1 ) dispo = new int[instance.getNbM1()];
        else if (stage == 2 ) dispo = new int[instance.getNbM2()];

        for (int j = 0; j < jobs.length; j++) {
            int index = getMinIndex(dd);
            int machineAvailable = getMinIndex(dispo)+1;

            s.addJob(jobs[index], stage, machineAvailable);
            dd[index] = Integer.MAX_VALUE;
            dispo[machineAvailable-1]+= jobs[index].getQuantity();
        }
    }

    /**
     * Place (job) on the first free machine in the first stage of (s)
     *
     * @param s
     * @param job
     */
    private void place(Solution s, Job job, int stage) {

    }
    /**
     * Place (job) when it is released, on a free machine
     *
     * @param s
     * @param job
     */
    private void placeAtNextRelease(Solution s, Job job) {
        //TODO
    }

    /**
     * Place the job (job) on the earliest free machine (in s, second stage) after its release, and return true
     * if the release date is after every machine is free, return false
     *
     * @param s
     * @param job
     * @return
     */
    private boolean placeWithRelease(Solution s, Job job) {
        //TODO
        return false;
    }

    /**
     * @param tab
     * @return the index of the minimum value in tab
     */
    public int getMinIndex(int[] tab) {
        int minIndex=0;
        for(int i=1; i<tab.length;i++){
            if(tab[i]<tab[minIndex]) minIndex =i;
        }
        return minIndex;
    }
}
