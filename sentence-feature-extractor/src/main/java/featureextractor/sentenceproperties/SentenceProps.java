package featureextractor.sentenceproperties;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import featureextractor.wordproperties.Frequency;

public abstract class SentenceProps {

	protected ArrayList<String> seq_sentence1;
	protected ArrayList<String> seq_sentence2;
	protected ArrayList<String> distinct_words_sentence1;
	protected ArrayList<String> distinct_words_sentence2;
	protected ArrayList<String> common_words;
	private Hashtable<String, Frequency> freq_vector = new Hashtable<String,Frequency>();
	private LinkedList<String> distincts = new LinkedList<String>();

	public void initializeFrequencies() {
		for (int i = 0; i < seq_sentence1.size(); i++) {
			String tmp_wd = seq_sentence1.get(i).trim();
			if (tmp_wd.length() > 0) {
				if (freq_vector.containsKey(tmp_wd)) {
					Frequency vals1 = freq_vector.get(tmp_wd);
					int freq1 = vals1.getSentence1() + 1;
					int freq2 = vals1.getSentence2();
					vals1.updateFrequency(freq1, freq2);
					freq_vector.put(tmp_wd, vals1);
				} else {
					Frequency vals1 = new Frequency(1, 0);
					freq_vector.put(tmp_wd, vals1);
					distincts.add(tmp_wd);
				}
			}
		}

		//prepare word frequency vector by using Text2
		for (int i = 0; i < seq_sentence2.size(); i++) {
			String tmp_wd = seq_sentence2.get(i).trim();
			if (tmp_wd.length() > 0) {
				if (freq_vector.containsKey(tmp_wd)) {
					Frequency vals1 = freq_vector.get(tmp_wd);
					int freq1 = vals1.getSentence1();
					int freq2 = vals1.getSentence2() + 1;
					vals1.updateFrequency(freq1, freq2);
					freq_vector.put(tmp_wd, vals1);
				} else {
					Frequency vals1 = new Frequency(0, 1);
					freq_vector.put(tmp_wd, vals1);
					distincts.add(tmp_wd);
				}
			}
		}
	}

	public ArrayList<String> getSeq_sentence1() {
		return seq_sentence1;
	}

	public void setSeq_sentence1(ArrayList<String> seq_sentence1) {
		this.seq_sentence1 = seq_sentence1;
	}

	public ArrayList<String> getSeq_sentence2() {
		return seq_sentence2;
	}

	public void setSeq_sentence2(ArrayList<String> seq_sentence2) {
		this.seq_sentence2 = seq_sentence2;
	}

	public Hashtable<String, Frequency> getFreq_vector() {
		return freq_vector;
	}

	public void setFreq_vector(Hashtable<String, Frequency> freq_vector) {
		this.freq_vector = freq_vector;
	}

	public LinkedList<String> getDistincts() {
		return distincts;
	}

	public void setDistincts(LinkedList<String> distincts) {
		this.distincts = distincts;
	}

	public ArrayList<String> getDistinct_words_sentence1() {
		return distinct_words_sentence1;
	}

	public ArrayList<String> getDistinct_words_sentence2() {
		return distinct_words_sentence2;
	}

	public ArrayList<String> getCommon_words() {
		return common_words;
	}
}
