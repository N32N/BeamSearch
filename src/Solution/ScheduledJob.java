package Solution;

import Instance.Job;

/**
 * Created by n on 08/02/16.
 */
public class ScheduledJob extends Job {
    private ScheduledJob next;

    public ScheduledJob(int number, int p, int t, int q, int d) {
        super(number, p, t, q, d);
    }

    public ScheduledJob(int number, int p, int t, int q, int d, ScheduledJob job) {
        this(number, p, t, q, d);
        this.next = job;
    }

    public ScheduledJob(Job j) {
        this(j.getNumber(), j.getProduct(), j.getType(), j.getQuantity(), j.getDueDate());
    }
    public ScheduledJob(Job j, ScheduledJob job) {
        this(j.getNumber(), j.getProduct(), j.getType(), j.getQuantity(), j.getDueDate());
        this.next = job;
    }

    public ScheduledJob getNext() {
        return next;
    }

    public void setNext(ScheduledJob next) {
        this.next = next;
    }
}