package Solution;

import Instance.Instance;

/**
 * Created by n on 08/02/16.
 */
public class Planning {
    private Instance instance;
    private int[][][] planning;     //Planning[machine][lines of the jobs][number-beginTime-endTime]
    private int fin;

    // reste cinq lignes à écrire
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
                time += 0; //ajout processTime de currentFirst sur machine
                planning[machine - 1][line][2] = time;
                time += 0; //ajout setUpTime entre currentFirst et nextFirst sur machine -- !!! si nextFirst est null !!!
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
                //Vérification de la release à l'étage précédent
                planning[instance.getNbM1() + machine - 1][line][1] = time;
                time += 0; //ajout processTime de currentSecond sur machine
                planning[instance.getNbM1() + machine - 1][line][2] = time;
                time += 0; //ajout setUpTime entre currentSecond et nextSecond sur machine -- !!! si nextSecond est null !!!
                if (time > max) max = time;
                line++;
            }
            currentSecond = nextSecond;
            if (null != nextSecond) nextSecond = nextSecond.getNext();
        }
        this.fin = max;
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
}
