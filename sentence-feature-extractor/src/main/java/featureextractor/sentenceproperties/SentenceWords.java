package featureextractor.sentenceproperties;

public class SentenceWords extends SentenceProps{

	public SentenceWords(String sentence1, String sentence2){
		seq_sentence1=sentence1.split("");
		seq_sentence2=sentence2.split("");
	}
}

