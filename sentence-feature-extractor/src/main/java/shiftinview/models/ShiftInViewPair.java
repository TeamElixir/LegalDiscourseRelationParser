package shiftinview.models;

import java.sql.SQLException;

import utils.SQLiteUtils;

public class ShiftInViewPair {

	private int dbId;
	private int relationshipId;
	private int linShift;
	private double pubMedVal;
	private int pubMedCal;
	private String sourceSentence;
	private String targetSentence;

	private static SQLiteUtils sqLiteUtils;

	static {
		try {
			sqLiteUtils = new SQLiteUtils();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getDbId() {
		return dbId;
	}

	public int getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(int relationshipId) {
		this.relationshipId = relationshipId;
	}

	public int getLinShift() {
		return linShift;
	}

	public void setLinShift(int linShift) {
		this.linShift = linShift;
	}

	public double getPubMedVal() {
		return pubMedVal;
	}

	public void setPubMedVal(double pubMedVal) {
		this.pubMedVal = pubMedVal;
	}

	public int getPubMedCal() {
		return pubMedCal;
	}

	public void setPubMedCal(int pubMedCal) {
		this.pubMedCal = pubMedCal;
	}

	public String getSourceSentence() {
		return sourceSentence;
	}

	public void setSourceSentence(String sourceSentence) {
		this.sourceSentence = sourceSentence;
	}

	public String getTargetSentence() {
		return targetSentence;
	}

	public void setTargetSentence(String targetSentence) {
		this.targetSentence = targetSentence;
	}

	public void save() throws SQLException {
		String sql = "INSERT INTO SHIFT_IN_VIEW"
				+ "(RELATIONSHIP_ID,"
				+ "LIN_SHIFT,"
				+ "PUBMED_VAL,"
				+ "PUBMED_CAL,"
				+ "SSENT,"
				+ "TSENT) " +
				"VALUES ("
				+ relationshipId + ", " +
				+ linShift + ", " +
				+ pubMedVal + ", " +
				+ pubMedCal + ", "
				+ sourceSentence + ", "
				+ targetSentence + ");";
		System.out.println(sql);
		sqLiteUtils.executeUpdate(sql);
	}
}
