package findingneno.components;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import Prism.core.Event;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;
import lombok.SneakyThrows;

@RunWith(MockitoJUnitRunner.class)
public class ResultBatchNotifierTest {
    private ResultBatchNotifier resultBatchNotifier;

    @Mock
    private HttpClient httpClient;
    @Mock
    private Event event;
    @Mock
    private Job job;
    @Mock
    private HttpResponse httpResponse;
    @Mock
    private StatusLine statusLine;

    private String value;

    @Before
    public void setUp() {
	resultBatchNotifier = new ResultBatchNotifier(httpClient);
    }

    @Test
    @SneakyThrows
    public void testSentPostRequest() {
	when(event.getParameter(EventConstants.JOB_PARAMETER)).thenReturn(job);
	when(event.getParameter(EventConstants.NEW_VALUE_PARAMETER)).thenReturn(value);
	when(httpClient.execute(Mockito.any())).thenReturn(httpResponse);
	when(httpResponse.getStatusLine()).thenReturn(statusLine);
	when(statusLine.getStatusCode()).thenReturn(200);
	resultBatchNotifier.handle(event);
	verify(httpClient).execute(Mockito.any());
    }

}
