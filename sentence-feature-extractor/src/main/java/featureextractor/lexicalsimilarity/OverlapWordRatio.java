package featureextractor.lexicalsimilarity;

import featureextractor.sentenceproperties.SentenceProps;
import featureextractor.sentenceproperties.SentenceWords;
import utils.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class OverlapWordRatio {

	public ArrayList<Double> getOverlapScore(String sourceSentence, String targetSentence) {

		ArrayList<Double> overlapScores = new ArrayList<Double>();

		SentenceProps wordProps = new SentenceWords(sourceSentence, targetSentence);

		// remember to initialize distinct words and common words
		wordProps.initializeDistinctsCommons();

		double sentence1Ratio =
				((double) wordProps.getCommons().size() /
						(double) wordProps.getDistincts_sentence1().size());

		double sentence2Ratio =
				((double) wordProps.getCommons().size() /
						(double) wordProps.getDistincts_sentence2().size());

		overlapScores.add(sentence1Ratio);
		overlapScores.add(sentence2Ratio);
//		System.out.println("commons :"+wordProps.getCommons());
//		System.out.println("distingt 1 : "+wordProps.getDistincts_sentence1());
//		System.out.println("distinct 2 : "+wordProps.getDistincts_sentence2());
		return overlapScores;
	}

	public ArrayList<Double> getRefinedOverlapScore(String sourceSentence, String targetSentence) {
		ArrayList<String> present = new ArrayList<>(Arrays.asList(
				"be", "is", "are", "am", "being", "has", "have", "do", "does"));
		ArrayList<String> past = new ArrayList<>(Arrays.asList(
				"was", "were", "been", "would", "should", "did","had"));
		ArrayList<String> future = new ArrayList<>(Arrays.asList("will", "shall"));

		ArrayList<String> stopWords = new ArrayList<>(Arrays.asList("a","an","the","of"))

		ArrayList<Double> overlapScores = new ArrayList<Double>();

		SentenceProps wordProps = new SentenceWords(sourceSentence, targetSentence);

		// remember to initialize distinct words and common words
		wordProps.initializeDistinctsCommons();

		ArrayList<String> commonWords =wordProps.getCommons();
		ArrayList<String> filteredCommonWords=new ArrayList<>();

		for(String commonWord:commonWords){
			if(!present.contains(commonWord.toLowerCase())&& !past.contains(commonWord.toLowerCase()) &&
					!future.contains(commonWord.toLowerCase()) && !stopWords.contains(commonWord.toLowerCase())){

				filteredCommonWords.add(commonWord);

			}
		}


		double sentence1Ratio =
				((double) wordProps.getCommons().size() /
						(double) wordProps.getDistincts_sentence1().size());

		double sentence2Ratio =
				((double) wordProps.getCommons().size() /
						(double) wordProps.getDistincts_sentence2().size());

		overlapScores.add(sentence1Ratio);
		overlapScores.add(sentence2Ratio);
//		System.out.println("commons :"+wordProps.getCommons());
//		System.out.println("distingt 1 : "+wordProps.getDistincts_sentence1());
//		System.out.println("distinct 2 : "+wordProps.getDistincts_sentence2());
		return overlapScores;
	}


}
