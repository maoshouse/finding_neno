package notifierbackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Prism.core.FIFOScheduler;
import Prism.core.PrismConstants;
import Prism.core.RRobinDispatcher;
import Prism.core.Scaffold;
import Prism.extensions.architecture.ExtensibleArchitecture;
import Prism.extensions.component.ExtensibleComponent;
import Prism.extensions.connector.ExtensibleConnector;
import Prism.style.StyleFactory;
import notifierbackend.components.ComponentConstants.ComponentNames;
import notifierbackend.components.ComponentConstants.ConnectorNames;
import notifierbackend.components.DbPoller;
import notifierbackend.components.FailedJobQueue;
import notifierbackend.components.JobScheduler;
import notifierbackend.components.JobWorkflowInitiator;
import notifierbackend.components.Notifier;
import notifierbackend.components.OpenJobQueue;
import notifierbackend.configuration.Configuration;

public class NotifierBackend {
    private static final Logger logger = LogManager.getLogger(NotifierBackend.class);

    private static Connection makeDbConnection() {
	Connection connection = null;
	try {
	    // Class.forName("com.mysql.cj.jdbc.Driver");
	    connection = DriverManager.getConnection(Configuration.DbPollerConfiguration.MYSQL_URL,
		    Configuration.DbPollerConfiguration.MYSQL_USER, Configuration.DbPollerConfiguration.MYSQL_PASSWORD);
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return connection;
    }

    public static ExtensibleArchitecture makeArchitecture(Scaffold scaffold) {
	ExtensibleArchitecture extensibleArchitecture = StyleFactory
		.generateArchitecture(Configuration.Constants.ARCHITECTURE_NAME, PrismConstants.C2_ARCH);
	ExtensibleComponent dbPollerComponent = StyleFactory.generateComponent(ComponentNames.DB_POLLER,
		PrismConstants.C2_COMP, new DbPoller(makeDbConnection()));
	ExtensibleComponent failedJobQueueComponent = StyleFactory.generateComponent(ComponentNames.FAILED_JOB_QUEUE,
		PrismConstants.C2_COMP, new FailedJobQueue());
	ExtensibleComponent jobSchedulerComponent = StyleFactory.generateComponent(ComponentNames.JOB_SCHEDULER,
		PrismConstants.C2_COMP, new JobScheduler());
	ExtensibleComponent jobWorkflowInitiatorComponent = StyleFactory.generateComponent(
		ComponentNames.JOB_WORKFLOW_INITIATOR, PrismConstants.C2_COMP, new JobWorkflowInitiator());
	ExtensibleComponent openJobQueueComponent = StyleFactory.generateComponent(ComponentNames.OPEN_JOB_QUEUE,
		PrismConstants.C2_COMP, new OpenJobQueue());
	ExtensibleComponent resultBatchNotifierComponent = StyleFactory.generateComponent(
		ComponentNames.RESULT_BATCH_NOTIFIER, PrismConstants.C2_COMP,
		new Notifier(HttpClientBuilder.create().build()));

	dbPollerComponent.scaffold = scaffold;
	failedJobQueueComponent.scaffold = scaffold;
	jobSchedulerComponent.scaffold = scaffold;
	jobWorkflowInitiatorComponent.scaffold = scaffold;
	openJobQueueComponent.scaffold = scaffold;
	resultBatchNotifierComponent.scaffold = scaffold;

	ExtensibleConnector dbPollerToJobSchedulerConnector = StyleFactory
		.generateConnector(ConnectorNames.DB_POLLER_TO_JOB_SCHEDULER, PrismConstants.C2_CONN);
	ExtensibleConnector jobSchedulerToQueuesConnector = StyleFactory
		.generateConnector(ConnectorNames.JOB_SCHEDULER_TO_QUEUES, PrismConstants.C2_CONN);
	ExtensibleConnector queuesToWorkflowInitiatorConnector = StyleFactory
		.generateConnector(ConnectorNames.QUEUES_TO_WORKFLOW_INITIATOR, PrismConstants.C2_CONN);
	ExtensibleConnector workflowInitiatorToBatchNotifierConnector = StyleFactory
		.generateConnector(ConnectorNames.WORKFLOW_INTIATOR_TO_BATCH_NOTIFIER, PrismConstants.C2_CONN);

	dbPollerToJobSchedulerConnector.scaffold = scaffold;
	jobSchedulerToQueuesConnector.scaffold = scaffold;
	queuesToWorkflowInitiatorConnector.scaffold = scaffold;
	workflowInitiatorToBatchNotifierConnector.scaffold = scaffold;

	extensibleArchitecture.add(dbPollerComponent);
	extensibleArchitecture.add(failedJobQueueComponent);
	extensibleArchitecture.add(jobSchedulerComponent);
	extensibleArchitecture.add(jobWorkflowInitiatorComponent);
	extensibleArchitecture.add(openJobQueueComponent);
	extensibleArchitecture.add(resultBatchNotifierComponent);
	extensibleArchitecture.add(dbPollerToJobSchedulerConnector);
	extensibleArchitecture.add(jobSchedulerToQueuesConnector);
	extensibleArchitecture.add(queuesToWorkflowInitiatorConnector);
	extensibleArchitecture.add(workflowInitiatorToBatchNotifierConnector);

	extensibleArchitecture.weld(resultBatchNotifierComponent, workflowInitiatorToBatchNotifierConnector);
	extensibleArchitecture.weld(workflowInitiatorToBatchNotifierConnector, jobWorkflowInitiatorComponent);
	extensibleArchitecture.weld(jobWorkflowInitiatorComponent, queuesToWorkflowInitiatorConnector);
	extensibleArchitecture.weld(queuesToWorkflowInitiatorConnector, openJobQueueComponent);
	extensibleArchitecture.weld(queuesToWorkflowInitiatorConnector, failedJobQueueComponent);
	extensibleArchitecture.weld(openJobQueueComponent, jobSchedulerToQueuesConnector);
	extensibleArchitecture.weld(failedJobQueueComponent, jobSchedulerToQueuesConnector);
	extensibleArchitecture.weld(jobSchedulerToQueuesConnector, jobSchedulerComponent);
	extensibleArchitecture.weld(jobSchedulerComponent, dbPollerToJobSchedulerConnector);
	extensibleArchitecture.weld(dbPollerToJobSchedulerConnector, dbPollerComponent);

	return extensibleArchitecture;
    }

    public static NotifierBackendRunnable makeRunnable() {
	FIFOScheduler fifoScheduler = new FIFOScheduler(Configuration.Constants.FIFO_SCEHDULER_SIZE);
	RRobinDispatcher roundRobinDispatcher = new RRobinDispatcher(fifoScheduler,
		Configuration.Constants.DISPATCHER_THREAD_COUNT);
	Scaffold scaffold = new Scaffold();
	scaffold.scheduler = fifoScheduler;
	scaffold.dispatcher = roundRobinDispatcher;
	ExtensibleArchitecture extensibleArchitecture = makeArchitecture(scaffold);
	extensibleArchitecture.scaffold = scaffold;
	NotifierBackendRunnable findingNenoRunnable = new NotifierBackendRunnable(extensibleArchitecture, roundRobinDispatcher);
	return findingNenoRunnable;
    }

    public static void main(String[] args) {
	Thread thread = new Thread(makeRunnable());
	logger.info("Starting FindingNeno");
	thread.start();
	try {
	    thread.join();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

    }
}
