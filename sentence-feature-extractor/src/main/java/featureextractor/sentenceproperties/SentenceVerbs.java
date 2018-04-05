package featureextractor.sentenceproperties;

import utils.NLPUtils;

public class SentenceVerbs extends SentenceProps{

	public SentenceVerbs(String sentence1, String sentence2, NLPUtils nlpUtils) {
		seq_sentence1 = nlpUtils.getVerbsWithOutBe(sentence1);
		seq_sentence2 = nlpUtils.getVerbsWithOutBe(sentence2);
	}

}
