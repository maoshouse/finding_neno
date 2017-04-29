package findingneno.components;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import findingneno.configuration.Configuration;

public class DbPoller extends AbstractImplementation {
    private static final Logger logger = LogManager.getLogger(DbPoller.class.getName());

    private final Connection connection;
    private ScheduledExecutorService scheduledExecutorService;

    public DbPoller(Connection connection) {
	this.connection = connection;
	scheduledExecutorService = Executors
		.newScheduledThreadPool(Configuration.DbPollerConfiguration.THREADPOOL_SIZE);
    }

    @Override
    public void start() {
	DbPollerRunnable dbPollerRunnable = new DbPollerRunnable(this, connection);
	scheduledExecutorService.scheduleWithFixedDelay(dbPollerRunnable, 0L,
		Configuration.DbPollerConfiguration.THREADPOOL_SCHEDULE_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    public void stop() {
	scheduledExecutorService.shutdown();
	try {
	    scheduledExecutorService.awaitTermination(
		    Configuration.DbPollerConfiguration.THREADPOOL_TERMINATION_TIMEOUT_MILLISECONDS,
		    TimeUnit.MILLISECONDS);
	} catch (InterruptedException exception) {
	    // TODO log4j stuff
	}
    }

    @Override
    public void handle(Event event) {
	// I don't think the poller really cares unless some fatal error
	// downstream wants us to shut the service down
    }
}
