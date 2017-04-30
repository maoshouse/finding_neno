package findingneno.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import Prism.core.PrismConstants;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;
import findingneno.configuration.Configuration;

public class JobWorkflowInitiator extends AbstractImplementation {
    // private static final Logger logger =
    // LogManager.getLogger(JobWorkflowInitiator.class.getName());

    private ExecutorService executorService;

    public JobWorkflowInitiator() {
	executorService = Executors
		.newFixedThreadPool(Configuration.JobWorkflowInitiatorConfiguration.INITIATOR_WORKFLOW_THREADPOOL_SIZE);
    }

    @Override
    public void handle(Event event) {
	if (event.eventType == PrismConstants.REPLY) {
	    // initiator doesn't take care of errors and doesnt expect a
	    // notification.
	    send(event);
	} else if (event.eventType == PrismConstants.REQUEST) {
	    // this was a request from a lower layer
	    Job job = (Job) event.getParameter(EventConstants.JOB_PARAMETER);
	    executorService.submit(new JobWorkflowRunnable(this, job));
	}
    }

}
