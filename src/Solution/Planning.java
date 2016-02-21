package Solution;

import Instance.*;

/**
 * Created by n on 08/02/16.
 */
public class Planning {
    private Instance instance;
    public int[][][] planning;     //Planning[machine][lines of the jobs][number-beginTime-endTime]
    private int fin;
    private int lateTime;

    public Planning(Instance instance, Solution s) {
        this.instance = instance;
        this.planning = new int[s.getInstance().getNbM1() + s.getInstance().getNbM2()][s.getInstance().getNbJob()][3];
        fin = 0;

        ScheduledJob currentFirst = s.getFirst();
        ScheduledJob nextFirst = s.getFirst().getNext();
        int machine = 0;
        int time = 0;
        int line = 0;
        while (null != currentFirst) {         //Tant qu'on a pas fait chaque scheduledJob du premier étage, on continue à lire
            if (currentFirst.getNumber() < 0) {         //Si le job est un marqueur pour une machine
                machine = -currentFirst.getNumber();    //On change la machine actuelle
                line = 0;
                time = 0;
            } else {                                    //Si le scheduledJob est un Job
                planning[machine - 1][line][0] = currentFirst.getNumber();//On ajoute le job au planning, à la ligne suivante
                planning[machine - 1][line][1] = time;  //Date de lancement de productiondu job
                time += currentFirst.getQuantity();     //ajout processTime de currentFirst sur machine
                planning[machine - 1][line][2] = time;  //Date de fin de production du job
                if (null != nextFirst && nextFirst.getNumber() > 0) {
                    time += instance.getSetupTime(1, machine - 1) * instance.getSetUp(currentFirst.getProduct(), nextFirst.getProduct());
                    //ajout setUpTime entre currentFirst et nextFirst sur machine
                    if (time > fin) fin = time;
                    line++;
                }
            }
            currentFirst = nextFirst;
            if (null != nextFirst) nextFirst = nextFirst.getNext();
        }

        ScheduledJob currentSecond = s.getSecond();
        ScheduledJob nextSecond = s.getSecond().getNext();
        machine = 0;
        time = 0;
        line = 0;
        while (null != currentSecond) {
            if (currentSecond.getNumber() < 0) {
                machine = -currentSecond.getNumber();
                line = 0;
                time = 0;
            } else {
                planning[instance.getNbM1() + machine - 1][line][0] = currentSecond.getNumber();
                int release = this.readPlanning(currentSecond.getNumber(), 1, false);//Date à partir de laquelle le job est produit(fin) au premier étage
                if (time < release) time = release;//Vérification de la release à l'étage précédent
                planning[instance.getNbM1() + machine - 1][line][1] = time;
                time += currentSecond.getQuantity() * instance.getDuration(machine - 1, currentSecond.getType()); //ajout processTime de currentSecond sur machine
                planning[instance.getNbM1() + machine - 1][line][2] = time;
                int late = time - currentSecond.getDueDate();
                if (late < 0) late = 0;
                lateTime += late;
                if (null != nextSecond && nextSecond.getNumber() > 0)
                    time += instance.getSetupTime(2, machine - 1) * instance.getSetUp(currentSecond.getProduct(), nextSecond.getProduct()); //ajout setUpTime entre currentSecond et nextSecond sur machine -- !!! si nextSecond est null !!!
                if (time > fin) fin = time;
                line++;
            }
            currentSecond = nextSecond;
            if (null != nextSecond) nextSecond = nextSecond.getNext();
        }
    }

    public int readPlanning(int jobNumber, int stage, boolean start) {
        stage--;
        for (int indexMachine = stage * instance.getNbM1(); indexMachine < instance.getNbM1() + stage * instance.getNbM2(); indexMachine++) {
            int indexJob = 0;
            while (indexJob < planning[indexMachine].length && planning[indexMachine][indexJob][0] > 0) {
                if (jobNumber == planning[indexMachine][indexJob][0])
                    if (start) return planning[indexMachine][indexJob][1];
                    else return planning[indexMachine][indexJob][2];
                indexJob++;
            }
        }
        System.out.println("job " + jobNumber + " presents a problem");
        return 0;
    }

    public void print() {
        System.out.println("FIRST FLOOR");
        for (int machine = 1; machine <= instance.getNbM1(); machine++) {
            System.out.println("Machine " + machine);
            int i = 0;
            while (i < planning[machine - 1].length && planning[machine - 1][i][0] != 0) {
                System.out.println("" + planning[machine - 1][i][0] + " - " + planning[machine - 1][i][1] + " - " + planning[machine - 1][i][2]);
                i++;
            }
        }
        System.out.println("SECOND FLOOR");
        for (int machine = 1; machine <= instance.getNbM2(); machine++) {
            System.out.println("Machine " + machine);
            int i = 0;
            while (planning[instance.getNbM1() + machine - 1][i][0] != 0) {
                System.out.println("" + planning[instance.getNbM1() + machine - 1][i][0] + " - " + planning[instance.getNbM1() + machine - 1][i][1] + " - " + planning[instance.getNbM1() + machine - 1][i][2]);
                i++;
            }
        }
        System.out.println("Objective = " + this.objective() + " (end = " + this.fin + ", late = " + this.lateTime + ")");
    }

