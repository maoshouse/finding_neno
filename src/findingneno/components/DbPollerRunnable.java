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
		String id = resultSet.getString(DbPollerConstants.ID_FIELD);
		String className = resultSet.getString(DbPollerConstants.CLASS_FIELD);
		String url = resultSet.getString(DbPollerConstants.URL_FIELD);
		String tag = resultSet.getString(DbPollerConstants.TAG_FIELD);
		String tagValue = resultSet.getString(DbPollerConstants.TAG_VALUE_FIELD);
		Event requestEvent = EventUtil.makeRequest(EventConstants.EVENT_GOT_NEW_JOB);
		requestEvent.addParameter(EventConstants.JOB_PARAMETER, new Job(id, className, url, tag, tagValue));
		dbPoller.send(requestEvent);
	    }
	} catch (SQLException e) {
	    logger.error("DB query error: " + e.getMessage());
	}
    }
}
