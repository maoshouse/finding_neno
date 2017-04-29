package findingneno.components;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import Prism.core.PrismConstants;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;

public class JobScheduler extends AbstractImplementation {
    private static final Logger logger = LogManager.getLogger(JobScheduler.class.getName());

    private HashSet<String> blacklistedJobs;

    public JobScheduler() {
	blacklistedJobs = new HashSet<String>();
    }

    @Override
    public void handle(Event event) {
	Job job = (Job) event.getParameter(EventConstants.JOB_PARAMETER);
	if (event.eventType == PrismConstants.REPLY) {
	    if (StringUtils.equals(event.name, EventConstants.NOTIFICATION_BLACKLIST_JOB)) {
		blacklistedJobs.add(job.toString());
		logger.info("Added " + job.toString() + " to blacklist");
	    } else {
		sendRequest(job);
	    }
	} else if (event.eventType == PrismConstants.REQUEST) {
	    sendRequest(job);
	    logger.info("Resubmitted " + job.toString());
	}
    }

    private void sendRequest(Job job) {
	if (!blacklistedJobs.contains(job.toString())) {
	    Event requestEvent = EventUtil.makeRequest(EventConstants.EVENT_REQUEST_SCHEDULE_NEW_JOB);
	    requestEvent.addParameter(EventConstants.JOB_PARAMETER, job);
	    send(requestEvent);
	    logger.info("Submitted " + job.toString());
	} else {
	    logger.info("Dropped " + job.toString());
	}
    }
}
