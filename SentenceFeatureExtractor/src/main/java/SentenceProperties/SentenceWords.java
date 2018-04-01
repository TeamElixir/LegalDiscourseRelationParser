package SentenceProperties;

import WordProperties.WordFrequency;
import java.util.Hashtable;
import java.util.LinkedList;

public class SentenceWords {

    String [] word_seq_sentence1 ;
    String [] word_seq_sentence2 ;
    Hashtable<String, WordFrequency> word_freq_vector = new Hashtable<String,WordFrequency>();
    LinkedList<String> distinct_words = new LinkedList<String>();

    public SentenceWords(String sentence1, String sentence2){
        word_seq_sentence1=sentence1.split("");
        word_seq_sentence2=sentence2.split("");
    }

    public void initializeFrequencies() {
        for (int i = 0; i < word_seq_sentence1.length; i++) {
            String tmp_wd = word_seq_sentence1[i].trim();
            if (tmp_wd.length() > 0) {
                if (word_freq_vector.containsKey(tmp_wd)) {
                    WordFrequency vals1 = word_freq_vector.get(tmp_wd);
                    int freq1 = vals1.getSentence1() + 1;
                    int freq2 = vals1.getSentence2();
                    vals1.updateFrequency(freq1, freq2);
                    word_freq_vector.put(tmp_wd, vals1);
                } else {
                    WordFrequency vals1 = new WordFrequency(1, 0);
                    word_freq_vector.put(tmp_wd, vals1);
                    distinct_words.add(tmp_wd);
                }
            }
        }

        //prepare word frequency vector by using Text2
        for (int i = 0; i < word_seq_sentence2.length; i++) {
            String tmp_wd = word_seq_sentence2[i].trim();
            if (tmp_wd.length() > 0) {
                if (word_freq_vector.containsKey(tmp_wd)) {
                    WordFrequency vals1 = word_freq_vector.get(tmp_wd);
                    int freq1 = vals1.getSentence1();
                    int freq2 = vals1.getSentence2() + 1;
                    vals1.updateFrequency(freq1, freq2);
                    word_freq_vector.put(tmp_wd, vals1);
                } else {
                    WordFrequency vals1 = new WordFrequency(0, 1);
                    word_freq_vector.put(tmp_wd, vals1);
                    distinct_words.add(tmp_wd);
                }
            }
        }
    }

}
