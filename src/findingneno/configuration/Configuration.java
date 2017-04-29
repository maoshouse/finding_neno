package findingneno.configuration;

public class Configuration {
    public static class Constants {
	public static final String ARCHITECTURE_NAME = "FINDING_NENO";
	public static final int FIFO_SCEHDULER_SIZE = 100;
	public static final int DISPATCHER_THREAD_COUNT = 10;
    }

    public static class DbPollerConfiguration {
	public static final int THREADPOOL_SIZE = 1;
	public static final long THREADPOOL_TERMINATION_TIMEOUT_MILLISECONDS = 1000L;
	public static final long THREADPOOL_SCHEDULE_DELAY_SECONDS = 5L;
	public static final String MYSQL_URL = "jdbc:mysql://35.184.10.72/dorys_shells";
	public static final String MYSQL_USER = "root";
	public static final String MYSQL_PASSWORD = "csci578cool";
    }

    public static class OpenQueueConfiguration {
	public static final int MAX_QUEUE_SIZE = 1;
    }

    public static class FailedJobQueueConfiguration {
	public static final int MAX_JOB_FAIL_COUNT = 3;
	public static final long MAX_CACHE_SIZE = 1000L;
	public static final long CACHE_ENTRY_TTL_MIN = 5L;
    }

    public static class JobWorkflowInitiatorConfiguration {
	public static final int INITIATOR_WORKFLOW_THREADPOOL_SIZE = 5;
    }

    public static class ResultBatchNotifierConfiguration {
	public static final String POST_URL = "post_url";
    }
}
