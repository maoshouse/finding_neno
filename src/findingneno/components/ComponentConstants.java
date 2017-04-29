package findingneno.components;

public class ComponentConstants {
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

    public static class DbPollerConstants {
	public static final String QUERY_STRING = "SELECT * FROM SUBSCRIPTIONS";
	public static final String CLASS_FIELD = "class";
	public static final String ID_FIELD = "id";
	public static final String URL_FIELD = "url";
	public static final String TAG_FIELD = "tag";
	public static final String TAG_VALUE_FIELD = "tag_value";
    }

    public static class EventConstants {
	public static final String JOB_PARAMETER = "Job";
	public static final String NEW_VALUE_PARAMETER = "New Value";
	public static final String EVENT_GOT_NEW_JOB = "Got new Job";
	public static final String EVENT_REQUEST_SCHEDULE_NEW_JOB = "Schedule new Job";
	public static final String EVENT_REQUEST_WORKFLOW = "Request a new Workflow";
	public static final String REQUEST_VALUE_CHANGED = "Value changed";
	public static final String NOTIFICATION_RESULT_NOTIFY_ERROR = "Notification: Result notify error";
	public static final String NOTIFICATION_RESULT_NOTIFY_SUCCESS = "Notification: Result notify success";
	public static final String NOTIFICATION_RESCHEDULE_JOB = "Notification: Reschedule job";
	public static final String NOTIFICATION_BLACKLIST_JOB = "Notification: Blacklist job";
    }

}
