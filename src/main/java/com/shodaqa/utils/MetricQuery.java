package com.shodaqa.utils;
import org.hibernate.stat.Statistics;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public class MetricQuery {
    // some interesting metrics
    long queryExecutions;
    long transactions;
    long entityLoads;
    long connects;
    long time;
    private org.hibernate.stat.Statistics stats;

    public MetricQuery(Statistics statisticss) {
        this.stats = statisticss;
    }

    public void start() {
        queryExecutions = -stats.getQueryExecutionCount();
        transactions = -stats.getTransactionCount();
        entityLoads = -stats.getEntityLoadCount();
        connects = -stats.getConnectCount();
        time = -System.currentTimeMillis();
    }

    public void stop() {
        queryExecutions += stats.getQueryExecutionCount();
        transactions += stats.getTransactionCount();
        entityLoads += stats.getEntityLoadCount();
        connects += stats.getConnectCount();
        time += System.currentTimeMillis();
    }

    // getter methods for various delta metrics

    public String toString() {
        return "Stats"
                + "[ queries=" + queryExecutions
                + ", xactions=" + transactions
                + ", loads=" + entityLoads
                + ", connects=" + connects
                + ", time=" + time + " ]";
    }
}
