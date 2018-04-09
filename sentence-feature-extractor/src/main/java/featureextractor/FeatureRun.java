package featureextractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import featureextractor.grammaticalrelationships.GrammarOverlapRatio;
import featureextractor.sentencepropertyfeatures.NERRatio;
import utils.NLPUtils;

public class FeatureRun {

	public static void main(String[] args) {

		// noun, verb, adjective check
		/*
		NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos");

		ArrayList<String> nouns = nlpUtils.getNouns("Thejan loves me more than Linda loves me");
		System.out.println("Nouns : " + nouns.toString());

		ArrayList<String> verbs = nlpUtils.getVerbs("My brown dog is beautiful.");
		ArrayList<String> verbsWithOutBe = nlpUtils.getVerbsWithOutBe("My brown dog is beautiful.");
		System.out.println("Verbs : " + verbs.toString());
		System.out.println("VerbsWithOutBe : " + verbsWithOutBe.toString());

		ArrayList<String> adjectives = nlpUtils.getAdjectives("My brown dog is beautiful.");
		System.out.println("Adjectives : " + adjectives.toString());
		*/

		NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos,lemma,ner,depparse,coref");

		String sentence1 = "Julie likes her brown dog.";
		String sentence2 = "She is a good girl.";

		String text = sentence1 + " " +sentence2;

		Annotation annotation = nlpUtils.annotate(text);
		nlpUtils.replaceCoreferences(annotation);


	}

}
