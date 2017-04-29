package findingneno.components;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import Prism.core.PrismConstants;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;
import findingneno.configuration.Configuration;

public class OpenJobQueue extends AbstractImplementation {
    private static final Logger logger = LogManager.getLogger(OpenJobQueue.class.getName());

    private Queue<Job> openQueue;

    public OpenJobQueue() {
	openQueue = new LinkedList<Job>();
    }

    @Override
    public void handle(Event event) {
	if (event.eventType == PrismConstants.REQUEST) {
	    Job job = (Job) event.getParameter(EventConstants.JOB_PARAMETER);
	    openQueue.add(job);
	    if (openQueue.size() >= Configuration.OpenQueueConfiguration.MAX_QUEUE_SIZE) {
		while (!openQueue.isEmpty()) {
		    Job nextJob = openQueue.poll();
		    Event requestEvent = EventUtil.makeRequest(EventConstants.EVENT_REQUEST_WORKFLOW);
		    requestEvent.addParameter(EventConstants.JOB_PARAMETER, nextJob);
		    send(requestEvent);
		    logger.info("Request: Submitting to initiator " + job.toString());
		}
	    }
	}
    }
}
