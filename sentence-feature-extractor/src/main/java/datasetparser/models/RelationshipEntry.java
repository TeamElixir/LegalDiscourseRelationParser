package datasetparser.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "R")
public class RelationshipEntry {
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

}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RELATION")
class Relation{

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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TABLE")
class Table{

	@XmlElement(name = "R")
	private ArrayList<RelationshipEntry> relationshipEntries;

	public ArrayList<RelationshipEntry> getRelationshipEntries() {
		return relationshipEntries;
	}
}
