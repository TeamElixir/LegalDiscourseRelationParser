package shiftinview.pubmed;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.util.WordNetUtil;
import edu.stanford.nlp.pipeline.Annotation;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.dictionary.Dictionary;
import shiftinview.ConstituentParser;
import utils.NLPUtils;
import utils.models.Triple;

public class TripleAnalyzer {

	private static final double wYes = 1;

	private static final double wNo = 10;

	private HashMap<String, Double> dictionary;

	public TripleAnalyzer() {
		dictionary = new HashMap<>();
		dictionary = DictionaryCreator.readDic();
	}

	public void analyze(ArrayList<Triple> sourceTriples, ArrayList<Triple> targetTriples) throws Exception {
		double similT = 0.0;
		double diffT = 0.0;
		int sN = 0;
		int dN = 0;

		for (Triple sourceTriple : sourceTriples) {
			// TODO: 9/16/18 remove stop words from both triple word list
			// TODO: 9/16/18 replace if one word with lemma
			String sourceRelation = sourceTriple.relation;
			String[] sourceRelationWordArray = sourceRelation.split(" ");
			ArrayList<String> sourceRelationWords = new ArrayList<>(Arrays.asList(sourceRelationWordArray));
			for (Triple targetTriple : targetTriples) {
				String targetRelation = targetTriple.relation;
				String[] targetRelationWordArray = targetRelation.split(" ");
				ArrayList<String> targetRelationWords = new ArrayList<>(Arrays.asList(targetRelationWordArray));

				for (int i = 0; i < sourceRelationWords.size(); i++) {
					String sWord = sourceRelationWords.get(i);
					for (int j = 0; j < targetRelationWords.size(); j++) {
						String tWord = targetRelationWords.get(j);

						if (sWord.equalsIgnoreCase(tWord)) {
							if (i != 0 && j != 0) {
								if ("not".equalsIgnoreCase(sourceRelationWords.get(i - 1)) && "not"
										.equalsIgnoreCase(targetRelationWords.get(j - 1))) {
									similT += Math.pow((dictionary.get(sWord)), 2.0) * wYes;
									sN++;
								}
							} else if (i != 0) {
								if ("not".equalsIgnoreCase(sourceRelationWords.get(i - 1))) {
									diffT += Math.pow((dictionary.get(sWord)), 2.0) * wNo;
									dN++;
								}
							} else if (j != 0) {
								if ("not".equalsIgnoreCase(targetRelationWords.get(j - 1))) {
									diffT += Math.pow((dictionary.get(sWord)), 2.0) * wNo;
									dN++;
								}
							} else {
								similT += Math.pow((dictionary.get(sWord)), 2.0) * wYes;
								sN++;
							}
						} else {
							SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();
							double simil = similarity.wordSimilarity(sWord, POS.v, tWord, POS.v);
							simil = simil / (sourceRelationWordArray.length + targetRelationWordArray.length);

							Dictionary dic = Dictionary.getInstance();

							IndexWord indexSWord = getIndexedWord(sWord, dic);
							IndexWord indexTWord = getIndexedWord(tWord, dic);

							String sWordLemma = sWord;
							String tWordLemma = tWord;
							if (indexSWord != null) {
								sWordLemma = indexSWord.getLemma();
							}
							if (indexTWord != null) {
								tWordLemma = indexTWord.getLemma();
							}

							ArrayList<String> synAntonymsSWord = getSynAntonyms(sWordLemma, dic);
							ArrayList<String> synAntonymsTWord = getSynAntonyms(tWordLemma, dic);

							if(synAntonymsSWord.isEmpty()){

							}

						}
					}
				}

			}
		}
	}

	private ArrayList<String> getSynAntonyms(String wordLemma, Dictionary dictionary) throws Exception {
		Synset[] synWord = getSynsets(wordLemma, dictionary);

		PointerUtils pu = PointerUtils.getInstance();

		PointerTargetNodeList ptnl = new PointerTargetNodeList();
		if (synWord != null) {
			for (int k = 0; k < synWord.length; k++) {
				ptnl.addAll(pu.getAntonyms(synWord[k]));
			}
		}

		HashSet<String> candidates = new HashSet<>();

		if (ptnl.size() > 0) {
			Object[] resultsAsArray = ptnl.toArray();

			for (int i = 0; i < resultsAsArray.length; i++) {
				Synset synset = ((PointerTargetNode) resultsAsArray[i]).getSynset();
				Word[] words = synset.getWords();
				for (int j = 0; j < words.length; j++) {
					candidates.add(words[j].getLemma());
				}
			}
		}

		ArrayList<String> antonyms = new ArrayList<>(candidates);

		return antonyms;
	}

	private Synset[] getSynsets(String rawWord, Dictionary dictionary) throws Exception {
		IndexWord word = getIndexedWord(rawWord, dictionary);

		if (word == null) {
			return null;
		}

		Synset[] s = word.getSenses();

		if (s.length == 0) {
			return null;
		}

		return s;
	}

	private IndexWord getIndexedWord(String rawWord, Dictionary dictionary) throws Exception {
		IndexWord word = dictionary.lookupIndexWord(net.didion.jwnl.data.POS.VERB, rawWord);

		if (word == null) {
			word = dictionary.lookupIndexWord(net.didion.jwnl.data.POS.NOUN, rawWord);
		}
		if (word == null) {
			word = dictionary.lookupIndexWord(net.didion.jwnl.data.POS.ADJECTIVE, rawWord);
		}
		if (word == null) {
			word = dictionary.lookupIndexWord(net.didion.jwnl.data.POS.ADVERB, rawWord);
		}
		if (word == null) {
			return null;
		}

		return word;
	}

	public ArrayList<Triple> generateTripleList(Annotation annotation, NLPUtils nlpUtils) {
		ArrayList<Triple> list = nlpUtils.getTriples(annotation);

		if (list.isEmpty()) {
			ArrayList<String> innerSentences;
			ConstituentParser constituentParser = new ConstituentParser();
			innerSentences = constituentParser.runConstituentParser(annotation, nlpUtils);
			System.out.println(innerSentences.toString());

			for (String insentence : innerSentences) {
				annotation = nlpUtils.annotate(insentence);
				ArrayList<Triple> tempList = nlpUtils.getTriples(annotation);
				if (!(tempList.isEmpty())) {
					list.addAll(tempList);
				}
			}
		}

		return list;
	}

	public static void main(String[] args) throws Exception{
		JWNL.initialize(new FileInputStream(
				"/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/src/main/resources/jwnl_properties.xml"));
		Dictionary dic = Dictionary.getInstance();
		TripleAnalyzer tn = new TripleAnalyzer();

		String sWord = "man";
		IndexWord indexSWord = tn.getIndexedWord(sWord, dic);

		String sWordLemma = sWord;
		if (indexSWord != null) {
			sWordLemma = indexSWord.getLemma();
		}

		ArrayList<String> synAntonymsSWord = tn.getSynAntonyms(sWordLemma, dic);
		System.out.println(synAntonymsSWord);
	}

}
