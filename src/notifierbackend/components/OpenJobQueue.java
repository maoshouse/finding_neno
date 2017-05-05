package notifierbackend.components;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import Prism.core.PrismConstants;
import notifierbackend.Job.Job;
import notifierbackend.components.ComponentConstants.EventConstants;
import notifierbackend.configuration.Configuration;

public class OpenJobQueue extends AbstractImplementation {
    private static final Logger logger = LogManager.getLogger(OpenJobQueue.class.getName());

    private ConcurrentHashMap<String, Job> openQueue;

    public OpenJobQueue() {
	openQueue = new ConcurrentHashMap<String, Job>();
    }

    @Override
    public void handle(Event event) {
	if (event.eventType == PrismConstants.REQUEST) {
	    Job job = (Job) event.getParameter(EventConstants.JOB_PARAMETER);
	    openQueue.put(job.toString(), job);
	    if (openQueue.size() >= Configuration.OpenQueueConfiguration.MAX_QUEUE_SIZE) {
		for (Job nextJob : openQueue.values()) {

		    Event requestEvent = EventUtil.makeRequest(EventConstants.EVENT_REQUEST_WORKFLOW);
		    requestEvent.addParameter(EventConstants.JOB_PARAMETER, nextJob);
		    send(requestEvent);
		    logger.info("Request: Submitting to initiator " + job.toString());
		}
		openQueue.clear();
	    }
	}
    }
}
