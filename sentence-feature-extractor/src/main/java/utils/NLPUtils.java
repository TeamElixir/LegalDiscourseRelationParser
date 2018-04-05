package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class NLPUtils {

	private StanfordCoreNLP pipeline;

	public NLPUtils(String annotatorList){
		Properties props = new Properties();
		props.setProperty("annotators", annotatorList);
		this.pipeline = new StanfordCoreNLP(props);
	}

	public ArrayList<String> getNouns(String text){
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String word = token.get(CoreAnnotations.TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
				System.out.println(word + "/" + pos);
			}
		}

		return null;
	}

}
