package datasetparser.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import datasetparser.utils.SQLiteUtils;

public class Relationship {

	private int id;
	private String sourceSent;
	private String targetSent;
	private int type;

	private static SQLiteUtils sqLiteUtils;

	static {
		try {
			sqLiteUtils = new SQLiteUtils();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSourceSent() {
		return sourceSent;
	}

	public void setSourceSent(String sourceSent) {
		this.sourceSent = sourceSent;
	}

	public String getTargetSent() {
		return targetSent;
	}

	public void setTargetSent(String targetSent) {
		this.targetSent = targetSent;
	}

	public int getRelation() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void save() throws SQLException {
		String sql = "INSERT INTO RELATIONSHIP (ID,SSENT,TSENT,TYPE) " +
				"VALUES ("
				+ id + ", " +
				"\"" + sourceSent + "\"" + ", " +
				"\"" + targetSent + "\"" + ", " +
				type + ");";
		System.out.println(sql);
		sqLiteUtils.executeUpdate(sql);
	}

	public ArrayList<Relationship> getAll() throws SQLException {
		String sql = "SELECT * FROM RELATIONSHIP;";
		ResultSet resultSet = sqLiteUtils.executeQuery(sql);

		if(resultSet.isClosed()){
			return null;
		}

		ArrayList<Relationship> relationships = new ArrayList<Relationship>();
		Relationship entry;
		while (resultSet.next()){
			entry = new Relationship();
			entry.id = resultSet.getInt("ID");
			entry.sourceSent = resultSet.getString("SSENT");
			entry.targetSent = resultSet.getString("TSENT");
			entry.type = resultSet.getInt("TYPE");


			relationships.add(entry);
		}

		return relationships;
	}
}
