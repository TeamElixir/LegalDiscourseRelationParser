package shiftinview.pubmed;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import edu.cmu.lti.jawjaw.pobj.POS;
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

	double sigma = 0.1;

	private HashMap<String, Double> dictionary;

	public Dictionary wordnetDic;

	public PointerUtils pu;

	private String[] stopWordsArr = new String[] { "a", "about", "above", "across", "after", "afterwards", "again",
			"against",
			"all", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst",
			"amoungst", "amount", "an", "and", "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere",
			"are", "around", "as", "at", "back", "be", "became", "because", "become", "becomes", "becoming", "been",
			"before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both",
			"bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de",
			"describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven",
			"else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything",
			"everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former",
			"formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has",
			"hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers",
			"herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed",
			"interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less",
			"ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most",
			"mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless",
			"next", "nine", "no", "nobody", "none", "noone", "nor", "nothing", "now", "nowhere", "of", "off", "often",
			"on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out",
			"over", "own", "part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed",
			"seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six",
			"sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still",
			"such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence",
			"there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin",
			"third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together",
			"too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us",
			"very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where",
			"whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while",
			"whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "within", "would", "yet", "you",
			"your", "yours", "yourself", "yourselves", "the", "did", "does"};

	private ArrayList<String> stopWords = new ArrayList<>(Arrays.asList(stopWordsArr));

	public TripleAnalyzer() throws Exception {
		dictionary = new HashMap<>();
		dictionary = DictionaryCreator.readDic();

		JWNL.initialize(new FileInputStream(
				"/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/src/main/resources/jwnl_properties.xml"));

		wordnetDic = Dictionary.getInstance();
		pu = PointerUtils.getInstance();
	}

	public Double analyze(ArrayList<Triple> sourceTriples, ArrayList<Triple> targetTriples) throws Exception {

		boolean cal = false;

		// triples from source sentence
		for (Triple sourceTriple : sourceTriples) {

			String sourceRelation = sourceTriple.relation;
			sourceRelation = replaceNt(sourceRelation);
			String[] sourceRelationWordArray = sourceRelation.split(" ");
			ArrayList<String> sourceRelationWords = new ArrayList<>(Arrays.asList(sourceRelationWordArray));

			// replace with lemma if single word relationship
			if (sourceRelationWords.size() == 1) {
				IndexWord indexWord = getIndexedWord(sourceRelationWords.get(0), wordnetDic);
				if (indexWord != null) {
					sourceRelationWords.set(0, indexWord.getLemma());
				}
			}

			// triples from target sentence
			for (Triple targetTriple : targetTriples) {

				if(!(targetTriple.subject.contains(sourceTriple.subject) || sourceTriple.subject.contains(targetTriple.subject))){
					continue;
				}

				System.out.println("source triple: " + sourceTriple.toString());
				System.out.println("target triple: " + targetTriple.toString());

				String targetRelation = targetTriple.relation;
				targetRelation = replaceNt(targetRelation);
				String[] targetRelationWordArray = targetRelation.split(" ");
				ArrayList<String> targetRelationWords = new ArrayList<>(Arrays.asList(targetRelationWordArray));

				// replace with lemma if single word relationship
				if (targetRelationWords.size() == 1) {
					IndexWord indexWord = getIndexedWord(targetRelationWords.get(0), wordnetDic);
					if (indexWord != null) {
						targetRelationWords.set(0, indexWord.getLemma());
					}
				}

				// values for each triple permutation
				double similT = 0.0;
				double diffT = 0.0;
				int sN = 0;
				int dN = 0;

				for (int i = 0; i < sourceRelationWords.size(); i++) {
					String sWord = sourceRelationWords.get(i).toLowerCase();
					for (int j = 0; j < targetRelationWords.size(); j++) {
						String tWord = targetRelationWords.get(j).toLowerCase();

						// if stopword no similarity measure, but not break eg: not in stop word list
						if (stopWords.contains(sWord) || stopWords.contains(tWord)) {
							continue;
						}

						if(!dictionary.containsKey(sWord) || !dictionary.containsKey(tWord)){
							continue;
						}

						System.out.println("SWord: " + sWord);
						System.out.println("TWord: " + tWord);

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

							IndexWord indexSWord = getIndexedWord(sWord, wordnetDic);
							IndexWord indexTWord = getIndexedWord(tWord, wordnetDic);

							String sWordLemma = sWord;
							String tWordLemma = tWord;
							if (indexSWord != null) {
								sWordLemma = indexSWord.getLemma();
							}
							if (indexTWord != null) {
								tWordLemma = indexTWord.getLemma();
							}

							SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();
							double simil = similarity.wordSimilarity(sWordLemma, POS.v, tWordLemma, POS.v);
							simil = simil / (sourceRelationWordArray.length + targetRelationWordArray.length);

							System.out.println(sWordLemma + " " + tWordLemma + " " + simil);

							ArrayList<String> synAntonymsSWord = getSynAntonyms(sWordLemma, wordnetDic);
							ArrayList<String> synAntonymsTWord = getSynAntonyms(tWordLemma, wordnetDic);

							double diff1 = 0;
							for (String antonym : synAntonymsTWord) {
								diff1 = Math.max(diff1, similarity.wordSimilarity(sWordLemma, POS.v, antonym, POS.v));
							}

							double diff2 = 0;
							for (String antonym : synAntonymsSWord) {
								diff2 = Math.max(diff1, similarity.wordSimilarity(tWordLemma, POS.v, antonym, POS.v));
							}

							double diff =
									((diff1 / targetRelationWordArray.length) + (diff2 / sourceRelationWordArray.length))
											/ 2;

							double oppo = (double) Math
									.max(Math.pow(Math.min(diff, 1), (wNo / (2 * wYes)) * Math.min(simil, 1) + 1),
											0) * (-1);

							if (oppo > 0) {
								similT += oppo * wYes ;
								sN++;
							} else if (oppo < 0) {
								diffT += oppo * wNo;
								dN++;
							}
						}
					}
				} // end both word loops in a triple relation

				similT = (similT * (dN + sigma) * wYes) / (sN + dN + 2 * sigma); //Calculate by inverse
				diffT = (diffT * (sN + sigma) * wNo) / (sN + dN + 2 * sigma);

				System.out.println("similT : " + similT);
				System.out.println("diffT : " + diffT);

				double simDifValue;

				if (similT < diffT) {
					simDifValue = diffT * (-1);
				} else {
					simDifValue = similT;
				}

				simDifValue = simDifValue * sourceTriple.confidence * targetTriple.confidence;

				cal = true;
			}

		}

		if(!cal){
			return null;
		}

		return new Double(1.0);

	}

	private ArrayList<String> getSynAntonyms(String wordLemma, Dictionary dictionary) throws Exception {
		Synset[] synWord = getSynsets(wordLemma, dictionary);

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

	public ArrayList<Triple> removeDuplicates(ArrayList<Triple> triples) {
		ArrayList<Integer> indexToRemove = new ArrayList<>();

		for (int i = 0; i < triples.size() - 1; i++) {
			for (int j = i + 1; j < triples.size(); j++) {
				if (triples.get(i).subject.equalsIgnoreCase(triples.get(j).subject)
						&& triples.get(i).relation.equalsIgnoreCase(triples.get(j).relation)
						&& triples.get(i).object.equalsIgnoreCase(triples.get(j).object)) {
					triples.remove(triples.get(j));
					j--;
				} else {
				}
			}
		}

		return triples;
	}

	public String replaceNt(String text) {
		text = text.replace("can't", "can not");
		text = text.replace("won't", "will not");
		text = text.replace("shan't", "shall not");
		text = text.replace("n't", " not");

		return text;
	}

	public static void main(String[] args) throws Exception {
		/*
		TripleAnalyzer tripleAnalyzer = new TripleAnalyzer();

		//		String targetSentence = "Although he has lived in this country for most of his life, Lee is not a United States citizen, and he feared that a criminal conviction might affect his status as a lawful permanent resident.";

		//		String sourceSentence
		//				= "His attorney assured him there was nothing to worry about,the Government would not deport him if he pleaded guilty.";

		String targetSentence = "Petitioner Jae Lee moved to the United States from South Korea with his parents when he was 13.";
		String sourceSentence = "In the 35 years he has spent in this country, he has never returned to South Korea, nor has he become a U. S. citizen, living instead as a lawful permanent resident.";

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,natlog,openie,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props);

		// text to resolve coreferences
		String corefText = targetSentence + " " + sourceSentence;

		// annotate both sentences in order to resolve coreferences
		Annotation annotation = nlpUtils.annotate(corefText);
		ArrayList<String> resolvedSents = nlpUtils.replaceCoreferences(annotation);

		// coreferences replaced new sentences
		sourceSentence = resolvedSents.get(0);
		targetSentence = resolvedSents.get(1);

		// annotate again
		Annotation sourceAnnotation = nlpUtils.annotate(sourceSentence);
		Annotation targetAnnotation = nlpUtils.annotate(targetSentence);

		// generate triple list
		ArrayList<Triple> sourceTriples = tripleAnalyzer.generateTripleList(sourceAnnotation, nlpUtils);
		ArrayList<Triple> targetTriples = tripleAnalyzer.generateTripleList(targetAnnotation, nlpUtils);

		sourceTriples = tripleAnalyzer.removeDuplicates(sourceTriples);
		targetTriples = tripleAnalyzer.removeDuplicates(targetTriples);

		System.out.println(sourceTriples);
		System.out.println(targetTriples);
		*/

		TripleAnalyzer tripleAnalyzer = new TripleAnalyzer();
//		Dictionary dic = tripleAnalyzer.wordnetDic;
//
//		IndexWord indexWord = tripleAnalyzer.getIndexedWord("indicated", dic);
//		String lemma = indexWord.getLemma();
//
//		SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();
//		double simil = similarity.wordSimilarity(lemma, POS.v, "receive", POS.v);
//
//		System.out.println("done" + simil);

		Triple sTriple = new Triple();
		sTriple.subject = "Lee";
		sTriple.relation = "increase";
		sTriple.object = "marks";
		ArrayList<Triple> sTriples = new ArrayList<>();
		sTriples.add(sTriple);

		Triple tTriple = new Triple();
		tTriple.subject = "Lee";
		tTriple.relation = "decrease";
		tTriple.object = "marks";
		ArrayList<Triple> tTriples = new ArrayList<>();
		tTriples.add(tTriple);

		tripleAnalyzer.analyze(sTriples,tTriples);

		//		tripleAnalyzer.analyze(sourceTriples, targetTriples);

		//		JWNL.initialize(new FileInputStream(
		//				"/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/src/main/resources/jwnl_properties.xml"));
		//
		//		Dictionary wordnetDic = Dictionary.getInstance();
		//
		//		System.out.println(tripleAnalyzer.getSynAntonyms("affect",wordnetDic));
	}

}
