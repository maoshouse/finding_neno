package findingneno.components;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import Prism.core.Event;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.EventConstants;
import findingneno.configuration.Configuration.FailedJobQueueConfiguration;
import lombok.SneakyThrows;

@RunWith(MockitoJUnitRunner.class)
public class ResultBatchNotifierTest {
    private Notifier resultBatchNotifier;

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
	resultBatchNotifier = new Notifier(httpClient);
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

    @Test
    @SneakyThrows
    public void testasdf() {
	LoadingCache<String, Integer> loadingCache = CacheBuilder.newBuilder()
		.maximumSize(FailedJobQueueConfiguration.MAX_CACHE_SIZE)
		.expireAfterAccess(FailedJobQueueConfiguration.CACHE_ENTRY_TTL_MIN, TimeUnit.MINUTES)
		.build(new CacheLoader<String, Integer>() {
		    @Override
		    public Integer load(String key) throws Exception {
			return new Integer(0);
		    }
		});

	Integer i = loadingCache.get("hello");
	i++;
	Integer j = loadingCache.get("hello");
	System.out.println(j);
    }

}
