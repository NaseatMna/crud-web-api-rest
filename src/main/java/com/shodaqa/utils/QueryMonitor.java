package com.shodaqa.utils;

import org.springframework.context.annotation.PropertySource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.stat.Statistics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
/**
 * Created by Naseat_PC on 9/9/2017.
 */
@PropertySource(value = {"classpath:application.properties"})
public class QueryMonitor {
    private static final Log log = LogFactory.getLog(MetricQuery.class);
    private static final boolean isEnabled = true;
    private static QueryMonitor singleton = null;
    private ConcurrentMap<Long, MetricQuery> watches;
    private AtomicLong idGenerator = new AtomicLong(0);
    private Statistics statistics;

    private QueryMonitor(Statistics statistic) {
        this.statistics = statistic;
        watches = new ConcurrentHashMap<Long, MetricQuery>();
    }

    public static QueryMonitor get(Statistics statistics) {
        if (singleton == null)
            return singleton = new QueryMonitor(statistics);
        return singleton;
    }

    public long start() {
        if (isEnabled) {
            MetricQuery watch = new MetricQuery(this.statistics);
            long id = idGenerator.incrementAndGet();
            watches.put(id, watch);
            watch.start();
            return id;
        }
        return 0;
    }

    public MetricQuery stop(long id, String callingContext) {
        if (isEnabled) {
            MetricQuery watch = watches.remove(id);
            System.out.println("Will Happend: " + watch.toString());
            if (watch == null) {
                // could happen if debugging was enabled after start() was called
                return null;
            }
            watch.stop();
            String caller = (callingContext == null ? "(unknown)" : callingContext);
            log.debug(watch.toString() + " for " + caller);
            return watch;
        }
        return null;
    }
}
