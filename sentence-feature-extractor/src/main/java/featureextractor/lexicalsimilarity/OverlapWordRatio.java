package featureextractor.lexicalsimilarity;

import featureextractor.sentenceproperties.SentenceProps;
import featureextractor.sentenceproperties.SentenceWords;
import utils.WordUtils;

import java.util.ArrayList;

public class OverlapWordRatio {

	private ArrayList<String> stopWords = WordUtils.getStopWords();
	private ArrayList<String> effectiveCommons = new ArrayList<>();
	private ArrayList<String> effectiveDistinctSent1 = new ArrayList<>();
	private ArrayList<String> effectiveDistinctSent2 = new ArrayList<>();

	public ArrayList<Double> getOverlapScore(String sourceSentence, String targetSentence) {

		ArrayList<Double> overlapScores = new ArrayList<Double>();



		SentenceProps wordProps = new SentenceWords(sourceSentence, targetSentence);

		// remember to initialize distinct words and common words
		wordProps.initializeDistinctsCommons();

		for (String word : wordProps.getCommons()){
			if(!stopWords.contains(word)){
				effectiveCommons.add(word);
			}
		}
		
		for (String word : wordProps.getDistincts_sentence1()){
			if(!stopWords.contains(word)){
				effectiveDistinctSent1.add(word);
			}
		}

		for(String word : wordProps.getDistincts_sentence2()){
			if(!stopWords.contains(word)){
				effectiveDistinctSent2.add(word);
			}
		}


		double sentence1Ratio =
				((double) effectiveCommons.size() /
						(double) effectiveDistinctSent1.size());

		double sentence2Ratio =
				((double) effectiveCommons.size() /
						(double) effectiveDistinctSent2.size());

		overlapScores.add(sentence1Ratio);
		overlapScores.add(sentence2Ratio);
		return overlapScores;
	}
}
