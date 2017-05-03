package findingneno.components;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Prism.core.AbstractImplementation;
import Prism.core.Event;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;
import findingneno.configuration.Configuration.ResultBatchNotifierConfiguration;

public class ResultBatchNotifier extends AbstractImplementation {
    private static final Logger logger = LogManager.getLogger(ResultBatchNotifier.class.getName());

    private final HttpClient httpClient;

    public ResultBatchNotifier(HttpClient httpClient) {
	this.httpClient = httpClient;
    }

    @Override
    public void handle(Event event) {
	Job job = (Job) event.getParameter(EventConstants.JOB_PARAMETER);
	String value = (String) event.getParameter(EventConstants.NEW_VALUE_PARAMETER);
	int statusCode = -1;
	try {
	    statusCode = sendPostRequest(job, value);
	} catch (Exception e) {
	    logger.error("HttpClient error: " + e.getMessage());
	}

	if (statusCode < 200 || statusCode >= 300) {
	    Event notificationEvent = EventUtil.makeNotification(EventConstants.NOTIFICATION_RESULT_NOTIFY_ERROR);
	    notificationEvent.addParameter(EventConstants.JOB_PARAMETER, job);
	    notificationEvent.addParameter(EventConstants.NEW_VALUE_PARAMETER, value);
	    send(notificationEvent);
	    logger.info("Notify: Http Post failure, failed to notify");
	}

	if (statusCode == 200) {
	    logger.debug("put!!");
	}
    }

    private int sendPostRequest(Job job, String value) throws ClientProtocolException, IOException {
	HttpPost httpPost = new HttpPost(ResultBatchNotifierConfiguration.PUT_URL);
	ArrayList<NameValuePair> postParameters;
	postParameters = new ArrayList<NameValuePair>();
	postParameters.add(new BasicNameValuePair("update", "1"));
	postParameters.add(new BasicNameValuePair("subscriptionid", job.getSubscriptionId()));
	postParameters.add(new BasicNameValuePair("newValue", value));
	httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

	HttpResponse httpResponse = httpClient.execute(httpPost);
	String responseAsString = EntityUtils.toString(httpResponse.getEntity());
	logger.debug("Post response: " + responseAsString);
	int statusCode = httpResponse.getStatusLine().getStatusCode();
	return statusCode;
    }
}
