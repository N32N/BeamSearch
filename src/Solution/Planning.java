package Solution;

import Instance.Instance;

/**
 * Created by n on 08/02/16.
 */
public class Planning {
    private Instance instance;
    private int[][][] planning;     //Planning[machine][lines of the jobs][number-beginTime-endTime]
    private boolean isStockValid;   //If the planning respects the stock constraints
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
                if (null != nextFirst && nextFirst.getNumber() > 0)
                    time += instance.getSetupTime(2, machine - 1) * instance.getSetUp(currentSecond.getNumber(), nextSecond.getNumber()); //ajout setUpTime entre currentSecond et nextSecond sur machine -- !!! si nextSecond est null !!!
                if (time > max) max = time;
                line++;
            }
            currentSecond = nextSecond;
            if (null != nextSecond) nextSecond = nextSecond.getNext();
        }
        this.fin = max;
        this.isStockValid = checkStock();
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

    public boolean isStockValid() {
        return isStockValid;
    }

    /**
     * Vérifie la validité de la solution à partir du planning. vérifie notamment les stocks et les contraintes.
     * @return
     */
    public boolean isValid(){
        return isStockValid;
    }
    public int objective() {
        //TODO
        return fin;
    }

    public boolean checkStock() {
        //Prepare stocks for every machine at second floor
        Stock[] stocks = new Stock[instance.getNbM2()];
        for(int s = 0; s<stocks.length; s++)
            stocks[s] = new Stock(instance, instance.getStockCapa(instance.getNbM1()+s));
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
                if (date(i, indexMachine[i]) <= min) {
                    min = date(i, indexMachine[i]);
                    machine = i;
                    job = indexMachine[i];
                }

            if (machine < instance.getNbM1()){
                stocks[machine].add(instance.getJob(planning[machine][job][0]).getQuantity(), instance.getJob(planning[machine][job][0]).getType());
                if(!stocks[machine].isValid()) return false;
            }
            else stocks[machine - instance.getNbM1()].add(-instance.getJob(planning[machine][job][0]).getQuantity(), instance.getJob(planning[machine][job][0]).getType());

            indexMachine[machine]++;
            end = true;
            for (int i = 0; i < indexMachine.length; i++)
                if (indexMachine[i] < planning[i].length && planning[i][indexMachine[i]][0] != 0)
                    end = false;
        }
        return true;
    }

    public int date(int machine, int line) {
        if (machine < instance.getNbM1()) return planning[machine][line][2];
        else return planning[machine][line][1];
    }
}
