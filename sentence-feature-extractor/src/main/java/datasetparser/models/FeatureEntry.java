package datasetparser.models;

public class FeatureEntry {

	private double adjectiveSimilarity;
	private double nounSimilarity;
	private double verbSimilarity;
	private double wordSimilarity;

	private double wordOverlapSSent;
	private double wordOverlapTSent;

	private double lcs;

	private double subjectOverlap;
	private double objectOverlap;
	private double subjectNounOverlap;

	private double nerRatio;
	private double lengthRatio;
	private int tosScore;

	// TODO: 4/19/18 add transitional score

	public double getAdjectiveSimilarity() {
		return adjectiveSimilarity;
	}

	public void setAdjectiveSimilarity(double adjectiveSimilarity) {
		this.adjectiveSimilarity = adjectiveSimilarity;
	}

	public double getNounSimilarity() {
		return nounSimilarity;
	}

	public void setNounSimilarity(double nounSimilarity) {
		this.nounSimilarity = nounSimilarity;
	}

	public double getVerbSimilarity() {
		return verbSimilarity;
	}

	public void setVerbSimilarity(double verbSimilarity) {
		this.verbSimilarity = verbSimilarity;
	}

	public double getWordSimilarity() {
		return wordSimilarity;
	}

	public void setWordSimilarity(double wordSimilarity) {
		this.wordSimilarity = wordSimilarity;
	}

	public double getWordOverlapSSent() {
		return wordOverlapSSent;
	}

	public void setWordOverlapSSent(double wordOverlapSSent) {
		this.wordOverlapSSent = wordOverlapSSent;
	}

	public double getWordOverlapTSent() {
		return wordOverlapTSent;
	}

	public void setWordOverlapTSent(double wordOverlapTSent) {
		this.wordOverlapTSent = wordOverlapTSent;
	}

	public double getLcs() {
		return lcs;
	}

	public void setLcs(double lcs) {
		this.lcs = lcs;
	}

	public double getSubjectOverlap() {
		return subjectOverlap;
	}

	public void setSubjectOverlap(double subjectOverlap) {
		this.subjectOverlap = subjectOverlap;
	}

	public double getObjectOverlap() {
		return objectOverlap;
	}

	public void setObjectOverlap(double objectOverlap) {
		this.objectOverlap = objectOverlap;
	}

	public double getSubjectNounOverlap() {
		return subjectNounOverlap;
	}

	public void setSubjectNounOverlap(double subjectNounOverlap) {
		this.subjectNounOverlap = subjectNounOverlap;
	}

	public double getNerRatio() {
		return nerRatio;
	}

	public void setNerRatio(double nerRatio) {
		this.nerRatio = nerRatio;
	}

	public double getLengthRatio() {
		return lengthRatio;
	}

	public void setLengthRatio(double lengthRatio) {
		this.lengthRatio = lengthRatio;
	}

	public int getTosScore() {
		return tosScore;
	}

	public void setTosScore(int tosScore) {
		this.tosScore = tosScore;
	}

}
