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
	public static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/findingneno_db";
	public static final String POSTGRES_USER = "rick";
	public static final String POSTGRES_PASSWORD = "morty";
    }

    public static class OpenQueueConfiguration {
	public static final int MAX_QUEUE_SIZE = 1;
    }

}
