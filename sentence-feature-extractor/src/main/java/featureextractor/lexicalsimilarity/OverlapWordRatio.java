package featureextractor.lexicalsimilarity;

import featureextractor.sentenceproperties.SentenceProps;
import featureextractor.sentenceproperties.SentenceWords;

import java.util.ArrayList;

public class OverlapWordRatio {

	public ArrayList<Double> getOverlapScore(String sourceSentence, String targetSentence) {

		ArrayList<Double> overlapScores = new ArrayList<Double>();

		SentenceProps wordProps = new SentenceWords(sourceSentence, targetSentence);

		// remember to initialize distinct words and common words
		wordProps.initializeDistinctsCommons();

		double sentence1Ratio = 2 *
				((double) wordProps.getCommons().size() /
						(double) wordProps.getDistincts_sentence1().size());

		double sentence2Ratio = 2 *
				((double) wordProps.getCommons().size() /
						(double) wordProps.getDistincts_sentence2().size());

		overlapScores.add(sentence1Ratio);
		overlapScores.add(sentence2Ratio);
		return overlapScores;
	}
}
