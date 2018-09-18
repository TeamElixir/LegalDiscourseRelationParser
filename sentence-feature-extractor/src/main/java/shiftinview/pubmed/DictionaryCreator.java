package shiftinview.pubmed;

import static shiftinview.pubmed.TfIdfCal.getWordsOfCase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import utils.NLPUtils;

public class DictionaryCreator {

	public static void main(String[] args) {

		// stores all the words in all cases, case wise with duplicates
		ArrayList<ArrayList<String>> totalWords = new ArrayList<>();

		for (int i = 0; i < 231; i++) {
			totalWords.add(getWordsOfCase(i + 1));
		}

		// stores words from all cases with duplicates
		ArrayList<String> allWordsList = new ArrayList<>();
		// word frequency values for all cases
		ArrayList<HashMap<String, Double>> casesWordFreq = new ArrayList<>();
		for (ArrayList caseWords : totalWords) {
			// flat totalWords list
			allWordsList.addAll(caseWords);
			// get word frequency values for the case
			casesWordFreq.add(getCaseWordFreq(caseWords));
		}

		// distinct words in all cases
		Set<String> distinctWords = new HashSet<>(allWordsList);

		// dictionary
		HashMap<String, Double> dictionary = new HashMap<>();

		// iterating through all the unique words
		for (String distinctWord : distinctWords) {

			// case count containing the word
			double docFrequency = 0.0;
			for (ArrayList<String> docWords : totalWords) {
				if (docWords.contains(distinctWord)) {
					docFrequency++;
				}
			}

			// sum of (term frequency/case word count) values
			double termFreqSum = 0.0;
			for (HashMap<String, Double> caseWordFreq : casesWordFreq) {
				Double freq = caseWordFreq.get(distinctWord);
				if (freq != null) {
					termFreqSum += (double) freq;
				}
			}

			// value/weight for the dictionary
			double dicValue = termFreqSum / docFrequency;

			dictionary.put(distinctWord, dicValue);

			System.out.println(distinctWord + " : " + dicValue);
		}

		Double max = Collections.max(dictionary.values());
		Double min = Collections.min(dictionary.values());

		// scaling dictionary values
		for (String word : dictionary.keySet()) {
			double value = dictionary.get(word);
			double normValue = (((value - min) * (1 - min)) / (max - min)) + min;
			dictionary.put(word, normValue);
		}

		System.out.println("done");

		writeDic(dictionary);

	}

	private static HashMap<String, Double> getCaseWordFreq(ArrayList<String> caseWords) {
		Set<String> distinctWords = new HashSet<>(caseWords);
		HashMap<String, Double> caseFrq = new HashMap<>();
		// case word count with duplicates
		int caseWordCount = caseWords.size();

		for (String word : distinctWords) {
			double termFrequency = (double) Collections.frequency(caseWords, word);

			// term frequency/case word count
			double value = termFrequency / caseWordCount;

			caseFrq.put(word, value);
		}

		return caseFrq;
	}

	private static void writeDic(HashMap<String, Double> dictionary) {
		try (PrintWriter writer = new PrintWriter("dictionary.csv", "UTF-8")) {
			for (String word : dictionary.keySet()) {
				double value = dictionary.get(word);
				writer.println(word + "," + value);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, Double> readDic() {
		HashMap<String, Double> dictionary = new HashMap<>();

		String filePath = "/home/thejan/FYP/LegalDisourceRelationParser/sentence-feature-extractor/";
		filePath += "dictionary.txt";

		try (FileReader fileReader = new FileReader(filePath)) {
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			while ((line = br.readLine()) != null) {
				String[] temp = line.split("\t");
				dictionary.put(temp[0], Double.parseDouble(temp[1]));
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return dictionary;
	}

}
