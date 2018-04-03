package datasetparser.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteUtils {

	private static Connection connection = null;

	public SQLiteUtils() throws SQLException {
		if(connection==null){
			connection = DriverManager.getConnection("jdbc:sqlite:extractorDatabase.db");
		}
	}

	public Connection getConnection() throws SQLException {
		return connection;
	}

	public void executeUpdate(String sql) throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		Statement statement = connection.createStatement();
		return statement.executeQuery(sql);
	}

	// TABLE CREATION SQLs
	/*
	String sql = "CREATE TABLE RELATIONSHIP_ENTRY " +
			"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
			" SDID      CHAR(2)     NOT NULL, " +
			" SSENT     INT         NOT NULL, " +
			" TDID      CHAR(2)     NOT NULL, " +
			" TSENT     INT         NOT NULL, " +
			" TYPE      INT         NOT NULL, " +
			" JUDGE     CHAR(1)     NOT NULL, " +
			" SOURCE    CHAR(20)    NOT NULL )";

	String sql = "CREATE TABLE SENTENCE_ENTRY " +
				"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				" SNO       INT     NOT NULL, " +
				" SENT      TEXT        NOT NULL, " +
				" DID       CHAR(2)     NOT NULL, " +
				" SOURCE    CHAR(20)    NOT NULL )";
	*/

}