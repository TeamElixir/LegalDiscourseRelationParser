package featureextractor.sentenceproperties;

import java.util.ArrayList;
import java.util.Arrays;

public class SentenceWords extends SentenceProps{

	public SentenceWords(String sentence1, String sentence2){
		seq_sentence1= new ArrayList<String>(Arrays.asList(sentence1.split(" ")));
		seq_sentence2= new ArrayList<String>(Arrays.asList(sentence2.split(" ")));
	}
}

