package datasetparser.models;

public class Relationship {

	private int id;
	private String sourceSent;
	private String targetSent;
	private int relation;

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
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public void save(){

	}
}
