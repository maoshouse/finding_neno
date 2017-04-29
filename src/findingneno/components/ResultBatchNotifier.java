package findingneno.components;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;
import findingneno.configuration.Configuration.ResultBatchNotifierConfiguration;

public class ResultBatchNotifier extends AbstractImplementation {

    @Override
    public void handle(Event event) {
	Job job = (Job) event.getParameter(EventConstants.JOB_PARAMETER);
	String value = (String) event.getParameter(EventConstants.NEW_VALUE_PARAMETER);
	int statusCode = -1;
	try {
	    statusCode = sendPostRequest(job, value);
	} catch (Exception e) {
	    // handle
	}

	if (statusCode < 200 || statusCode >= 300) {
	    // notify initiator that notification failed, log it and try
	    // initiating again.
	    Event notificationEvent = EventUtil.makeNotification(EventConstants.NOTIFICATION_RESULT_NOTIFY_ERROR);
	    notificationEvent.addParameter(EventConstants.JOB_PARAMETER, job);
	    notificationEvent.addParameter(EventConstants.NEW_VALUE_PARAMETER, value);
	    send(notificationEvent);
	} else {
	    Event notificationEvent = EventUtil.makeNotification(EventConstants.NOTIFICATION_RESULT_NOTIFY_SUCCESS);
	    notificationEvent.addParameter(EventConstants.JOB_PARAMETER, job);
	    notificationEvent.addParameter(EventConstants.NEW_VALUE_PARAMETER, value);
	    send(notificationEvent);
	}
    }

    public int sendPostRequest(Job job, String value) throws ClientProtocolException, IOException {
	HttpClient httpClient = HttpClientBuilder.create().build();
	HttpPost httpPost = new HttpPost(ResultBatchNotifierConfiguration.POST_URL);
	// httpPost.addHeader("User-Agent", "lol");
	HttpResponse httpResponse = httpClient.execute(httpPost);
	int statusCode = httpResponse.getStatusLine().getStatusCode();
	return statusCode;
    }
}
