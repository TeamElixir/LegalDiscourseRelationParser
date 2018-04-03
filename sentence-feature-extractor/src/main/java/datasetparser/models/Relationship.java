package datasetparser.models;

import java.sql.SQLException;

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
}
