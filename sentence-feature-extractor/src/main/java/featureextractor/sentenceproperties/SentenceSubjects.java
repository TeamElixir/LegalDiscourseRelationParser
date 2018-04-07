package featureextractor.sentenceproperties;

import utils.NLPUtils;

public class SentenceSubjects extends SentenceProps{

	public SentenceSubjects(String sentence1, String sentence2, NLPUtils nlpUtils) {
		seq_sentence1 = nlpUtils.getSubjects(sentence1);
		seq_sentence2 = nlpUtils.getSubjects(sentence2);
	}

}
