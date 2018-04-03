package datasetparser.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datasetparser.utils.SQLiteUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "R")
public class RelationshipEntry {

	private String source;

	@XmlAttribute(name = "SDID")
	private String sdId;

	@XmlAttribute(name = "SSENT")
	private int sSent;

	@XmlAttribute(name = "TDID")
	private String tdId;

	@XmlAttribute(name = "TSENT")
	private int tSent;

	@XmlElement(name = "RELATION")
	private List<Relation> relations;

	public String getSdId() {
		return sdId;
	}

	public int getSSent() {
		return sSent;
	}

	public String getTdId() {
		return tdId;
	}

	public int getTSent() {
		return tSent;
	}

	public List<Relation> getRelations() {
		return relations;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "TABLE")
	public static class Table {

		@XmlElement(name = "R")
		private ArrayList<RelationshipEntry> relationshipEntries;

		public ArrayList<RelationshipEntry> getRelationshipEntries() {
			return relationshipEntries;
		}
	}

	public void save() throws SQLException {
		SQLiteUtils sqLiteUtils = new SQLiteUtils();
		for (Relation relation : relations) {
			String sql = "INSERT INTO RELATIONSHIP_ENTRY (SDID,SSENT,TDID,TSENT,TYPE,JUDGE,SOURCE) " +
					"VALUES ("
					+ "'" + sdId + "'" + ", " +
					sSent + ", " +
					"'" + tdId + "'" + ", " +
					tSent + ", " +
					relation.getType() + ", " +
					"'" + relation.getJudge() + "'" + ", " +
					"'" + source + "'" + ");";
			System.out.println(sql);
			sqLiteUtils.executeUpdate(sql);
		}
	}

	public void getAll() throws SQLException {
		SQLiteUtils sqLiteUtils = new SQLiteUtils();

	}

}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RELATION")
class Relation {

	@XmlAttribute(name = "TYPE")
	private int type;

	@XmlAttribute(name = "JUDGE")
	private String judge;

	public int getType() {
		return type;
	}

	public String getJudge() {
		return judge;
	}
}
