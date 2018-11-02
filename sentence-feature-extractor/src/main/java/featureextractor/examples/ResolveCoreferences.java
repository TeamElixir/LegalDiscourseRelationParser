package featureextractor.examples;

import java.util.ArrayList;
import java.util.Properties;

import edu.stanford.nlp.pipeline.Annotation;
import utils.NLPUtils;

public class ResolveCoreferences {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("annotators","tokenize,ssplit,pos,lemma,ner,parse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props, "http://corenlp.run", 80);
 	//NLPUtils nlpUtils = new NLPUtils(props);

		String targetSentence =
				"Although he has lived in this country for most of his life, Lee is not a United States" +
				" citizen, and he feared that a criminal conviction might affect his status as a lawful " +
				"permanent resident.";
		String sourceSentence =
				"His attorney assured him there was nothing to worry about,the Government would not " +
				"deport him if he pleaded guilty.";

		// text to resolve coreferences
		String corefText = targetSentence + " " + sourceSentence;

		// annotate both sentences in order to resolve coreferences
		Annotation annotation = nlpUtils.annotate(corefText);
		ArrayList<String> resolvedSents = nlpUtils.replaceCoreferences(annotation);

		// coreferences replaced new sentences
		sourceSentence = resolvedSents.get(0);
		targetSentence = resolvedSents.get(1);

		System.out.println("source sentence: " + sourceSentence);
		System.out.println("target sentence: " + targetSentence);
	}

}
