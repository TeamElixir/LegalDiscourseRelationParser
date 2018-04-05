package featureextractor.cosinesimilarity;

import featureextractor.sentenceproperties.SentenceNouns;
import utils.NLPUtils;

public class NounSimilarity extends Similarity{

	public NounSimilarity(String sentence1, String sentence2, NLPUtils nlpUtils){
		sentenceProps = new SentenceNouns(sentence1, sentence2, nlpUtils);
	}

}
