package Solution;

/**
 * Created by n on 08/02/16.
 */
public class Mark extends ScheduledJob {
    private int machine;

    public Mark(int machine, ScheduledJob next) {
        super(-machine, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, next);
        this.machine = machine;
    }
}