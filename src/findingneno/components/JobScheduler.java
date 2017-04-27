package findingneno.components;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import Prism.core.PrismConstants;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;

public class JobScheduler extends AbstractImplementation {

    @Override
    public void start() {

    }

    @Override
    public void handle(Event event) {
	if (event.eventType == PrismConstants.REPLY) {
	    // this was a notification from a higher layer
	    // determine if this is from the dead queue, which should alert job
	    // has failed several times and to stop queueing

	    // if this is from open queue or another, then this means that this
	    // job is good.

	} else if (event.eventType == PrismConstants.REQUEST) {
	    // this was a request from a lower layer
	    Job job = (Job) event.getParameter(EventConstants.JOB_PARAMETER);
	    Event requestEvent = new Event(EventConstants.EVENT_REQUEST_SCHEDULE_NEW_JOB);
	    event.addParameter(EventConstants.JOB_PARAMETER, job);
	    send(requestEvent);
	}

    }

}
