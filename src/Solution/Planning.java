package Solution;

import Instance.Instance;

/**
 * Created by n on 08/02/16.
 */
public class Planning {
    private Instance instance;
    private int[][][] planning;     //Planning[machine][lines of the jobs][number-beginTime-endTime]
    private boolean isStockValiv;   //If the planning respects the stock constraints
    private int fin;

    public Planning(Instance instance, Solution s) {
        this.instance = instance;
        this.planning = new int[s.getInstance().getNbM1() + s.getInstance().getNbM2()][s.getInstance().getNbJob()][3];
        int max = 0;

        ScheduledJob currentFirst = s.getFirst();
        ScheduledJob nextFirst = s.getFirst().getNext();
        int machine = 0;
        int time = 0;
        int line = 0;
        while (null != currentFirst) {
            if (currentFirst.getNumber() < 0) {
                machine = -currentFirst.getNumber();
                line = 0;
                time = 0;
            } else {
                planning[machine - 1][line][0] = currentFirst.getNumber();
                planning[machine - 1][line][1] = time;
                time += currentFirst.getQuantity(); //ajout processTime de currentFirst sur machine
                planning[machine - 1][line][2] = time;
                if (null != nextFirst)
                    time += instance.getSetupTime(1, machine - 1) * instance.getSetUp(currentFirst.getNumber(), nextFirst.getNumber()); //ajout setUpTime entre currentFirst et nextFirst sur machine
                if (time > max) max = time;
                line++;
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
                int release = this.getRelease(currentSecond.getNumber());
                if (time < release) time = release;//Vérification de la release à l'étage précédent
                planning[instance.getNbM1() + machine - 1][line][1] = time;
                time += currentSecond.getQuantity() * instance.getDuration(machine - 1, currentSecond.getType()); //ajout processTime de currentSecond sur machine
                planning[instance.getNbM1() + machine - 1][line][2] = time;
                if (null != nextFirst && nextFirst.getNumber()>0)
                    time += instance.getSetupTime(2, machine - 1) * instance.getSetUp(currentSecond.getNumber(), nextSecond.getNumber()); //ajout setUpTime entre currentSecond et nextSecond sur machine -- !!! si nextSecond est null !!!
                if (time > max) max = time;
                line++;
            }
            currentSecond = nextSecond;
            if (null != nextSecond) nextSecond = nextSecond.getNext();
        }
        this.fin = max;
        this.isStockValiv = checkStock(planning);
    }

    public int getRelease(int jobNumber) {
        for (int indexMachine = 0; indexMachine < instance.getNbM1(); indexMachine++) {
            int indexJob = 0;
            while (indexJob < planning[indexMachine].length && planning[indexMachine][indexJob][0] != 0) {
                if (jobNumber == planning[indexMachine][indexJob][0]) return planning[indexMachine][indexJob][2];
                indexJob++;
            }
        }
        System.out.println("job " + jobNumber + " is not scheduled at first floor but at second");
        return 0;
    }

    public void print() {
        System.out.println("FIRST FLOOR");
        for (int machine = 1; machine <= instance.getNbM1(); machine++) {
            System.out.println("Machine " + machine);
            int i = 0;
            while (planning[machine - 1][i][0] != 0) {
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
    }

    public int objective() {
        //TODO
        return 0;
    }

    public static boolean checkStock(int[][][] planning) {
        return true;
    }
}
