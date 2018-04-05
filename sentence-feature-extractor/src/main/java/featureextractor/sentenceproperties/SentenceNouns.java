package featureextractor.sentenceproperties;

import utils.NLPUtils;

public class SentenceNouns extends SentenceProps {

	public SentenceNouns(String sentence1, String sentence2, NLPUtils nlpUtils) {
		seq_sentence1 = nlpUtils.getNouns(sentence1);
		seq_sentence2 = nlpUtils.getNouns(sentence2);
	}

}
