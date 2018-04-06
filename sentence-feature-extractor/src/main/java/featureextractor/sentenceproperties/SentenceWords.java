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
			String wordLowerCase = word.toLowerCase();
			seq_sentence1.add(wordLowerCase);
			if(!distinct_words_sentence1.contains(wordLowerCase)){
				distinct_words_sentence1.add(wordLowerCase);
			}
		}

		while (m2.find())
		{
			String word = m2.group();
			String wordLowerCase = word.toLowerCase();
			seq_sentence2.add(word);
			if(!distinct_words_sentence2.contains(wordLowerCase)){
				distinct_words_sentence2.add(wordLowerCase);
				if(distinct_words_sentence1.contains(wordLowerCase)){
					common_words.add(wordLowerCase);
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

