package featureextractor.sentenceproperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceWords extends SentenceProps{

	public SentenceWords(String sentence1, String sentence2){
		// Extracting words from string
		Pattern p = Pattern.compile("[a-zA-Z]+");
		Matcher m1 = p.matcher(sentence1);
		Matcher m2 = p.matcher(sentence2);

		seq_sentence1= new ArrayList<String>();
		seq_sentence2= new ArrayList<String>();
		distinct_words_sentence1=new ArrayList<String>();
		distinct_words_sentence2=new ArrayList<String>();
		common_words=new ArrayList<String>();



		while (m1.find())
		{
			String word = m1.group();
			seq_sentence1.add(word);
			if(!distinct_words_sentence1.contains(word)){
				distinct_words_sentence1.add(word);
			}
		}

		while (m2.find())
		{
			String word = m2.group();
			seq_sentence2.add(word);
			if(!distinct_words_sentence2.contains(word)){
				distinct_words_sentence2.add(word);
				if(distinct_words_sentence1.contains(word)){
					common_words.add(word);
				}
			}


		}
		System.out.println("st1 :"+seq_sentence1);
		System.out.println("st2 :"+seq_sentence2);
		System.out.println("dt1 :"+distinct_words_sentence1);
		System.out.println("dt2 :"+distinct_words_sentence2);
		System.out.println("cw :"+common_words);
	}
}

