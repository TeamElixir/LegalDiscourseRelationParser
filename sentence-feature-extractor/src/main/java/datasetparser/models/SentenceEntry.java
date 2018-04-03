package datasetparser.models;

public class SentenceEntry {

	private String sentence;
	private int sentenceNo;
	private String documentId;
	private String source;

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		sentence = sentence.trim();
		String[] arr = sentence.split("^[0-9]+");

		if(arr.length==2){
			sentence = arr[1].trim();
		}
		this.sentence = sentence;
	}

	public int getSentenceNo() {
		return sentenceNo;
	}

	public void setSentenceNo(int sentenceNo) {
		this.sentenceNo = sentenceNo;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