    /**
     * Vérifie la validité de la solution à partir du planning. vérifie notamment les stocks et les contraintes.
     * A voir après les procédures
     *
     * @return
     */
    public boolean isValid() {
        return checkStock() && allJobOk();
    }

    public int objective() {
        return instance.getCoutProd() * fin + instance.getCoutPenalite() * lateTime;
    }

    /**
     * @return true if all jobs are scheduled once at both floors
     */
    public boolean allJobOk() {
        boolean[] jobs1 = new boolean[instance.getNbJob()];
        for (int j = 0; j < jobs1.length; j++) jobs1[j] = false;

        for (int m = 0; m < instance.getNbM1(); m++) {
            int line = 0;
            while (line < planning[m].length && planning[m][line][0] > 0) {
                if (jobs1[planning[m][line][0] - 1]) return false;
                else jobs1[planning[m][line][0] - 1] = true;
                line++;
            }
        }

        boolean[] jobs2 = new boolean[instance.getNbJob()];
        for (int j = 0; j < jobs2.length; j++) jobs2[j] = false;

        for (int m = instance.getNbM1(); m < planning.length; m++) {
            int line = 0;
            while (line < planning[m].length && planning[m][line][0] > 0) {
                if (jobs2[planning[m][line][0] - 1]) return false;
                else jobs2[planning[m][line][0] - 1] = true;
                line++;
            }
        }

        for (int j = 0; j < jobs1.length; j++) if (!jobs1[j]) return false;
        for (int j = 0; j < jobs2.length; j++) if (!jobs2[j]) return false;
        return true;
    }

    public boolean[] needStock() {
        boolean[] needStock = new boolean[instance.getNbJob()];
        for (int m = 0; m < instance.getNbM1(); m++) {
            int line = 0;
            while (line < planning[m].length && planning[m][line][0] > 0) {
                if (planning[m][line][2] == readPlanning(planning[m][line][0], 2, true))
                    needStock[planning[m][line][0] - 1] = false;
                else needStock[planning[m][line][0] - 1] = true;
                line++;
            }
        }
        return needStock;
    }

    /**
     * @return true if no more than 3 jobs (with quantities lower than the capacities) are wainting between 1 machine at 2nd floor
     */
    public boolean checkStock() {
        boolean[] needStock = needStock();
        //Prepare stocks for every machine at second floor
        Stock[] stocks = new Stock[instance.getNbM2()];
        for (int s = 0; s < stocks.length; s++)
            stocks[s] = new Stock(instance, instance.getStockCapa(s));

        // cursors
        int[] indexMachine = new int[planning.length];
        for (int i = 0; i < indexMachine.length; i++)
            indexMachine[i] = 0;
        boolean end = false;
        // loop : go through "planning" by order of date, and checks the stocks every time : returns false if needed
        while (!end) {
            int machine = 0;
            int job = 0;
            int min = fin;
            for (int i = 0; i < indexMachine.length; i++)
                if (indexMachine[i] < planning[i].length && date(i, indexMachine[i]) <= min){
                        min = date(i, indexMachine[i]);
                        machine = i;
                        job = indexMachine[i];
                    }

            if (planning[machine][job][0] > 0 && needStock[planning[machine][job][0] - 1])
                if (!stockUpdate(stocks, machine, job)) return false;

            indexMachine[machine]++;
            end = true;
            for (int i = 0; i < indexMachine.length; i++)
                if (indexMachine[i] < planning[i].length && planning[i][indexMachine[i]][0] != 0)
                    end = false;
        }
        return true;
    }

    /**
     * Change the stocks according to the current job finishing at 1st floor or starting at 2nd floor
     *
     * @param stocks
     * @param machine
     * @param job
     */
    public boolean stockUpdate(Stock[] stocks, int machine, int job) {
        if (machine < instance.getNbM1()) {
            stocks[machine].add(instance.getJob(planning[machine][job][0] - 1).getQuantity());
            if (!stocks[machine].isValid()) return false;
        } else
            stocks[machine - instance.getNbM1()].remove(-instance.getJob(planning[machine][job][0] - 1).getQuantity());
        return true;
    }

    /**
     * @param machine
     * @param line
     * @return the date (release if first floor, start if second floor) the the job at line "line" on machine "machine"
     */
    public int date(int machine, int line) {
        if (planning[machine][line][0] == 0) return Integer.MAX_VALUE;
        if (machine < instance.getNbM1()) return planning[machine][line][2];
        else return planning[machine][line][1];
    }

    /**
     * @param machine
     * @return int[endTime][nbJob on the machine]
     */
    public int[] getEnd(int machine) {
        int[] ret = new int[2];
        int line = 0;
        while (line < planning[machine].length && planning[machine][line][0] != 0) line++;
        if(line == 0){
            ret[0] = 0;
            ret[0] = 0;
        }
        else {
            ret[0] = planning[machine][line - 1][2];
            ret[1] = line;
        }

        return ret;
    }
}
