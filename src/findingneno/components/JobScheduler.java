package findingneno.components;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import Prism.core.PrismConstants;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;

public class JobScheduler extends AbstractImplementation {
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
	    } else {
		sendRequest(job);
	    }
	} else if (event.eventType == PrismConstants.REQUEST) {
	    sendRequest(job);
	}
    }

    private void sendRequest(Job job) {
	if (!blacklistedJobs.contains(job.toString())) {
	    Event requestEvent = EventUtil.makeRequest(EventConstants.EVENT_REQUEST_SCHEDULE_NEW_JOB);
	    requestEvent.addParameter(EventConstants.JOB_PARAMETER, job);
	    send(requestEvent);
	}
    }
}
