package Resolution;

import Instance.*;
import Solution.*;

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

    /**
     * M�thode � appeler depuis la classe Main. Initialise une solution puis lance la beam search, pour retourner la meilleure solution trouv�e
     * @param beamValue Nombre de solution dans le beam
     */
    public void solve(int beamValue) {
        solution = firstSolution();
        solution = beamSearch(beamValue);
    }

    /**
     * @param beamValue
     * @return
     */
    public Solution beamSearch(int beamValue) {
        Beam beam = new Beam(beamValue, solution);
        beam.procedure("random", 5);
        beam.procedure("neh", 32);
        beam.procedure("bmp", 32);
       /* beam.procedure("random", 5);
        beam.procedure("neh", 32);
        beam.procedure("bmp", 32);
        beam.procedure("random", 5);
        beam.procedure("neh", 32);
        beam.procedure("bmp", 32);
        beam.procedure("random", 5);
        beam.procedure("neh", 32);
        beam.procedure("bmp", 32);
        beam.procedure("random", 50);
        beam.procedure("neh", 320);
        beam.procedure("bmp", 320);*/

        return beam.bestSolution();
    }

    /**
     * firstSolution generates a basic good solution based on EDD
     * @return Solution firstSolution
     */
    public Solution firstSolution() {
        Solution s = new Solution(this.instance);
        Job[] jobs = this.instance.getJobs();
        int[] indexByDd = EDD(jobs);

        this.scheduleFirst(s, jobs, indexByDd); //scheduleFirst stage 1
        this.scheduleSecond(s, jobs, indexByDd); //scheduleFirst stage 2
        s.set();
        return s;
    }

    public int[] EDD (Job[] jobs){
        int[] dd = new int[jobs.length];
        for (int j = 0; j < dd.length; j++) dd[j] = jobs[j].getDueDate();
        int[] indexByDd = new int[dd.length];
        for (int j = 0; j < jobs.length; j++){
            indexByDd[j] = getMinIndex(dd);
            dd[indexByDd[j]] = Integer.MAX_VALUE;
        }
        return indexByDd;
    }

    public void scheduleFirst(Solution s, Job[] jobs, int[] indexByDd) {
        int[] dispo = new int[instance.getNbM1()];
        for (int j = 0; j < jobs.length; j++) {
            int machineAvailable = getMinIndex(dispo);
            s.addJob(jobs[indexByDd[j]], 1, machineAvailable + 1);
            dispo[machineAvailable] += jobs[indexByDd[j]].getQuantity();
        }
    }

    public void scheduleSecond(Solution s, Job[] jobs, int[] indexByDd) {
        Planning p = new Planning(instance, s);
        int[] releases = new int[jobs.length];
        for(int j = 0; j < releases.length; j++)
            releases[j] = p.readPlanning(j+1, 1, false);
        int[] dispo = new int[instance.getNbM2()];
        for (int j = 0; j < jobs.length; j++) {
            int machineAvailable = getIndexBestMachine(releases, dispo, jobs[indexByDd[j]]);
            s.addJob(jobs[indexByDd[j]], 2, machineAvailable + 1);
        }
    }

    public  int getIndexBestMachine(int[] releases, int[] dispo, Job j){
        int[] ends = new int[dispo.length];
        for(int c = 0; c < ends.length; c++){
            if(releases[j.getNumber() - 1] < dispo[c])  ends[c] = dispo[c];
            else                                        ends[c] = releases[j.getNumber() - 1];
            ends[c] += (int)(j.getQuantity() * instance.getDuration(c, j.getType()));
        }
        int index = getMinIndex(ends);
        dispo[index] = ends[index];
        return index;
    }

    /**
     * @param tab
     * @return the index of the minimum value in tab
     */
    public int getMinIndex(int[] tab) {
        int minIndex = 0;
        for (int i = 1; i < tab.length; i++) {
            if (tab[i] < tab[minIndex]) minIndex = i;
        }
        return minIndex;
    }
}
