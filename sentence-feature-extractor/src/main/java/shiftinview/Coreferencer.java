package shiftinview;

import edu.stanford.nlp.pipeline.Annotation;
import utils.NLPUtils;

import java.util.ArrayList;
import java.util.Properties;

public class Coreferencer {

	private static  String sourceSentence = "Thomas, J., filed a dissenting opinion, " +
			"in which Alito, J., joined except as to Part I. Gorsuch, J., took no part in the consideration or decision of the case.";
	private static String targetSentence = "Roberts, C. J., delivered the opinion of the Court, in which " +
			"Kennedy, Ginsburg, Breyer, Sotomayor, and Kagan, JJ., joined.";
	public static ArrayList<String> getCoreferencedSentences(String sourceSentence, String targetSentence, NLPUtils nlpUtils) {
		ArrayList<String> twoSentences = new ArrayList<>();
		// text to resolve coreferences
		String corefText = targetSentence + " " + sourceSentence;

		// annotate both sentences in order to resolve coreferences
		Annotation annotation = nlpUtils.annotate(corefText);
		ArrayList<String> resolvedSents = nlpUtils.replaceCoreferencesNoHis(annotation);

		// coreferences replaced new sentences
		sourceSentence = resolvedSents.get(0);
		targetSentence = resolvedSents.get(1);
		twoSentences.add(sourceSentence);
		twoSentences.add(targetSentence);

		return twoSentences;
	}

	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props);

		ArrayList<String> coreferedSentences = getCoreferencedSentences(sourceSentence,targetSentence,nlpUtils);
		System.out.println(coreferedSentences.get(0));
		System.out.println(coreferedSentences.get(1));

	}
}
