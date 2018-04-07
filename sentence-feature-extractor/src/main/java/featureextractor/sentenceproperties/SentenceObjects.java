package featureextractor.sentenceproperties;

import utils.NLPUtils;

public class SentenceObjects extends SentenceProps{

	public SentenceObjects(String sentence1, String sentence2, NLPUtils nlpUtils) {
		seq_sentence1 = nlpUtils.getObjects(sentence1);
		seq_sentence2 = nlpUtils.getObjects(sentence2);
	}

}
