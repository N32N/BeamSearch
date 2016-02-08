package Solution;

import Instance.Instance;

/**
 * Created by n on 08/02/16.
 */
public class Planning {
    private Instance instance;
    private int[][][] planning;     //Planning[machine][lines of the jobs][number-beginTime-endTime]
    private int fin;

    public Planning(Instance instance, Solution s) {
        this.instance=instance;
        this.planning = new int[s.getInstance().getNbM1()+s.getInstance().getNbM2()][s.getInstance().getNbJob()][3];
        int max = 0;

        ScheduledJob current = s.getFirst();
        ScheduledJob next = s.getFirst().getNext();
        int machine=0;
        int time=0;
        int line=0;
        while(current!=null){
            if(current.getNumber()<0){
                machine = -current.getNumber();
                line=0;
                time=0;
            }
            else{
                planning[machine][line][0]=current.getNumber();
                planning[machine][line][1]=time;
                time+=0; //ajout processTime de current sur machine
                planning[machine][line][2]=time;
                time+=0; //ajout setUpTime entre current et next sur machine -- !!! si next est null !!!
                if(time>max) max=time;
                line++;
            }
            current=next;
            next=next.getNext();
        }
    this.fin=max;
    }

    public void print() {
        System.out.println("FIRST FLOOR");
        for(int machine = 1; machine<=instance.getNbM1(); machine++){
            System.out.println("Machine "+machine);
        }
    }

    public int objective() {
        //TODO
        return 0;
    }
}
