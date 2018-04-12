package featureextractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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

		Properties props = new Properties();
		props.setProperty("annotators","tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props);

		String sourceSentence= "She is a good girl.";
		String targetSentence = "Julie likes her brown dog.";

		String text = targetSentence + " " + sourceSentence;

		Annotation annotation = nlpUtils.annotate(text);
		ArrayList<String> sents = nlpUtils.replaceCoreferences(annotation, sourceSentence, targetSentence);
		System.out.println(sents.toString());


	}

}
