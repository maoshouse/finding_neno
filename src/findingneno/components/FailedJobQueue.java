package findingneno.components;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
		jobFailures++;
		Event notifyEvent = null;
		if (jobFailures >= FailedJobQueueConfiguration.MAX_JOB_FAIL_COUNT) {
		    // notify scheduler to stop scheduling this job
		    notifyEvent = EventUtil.makeNotification(EventConstants.NOTIFICATION_BLACKLIST_JOB);
		} else {
		    // notify scheduler to reschedule
		    notifyEvent = EventUtil.makeNotification(EventConstants.NOTIFICATION_RESCHEDULE_JOB);
		}
		notifyEvent.addParameter(EventConstants.JOB_PARAMETER, job);
		send(notifyEvent);
	    } catch (ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

}
