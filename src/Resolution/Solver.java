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

    /**
     * Méthode à appeler depuis la classe Main. Initialise une solution puis lance la beam search, pour retourner la meilleure solution trouvée
     * @param beamValue Nombre de solution dans le beam
     */
    public void solve(int beamValue) {
        solution = firstSolution();
        //solution = beamSearch(beamValue);
    }

    /**
     *
     * @param beamValue
     * @return
     */
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
        s.set();
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
