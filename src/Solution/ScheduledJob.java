package Solution;

import Instance.Job;

/**
 * Created by n on 08/02/16.
 */
public class ScheduledJob extends Job {
    private ScheduledJob next;

    public ScheduledJob(int p, int t, int q, int d) {
        super(p, t, q, d);
    }

    public ScheduledJob(int p, int t, int q, int d, ScheduledJob job) {
        this(p, t, q, d);
        this.next = job;
    }

    public ScheduledJob(Job j) {
        this(j.getProduct(), j.getType(), j.getQuantity(), j.getDueDate());
    }

    public ScheduledJob(Job j, ScheduledJob job) {
        this(j.getProduct(), j.getType(), j.getQuantity(), j.getDueDate());
        this.next = job;
    }
}