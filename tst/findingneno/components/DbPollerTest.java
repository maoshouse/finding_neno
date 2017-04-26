package findingneno.components;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import findingneno.components.TestConstants.SQLConstants;
import lombok.SneakyThrows;

public class DbPollerTest {

    @Test
    @SneakyThrows
    public void test() {
	// TODO actually make a test
	Connection c = null;
	Class.forName("org.postgresql.Driver");
	c = DriverManager.getConnection(SQLConstants.POSTGRES_TEST_URL, SQLConstants.POSTGRES_TEST_USER,
		SQLConstants.POSTGRES_TEST_PASSWORD);
	fail("Not yet implemented");
    }

}
