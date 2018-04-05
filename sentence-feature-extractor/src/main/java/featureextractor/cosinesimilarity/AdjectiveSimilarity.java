package featureextractor.cosinesimilarity;

import featureextractor.sentenceproperties.SentenceAdjectives;
import utils.NLPUtils;

public class AdjectiveSimilarity extends Similarity{

	public AdjectiveSimilarity(String sentence1, String sentence2, NLPUtils nlpUtils){
		sentenceProps = new SentenceAdjectives(sentence1, sentence2, nlpUtils);
	}

}
