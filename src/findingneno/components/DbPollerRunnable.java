package findingneno.components;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Prism.core.Event;
import findingneno.Job.Job;
import findingneno.components.ComponentConstants.DbPollerConstants;
import findingneno.components.ComponentConstants.EventConstants;

public class DbPollerRunnable implements Runnable {
    private static final Logger logger = LogManager.getLogger(DbPollerRunnable.class.getName());

    private final DbPoller dbPoller;
    private final Connection connection;

    public DbPollerRunnable(DbPoller dbPoller, Connection connection) {
	this.dbPoller = dbPoller;
	this.connection = connection;
    }

    @Override
    public void run() {
	try {
	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery(DbPollerConstants.QUERY_STRING);
	    while (resultSet.next()) {
		String subscriptionId = resultSet.getString(DbPollerConstants.SUBSCRIPTION_ID_FIELD);
		String id = resultSet.getString(DbPollerConstants.ELEMENT_ID_FIELD);
		String className = resultSet.getString(DbPollerConstants.ELEMENT_CLASS_FIELD);
		String url = resultSet.getString(DbPollerConstants.URL_FIELD);
		String tag = resultSet.getString(DbPollerConstants.ELEMENT_NAME_FIELD);
		String tagValue = resultSet.getString(DbPollerConstants.ELEMENT_VALUE_FIELD);
		Event requestEvent = EventUtil.makeRequest(EventConstants.EVENT_GOT_NEW_JOB);
		Job job = new Job(subscriptionId, id, className, url, tag, tagValue);
		requestEvent.addParameter(EventConstants.JOB_PARAMETER, job);
		logger.info(job.toString());
		dbPoller.send(requestEvent);
	    }
	    logger.info("Query finished");
	} catch (SQLException e) {
	    logger.error("DB query error: " + e.getMessage());
	}
    }
}
