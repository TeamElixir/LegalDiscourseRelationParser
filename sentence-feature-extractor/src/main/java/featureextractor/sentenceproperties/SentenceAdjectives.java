package featureextractor.sentenceproperties;

import utils.NLPUtils;

public class SentenceAdjectives extends SentenceProps {

	public SentenceAdjectives(String sentence1, String sentence2, NLPUtils nlpUtils) {
		seq_sentence1 = nlpUtils.getAdjectives(sentence1);
		seq_sentence2 = nlpUtils.getAdjectives(sentence2);
	}

}
