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
        int nbSwaps = (mere.getInstance().getNbJob() / 10) + 1;
        Solution[] solutions = new Solution[nbSwaps];
        int i = 0;
        while (i < nbSwaps) {
            Solution solution = mere.clone();
            solution.swapTwo((int) (Math.random() * mere.getInstance().getNbJob()) + 1, (int) (Math.random() * mere.getInstance().getNbJob()) + 1);
            solution.set();
            if (solution.isValid()) {
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
        //On a une liste pair de job tous diff?rents
        int nbSolutions = nbJobsToSwitch * (nbJobsToSwitch - 1) / 2;
        for (int i = 0; i < nbJobsToSwitch - 1; i++) {
            for (int j = i + 1; j < nbJobsToSwitch; j++) {

            }
        }
        return new Solution[1];
    }


    /**
     * @param mere
     * @return
     */
    public static Solution[] neh(Solution mere) {
        Solution[] stage1 = neh(mere, 1);
        Solution[] stage2 = neh(mere, 2);

        //TODO

        Solution[] all = new Solution[stage1.length+stage2.length];
        for(int i=0; i<stage1.length; i++){
            all[i] = stage1[i];
        }
        for (int i=0; i<stage2.length; i++){
            all[stage1.length + i] = stage2[i];
        }
        return all;
    }

    /**
     * Prend chaque job, le place à tout les emplacements sur l'ordonnancement avant sa position actuelle (autres machines notamment)
     * Commence au deuxième job de l'ordo et fini au dernier job
     *
     * @param mere
     * @param stage
     * @return
     */
    public static Solution[] neh(Solution mere, int stage) {
        ArrayList<Solution> solutions = new ArrayList<>();
        ScheduledJob currentJob;
        if (stage == 1)     currentJob = mere.getFirst();
        else                currentJob = mere.getSecond();

        while (currentJob != null) {
            if (currentJob.getNumber() > 0) {//tout les jobs
                Solution fille = mere.clone();
                ScheduledJob placeHere = fille.getFirstScheduledJobs(stage)[0];
                int previousJobNumber = fille.getScheduledJobAndPrevious(currentJob.getNumber(), stage)[0].getNumber();
                ScheduledJob job = fille.removeJob(currentJob.getNumber(), stage);
                while (placeHere.getNumber() != previousJobNumber) {
                    fille.insertAfter(placeHere,job);
                    Solution s = fille.clone();
                    s.set();
                    if (s.isValid()) {
                        solutions.add(s);
                    }
                    fille.remove(placeHere,job);
                    placeHere = placeHere.getNext();
                }
            }
            currentJob = currentJob.getNext();
        }
        return tab(solutions);
    }

    /**
     * Selectionne un job dans une des machines les plus utilis?e. Le place sur une des autres machine
     * Si 1 seule machine au niveau 1, ne s'?x?cute que pour le niveau 2
     *
     * @param mere
     * @return
     */
    public static Solution[] bmp(Solution mere) {
        return bmp(mere, 2);
    }

    public static Solution[] bmp(Solution mere, int stage) {
        ArrayList<Solution> list = new ArrayList<>();
        int busiest = mere.getBusiestMachine(stage);
        ScheduledJob p, c;
        if (busiest == 1) {
            if (stage == 1) p = mere.getFirst();
            else p = mere.getSecond();
        } else {
            p = mere.getLastJob(busiest-1, stage).getNext();
        }
        c = p.getNext();
        while (null != c && c.getNumber() > 0) {
            p.setNext(c.getNext());
            Solution noC = mere.clone();
            p.setNext(c);
            tryJobEveryWhere(new Job(c.getNumber(), c.getProduct(), c.getType(), c.getQuantity(), c.getDueDate()), noC, busiest, stage, list);
            p = p.getNext();
            c = c.getNext();
        }
        return tab(list);
    }

    /**
     * Add every valid solution created in result
     * creates solutions by puting "j" at every possible spot in the stage "stage" of "missOneJob" except on machine "busiest"
     *
     * @param j
     * @param missOneJob
     * @param busiest
     * @param stage
     * @param result
     */
    public static void tryJobEveryWhere(Job j, Solution missOneJob, int busiest, int stage, ArrayList<Solution> result) {
        boolean active = true;
        ScheduledJob current;
        if (stage == 1) current = missOneJob.getFirst();
        else current = missOneJob.getSecond();
        while (null != current) {
            if (current.getNumber() < 0) active = true;
            if (current.getNumber() == -busiest) active = false;
            if (active) {
                Solution fille = missOneJob.clone();
                ScheduledJob[] pair;
                if (null != current.getNext()) {
                    pair = fille.getScheduledJobAndPrevious(current.getNext().getNumber(), stage);
                    pair[0].setNext(new ScheduledJob(j, pair[1]));
                } else {
                    pair = fille.getScheduledJobAndPrevious(current.getNumber(), stage);
                    pair[1].setNext(new ScheduledJob(j, null));
                }
                fille.set();
                if (fille.isValid()) result.add(fille);
            }
            current = current.getNext();
        }
    }

    public static Solution[] tab(ArrayList<Solution> list) {
        int size = list.size();
        Solution[] tab = new Solution[size];
        int i = 0;
        for (Solution s : list) {
            tab[i] = s;
            i++;
        }
        return tab;
    }
}
