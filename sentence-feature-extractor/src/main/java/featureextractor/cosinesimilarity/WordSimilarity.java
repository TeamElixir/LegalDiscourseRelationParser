package featureextractor.cosinesimilarity;

import java.util.Hashtable;
import java.util.LinkedList;

import featureextractor.sentenceproperties.SentenceWords;
import featureextractor.wordproperties.WordFrequency;

public class WordSimilarity {

	public double cosine_word_similarity_score(String sentence1, String sentence2){

		double similarity_score = 0.0000000;
		String sentenceOne=sentence1;
		String sentenceTwo=sentence2;
		SentenceWords sentenceWords = new SentenceWords(sentence1,sentence2);
		sentenceWords.initializeFrequencies();

		double vectS1S2 = 0.0000000;
		double vectS1_Sq = 0.0000000;
		double vectS2_Sq = 0.0000000;

		LinkedList<String> distinct_words = sentenceWords.getDistinct_words();
		Hashtable<String, WordFrequency> word_freq_vector = sentenceWords.getWord_freq_vector();


		for(int i=0;i<distinct_words.size();i++)
		{
			WordFrequency frequencyValues = word_freq_vector.get(distinct_words.get(i));

			double freq1 = (double)frequencyValues.getSentence1();
			double freq2 = (double)frequencyValues.getSentence2();

			System.out.println(distinct_words.get(i)+"#"+freq1+"#"+freq2);

			vectS1S2=vectS1S2+(freq1*freq2);

			vectS1_Sq = vectS1_Sq + freq1*freq1;
			vectS2_Sq = vectS2_Sq + freq2*freq2;

		}

		System.out.println("VectS1S2 "+vectS1S2+" VectS1_Sq "+vectS1_Sq+" VectS2_Sq "+vectS2_Sq);
		similarity_score = ((vectS1S2)/(Math.sqrt(vectS1_Sq)*Math.sqrt(vectS2_Sq)));

		return similarity_score;
	}

}
