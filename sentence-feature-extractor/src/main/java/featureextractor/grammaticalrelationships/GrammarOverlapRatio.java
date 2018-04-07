package featureextractor.grammaticalrelationships;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import featureextractor.sentenceproperties.SentenceObjects;
import featureextractor.sentenceproperties.SentenceProps;
import featureextractor.sentenceproperties.SentenceSubjects;
import utils.NLPUtils;

public class GrammarOverlapRatio {

	private String sentence1;
	private String sentence2;
	private NLPUtils nlpUtils;

	public GrammarOverlapRatio(String sentence1, String sentence2, NLPUtils nlpUtils) {
		this.sentence1 = sentence1;
		this.sentence2 = sentence2;
		this.nlpUtils = nlpUtils;
	}

	public double getSubjectOverlap() {
		SentenceProps sentenceProps = new SentenceSubjects(sentence1, sentence2, nlpUtils);

		sentenceProps.initializeDistinctsCommons();

		return ((double) sentenceProps.getCommons().size() /
				(double) sentenceProps.getDistincts_sentence1().size());
	}

	public double getObjectOverlap() {
		SentenceProps sentenceProps = new SentenceObjects(sentence1, sentence2, nlpUtils);

		sentenceProps.initializeDistinctsCommons();

		return ((double) sentenceProps.getCommons().size() /
				(double) sentenceProps.getDistincts_sentence1().size());
	}

	public double getSubjectNounOverlap() {
		ArrayList<String> subjectsSent1 = nlpUtils.getSubjects(sentence1);
		ArrayList<String> nounsSent2 = nlpUtils.getNouns(sentence2);

		// distinct subjects
		Set<String> set = new HashSet<String>(subjectsSent1);
		// number of distinct subjects
		int noSubjects = set.size();

		// common elements from subjects and nouns
		set.retainAll(nounsSent2);
		// number of common elements
		int noCommon = set.size();

		return (double) noCommon / (double) noSubjects;
	}
}
