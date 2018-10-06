package shiftinview.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utils.SQLiteUtils;

public class ShiftInViewPair {

	private int dbId;

	private int relationshipId;

	private int verbShift;

	private int sentimentShift;

	private double pubMedVal;

	private int pubMedCal;

	private String sourceSentence;

	private String targetSentence;

	private int targetSentenceID;

	private int sourceSentenceID;

	public void setTargetSentenceID(int targetSentenceID) {
		this.targetSentenceID = targetSentenceID;
	}

	public void setSourceSentenceID(int sourceSentenceID) {
		this.sourceSentenceID = sourceSentenceID;
	}

	public int getTargetSentenceID() {
		return targetSentenceID;
	}

	public int getSourceSentenceID() {
		return sourceSentenceID;
	}

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

	public int getVerbShift() {
		return verbShift;
	}

	public void setVerbShift(int verbShift) {
		this.verbShift = verbShift;
	}

	public int getSentimentShift() {
		return sentimentShift;
	}

	public void setSentimentShift(int sentimentShift) {
		this.sentimentShift = sentimentShift;
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
				+ "VERB_SHIFT,"
				+ "SENTIMENT_SHIFT,"
				+ "PUBMED_VAL,"
				+ "PUBMED_CAL) " +
				"VALUES ("
				+ relationshipId + ", " +
				+verbShift + ", " +
				+sentimentShift + ", " +
				+pubMedVal + ", " +
				+pubMedCal + ");";
		System.out.println(sql);
		sqLiteUtils.executeUpdate(sql);
	}

	public static ArrayList<ShiftInViewPair> getAll() throws Exception {
		String sql = "SELECT * FROM SHIFT_IN_VIEW;";
		ResultSet resultSet = sqLiteUtils.executeQuery(sql);

		if (resultSet.isClosed()) {
			return null;
		}

		ArrayList<ShiftInViewPair> pairs = new ArrayList<>();
		ShiftInViewPair pair;
		while (resultSet.next()) {
			pair = new ShiftInViewPair();
			pair.dbId = resultSet.getInt("ID");
			pair.relationshipId = resultSet.getInt("RELATIONSHIP_ID");
			pair.verbShift = resultSet.getInt("VERB_SHIFT");
			pair.sentimentShift = resultSet.getInt("SENTIMENT_SHIFT");
			pair.pubMedVal = resultSet.getDouble("PUBMED_VAL");
			pair.pubMedCal = resultSet.getInt("PUBMED_CAL");

			pairs.add(pair);
		}

		return pairs;
	}

	/**
	 * to be only used for PubMed calculations
	 *
	 * @throws Exception
	 */
	public void update() throws Exception {
		String sql = "UPDATE SHIFT_IN_VIEW SET"
				+ " PUBMED_VAL=" + pubMedVal
				+ ", PUBMED_CAL= " + pubMedCal
				+ " WHERE ID=" + dbId + ";";
		System.out.println(sql);
		sqLiteUtils.executeUpdate(sql);
	}

	/**
	 *
	 * @param value unsigned oppositeness measure double
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<ShiftInViewPair> getPubMed(double value) throws Exception {
		String sql = "SELECT * FROM SHIFT_IN_VIEW WHERE PUBMED_CAL=1 AND PUBMED_VAL<-" + value + ";";
		ResultSet resultSet = sqLiteUtils.executeQuery(sql);

		if (resultSet.isClosed()) {
			return null;
		}

		ArrayList<ShiftInViewPair> pairs = new ArrayList<>();
		ShiftInViewPair pair;
		while (resultSet.next()) {
			pair = new ShiftInViewPair();
			pair.dbId = resultSet.getInt("ID");
			pair.relationshipId = resultSet.getInt("RELATIONSHIP_ID");
			pair.verbShift = resultSet.getInt("VERB_SHIFT");
			pair.sentimentShift = resultSet.getInt("SENTIMENT_SHIFT");
			pair.pubMedVal = resultSet.getDouble("PUBMED_VAL");
			pair.pubMedCal = resultSet.getInt("PUBMED_CAL");

			pairs.add(pair);
		}

		return pairs;
	}
}
