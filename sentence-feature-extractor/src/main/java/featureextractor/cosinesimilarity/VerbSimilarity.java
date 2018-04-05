package featureextractor.cosinesimilarity;

import featureextractor.sentenceproperties.SentenceVerbs;
import utils.NLPUtils;

public class VerbSimilarity extends Similarity{

	public VerbSimilarity(String sentence1, String sentence2, NLPUtils nlpUtils){
		sentenceProps = new SentenceVerbs(sentence1, sentence2, nlpUtils);
	}

}
