package findingneno.components;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

import Prism.core.Event;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;

public class JobWorkflowRunnable implements Runnable {
    private final JobWorkflowInitiator jobWorkflowInitiator;
    private final Job job;

    public JobWorkflowRunnable(JobWorkflowInitiator jobWorkflowInitiator, Job job) {
	this.jobWorkflowInitiator = jobWorkflowInitiator;
	this.job = job;
    }

    @Override
    public void run() {
	JBrowserDriver jBrowserDriver = new JBrowserDriver(
		Settings.builder().timezone(Timezone.AMERICA_LOSANGELES).build());
	jBrowserDriver.get(job.getUrl());

	WebElement webElement = null;
	if (StringUtils.isNotEmpty(job.getId())) {
	    webElement = jBrowserDriver.findElementById(job.getId());
	} else if (StringUtils.isNotEmpty(job.getClassName())) {
	    webElement = jBrowserDriver.findElementByClassName(job.getClassName());
	} else {
	    // maybe think about some combo of tag name and css
	}

	if (webElement != null) {
	    String currentValue = webElement.getText();
	    if (StringUtils.equalsIgnoreCase(currentValue, job.getTagValue())) {
		Event requestEvent = EventUtil.makeRequest(EventConstants.REQUEST_VALUE_CHANGED);
		requestEvent.addParameter(EventConstants.JOB_PARAMETER, job);
		requestEvent.addParameter(EventConstants.NEW_VALUE_PARAMETER, currentValue);
		jobWorkflowInitiator.send(requestEvent);
	    }
	}
	jBrowserDriver.close();
    }

}
