package featureextractor.grammaticalrelationships;

import featureextractor.sentenceproperties.SentenceObjects;
import featureextractor.sentenceproperties.SentenceProps;
import featureextractor.sentenceproperties.SentenceSubjects;
import utils.NLPUtils;

public class GrammerOverlapRatio {

	private String sentence1;
	private String sentence2;
	private NLPUtils nlpUtils;

	public GrammerOverlapRatio(String sentence1, String sentence2, NLPUtils nlpUtils) {
		this.sentence1 = sentence1;
		this.sentence2 = sentence2;
		this.nlpUtils = nlpUtils;
	}

	public double getSubjectOverlap(){
		SentenceProps sentenceProps = new SentenceSubjects(sentence1,sentence2,nlpUtils);

		return ((double)sentenceProps.getCommons().size() /
				(double)sentenceProps.getDistincts_sentence1().size());
	}

	public double getObjectOverlap(){
		SentenceProps sentenceProps = new SentenceObjects(sentence1,sentence2,nlpUtils);

		return ((double)sentenceProps.getCommons().size() /
				(double)sentenceProps.getDistincts_sentence1().size());
	}

	// TODO: 4/7/18
	public double getSubjectNounOverlap(){
		SentenceProps sentenceProps = new SentenceSubjects(sentence1,sentence2,nlpUtils);

		return 0;
	}
}
