package findingneno.components;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

import Prism.core.Event;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;

public class JobWorkflowRunnable implements Runnable {
    private static final Logger logger = LogManager.getLogger(JobWorkflowRunnable.class.getName());

    private final JobWorkflowInitiator jobWorkflowInitiator;
    private final Job job;

    public JobWorkflowRunnable(JobWorkflowInitiator jobWorkflowInitiator, Job job) {
	this.jobWorkflowInitiator = jobWorkflowInitiator;
	this.job = job;
    }

    @Override
    public void run() {
	try {
	    JBrowserDriver jBrowserDriver = new JBrowserDriver(
		    Settings.builder().timezone(Timezone.AMERICA_LOSANGELES).build());
	    jBrowserDriver.get(job.getUrl());

	    WebElement webElement = null;
	    if (StringUtils.isNotEmpty(job.getId())) {
		logger.info("Finding element by id");
		webElement = jBrowserDriver.findElementById(job.getId());
	    } else if (StringUtils.isNotEmpty(job.getClassName())) {
		logger.info("Finding element by class");
		webElement = jBrowserDriver.findElementByClassName(job.getClassName());
	    } else {
		// maybe think about some combo of tag name and css
	    }

	    if (webElement != null) {
		String currentValue = webElement.getText();
		if (!StringUtils.equalsIgnoreCase(currentValue, job.getTagValue())) {
		    Event requestEvent = EventUtil.makeRequest(EventConstants.REQUEST_VALUE_CHANGED);
		    requestEvent.addParameter(EventConstants.JOB_PARAMETER, job);
		    requestEvent.addParameter(EventConstants.NEW_VALUE_PARAMETER, currentValue);
		    jobWorkflowInitiator.send(requestEvent);
		    logger.info("Request: Value changed: " + job.getTagValue() + " => " + currentValue);
		} else {
		    logger.info("Value didn't change: " + job.getTagValue() + " => " + currentValue);
		}
	    } else {
		logger.info("Element not found");
	    }
	    // jBrowserDriver.close();
	} catch (Exception e) {
	    Event notificationEvent = EventUtil.makeNotification(EventConstants.NOTIFICATION_WORKFLOW_FAILURE);
	    notificationEvent.addParameter(EventConstants.JOB_PARAMETER, job);
	    jobWorkflowInitiator.send(notificationEvent);
	    logger.info("Notify: Jbrowser failure on Job ");
	}
    }

}
