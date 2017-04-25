package findingneno.components;

public class Constants {
    public static class ComponentNames {
	public static final String DB_POLLER = "Database Poller";
	public static final String FAILED_JOB_QUEUE = "Failed Job Queue";
	public static final String JOB_SCHEDULER = "Job Scheduler";
	public static final String JOB_WORKFLOW_INITIATOR = "Job Workflow Initiator";
	public static final String OPEN_JOB_QUEUE = "Open Job Queue";
	public static final String RESULT_BATCH_NOTIFIER = "Result Batch Notifier";
    }

    public static class ConnectorNames {
	public static final String DB_POLLER_TO_JOB_SCHEDULER = "Db Poller to Job Scheduler";
	public static final String JOB_SCHEDULER_TO_QUEUES = "Job Scheduler to Queues";
	public static final String QUEUES_TO_WORKFLOW_INITIATOR = "Queues to Workflow Initiators";
	public static final String WORKFLOW_INTIATOR_TO_BATCH_NOTIFIER = "Workflow Initiator to Batch Notifier";
    }
}
