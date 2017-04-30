package findingneno.components;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import Prism.core.PrismConstants;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;
import findingneno.configuration.Configuration.FailedJobQueueConfiguration;

public class FailedJobQueue extends AbstractImplementation {
    private static final Logger logger = LogManager.getLogger(FailedJobQueue.class.getName());

    private LoadingCache<String, Integer> failedJobsCache;

    public FailedJobQueue() {
	failedJobsCache = CacheBuilder.newBuilder().maximumSize(FailedJobQueueConfiguration.MAX_CACHE_SIZE)
		.expireAfterAccess(FailedJobQueueConfiguration.CACHE_ENTRY_TTL_MIN, TimeUnit.MINUTES)
		.build(new CacheLoader<String, Integer>() {
		    @Override
		    public Integer load(String key) throws Exception {
			return new Integer(0);
		    }
		});
    }

    @Override
    public void handle(Event event) {
	// failed job queue does not take requests
	if (event.eventType == PrismConstants.REPLY) {
	    Job job = (Job) event.getParameter(EventConstants.JOB_PARAMETER);
	    try {
		Integer jobFailures = failedJobsCache.get(job.toString());
		logger.info(job.toString() + " has failed " + jobFailures + " times");
		failedJobsCache.put(job.toString(), jobFailures + 1);
		jobFailures = failedJobsCache.get(job.toString());
		Event notifyEvent = null;
		if (jobFailures >= FailedJobQueueConfiguration.MAX_JOB_FAIL_COUNT) {
		    failedJobsCache.invalidate(job.toString());
		    // notify scheduler to stop scheduling this job
		    notifyEvent = EventUtil.makeNotification(EventConstants.NOTIFICATION_BLACKLIST_JOB);
		    logger.info("Notify: job failed too many times");
		} else {
		    // notify scheduler to reschedule
		    notifyEvent = EventUtil.makeNotification(EventConstants.NOTIFICATION_RESCHEDULE_JOB);
		    logger.info("Notify: reschedule job");
		}
		notifyEvent.addParameter(EventConstants.JOB_PARAMETER, job);
		send(notifyEvent);
	    } catch (ExecutionException e) {
		logger.error("Cache error: " + e.getMessage());
	    }
	}

    }

}
