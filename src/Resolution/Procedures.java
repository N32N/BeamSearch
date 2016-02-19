package Resolution;

import Instance.Job;
import Solution.*;

import java.util.ArrayList;

/**
 * Created by n on 08/02/16.
 * Ne retourne que des solutions valides.
 */
public final class Procedures {

    public static Solution[] random(Solution mere) {
        int nbSwaps =  (mere.getInstance().getNbJob() / 10) + 1;
        Solution[] solutions = new Solution[nbSwaps];
        int i=0;
        while(i< nbSwaps){
            Solution solution = mere.clone();
            solution.swapTwo((int) (Math.random() * mere.getInstance().getNbJob()) + 1, (int) (Math.random() * mere.getInstance().getNbJob()) + 1);
            solution.set();
            if(solution.isValid()){
                solutions[i] = solution;
                i++;
            }
        }
        return solutions;
    }

    public static Solution[] randomSwitch(Solution mere) {
        int nbJobsToSwitch = 2 * ((int) (mere.getInstance().getNbJob() / 10) + 1);//Nombre pair de job, >= 2
        int[] listJobs = new int[nbJobsToSwitch];
        for (int i = 0; i < nbJobsToSwitch / 2; i++) {
            listJobs[i] = (int) (Math.random() * mere.getInstance().getNbJob()) + 1;
            int secondJob = listJobs[i];
            while (secondJob == listJobs[i]) {
                secondJob = (int) (Math.random() * mere.getInstance().getNbJob()) + 1;
            }
            listJobs[i + 1] = secondJob;
        }
        //On a une liste pair de job tous différents
        int nbSolutions = nbJobsToSwitch*(nbJobsToSwitch-1)/2;
        for (int i = 0; i < nbJobsToSwitch-1; i++) {
            for(int j=i+1; j < nbJobsToSwitch;j++){

            }
        }
        return new Solution[1];
    }


    /**
     * Selectionne un job arbitrairement, le place à tout emplacement possible dans la solution
     *
     * @param mere
     * @return
     */
    public static Solution[] neh(Solution mere) {
        return null;
    }

    /**
     * Selectionne un job dans une des machines les plus utilisée. Le place sur une des autres machine
     * Si 1 seule machine au niveau 1, ne s'éxécute que pour le niveau 2
     *
     * @param mere
     * @return
     */
    public static Solution[] bmp(Solution mere) {
        Solution sol = mere.clone();

        return null;
    }
}
