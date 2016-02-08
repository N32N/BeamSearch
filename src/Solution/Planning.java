package Solution;

/**
 * Created by n on 08/02/16.
 */
public class Planning {
    private int[][] planning;   //planning[i][.] = line of job i = [which machine at FIRST floor][date][which machine at SECOND floor][date]
    private int fin;

    public Planning(Solution s) {
        this.planning = new int[s.getInstance().getNbJob()][4];

        //Parcourir les deux ordos de s, remplir planning


        // Max => this.fin
    }

    public void print() {
        //TODO
    }

    public int objective() {
        //TODO
        return 0;
    }
}
