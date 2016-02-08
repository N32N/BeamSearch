package Resolution;

import Instance.*;
import Solution.Solution;

import java.util.ArrayList;

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
        this.first(s, jobs); //EDD
        this.second(s, jobs); //EDD avec releases
        return s;
    }

    public void first(Solution s, Job[] jobs) {
        int[] dd = new int[jobs.length];
        for (int j = 0; j < dd.length; j++) dd[j] = jobs[j].getDueDate();
        int[] earliestFree = new int[instance.getNbM1() + instance.getNbM2()];
        for (int j = 0; j < earliestFree.length; j++) earliestFree[j] = 0;
        for (int j = 0; j < jobs.length; j++) {
            int index = getMinIndex(dd);
            this.place(s, jobs[index]);
            dd[index] = Integer.MAX_VALUE;
        }
    }

    /**
     * Place (job) on the first free machine in the first stage of (s)
     *
     * @param s
     * @param job
     */
    private void place(Solution s, Job job) {
        //TODO
    }

    public void second(Solution s, Job[] jobs) {
        int[] dd = new int[jobs.length];
        for (int j = 0; j < jobs.length; j++) dd[j] = jobs[j].getDueDate();
        ArrayList<Job> l = new ArrayList<Job>();
        for (int j = 0; j < jobs.length; j++) {
            int indice = getMinIndex(dd);
            l.add(jobs[indice]);
            dd[indice] = Integer.MAX_VALUE;
        }
        while (!l.isEmpty()) {
            boolean isPlanned = false;
            int curseur = 0;
            while (!isPlanned && curseur < l.size()) {
                if (this.placeWithRelease(s, l.get(curseur))) {
                    isPlanned = true;
                    l.remove(curseur);
                } else curseur++;
            }
            if (curseur == l.size()) {
                curseur = 1;
                this.placeAtNextRelease(s, l.get(curseur));
                l.remove(curseur);
            }
        }
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
        //TODO
        return 10000;
    }
}
