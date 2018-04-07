package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

public class NLPUtils {

	private StanfordCoreNLP pipeline;

	public NLPUtils(String annotatorList){
		Properties props = new Properties();
		props.setProperty("annotators", annotatorList);
		this.pipeline = new StanfordCoreNLP(props);
	}

	public Annotation annotate(String text){
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);

		return annotation;
	}

	// TODO: 4/7/18 same text annotated again and again when calling getNouns,getVerbs etc

	public ArrayList<String> getNouns(String text){
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);

		ArrayList<String> nouns = new ArrayList<String>();

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String word = token.get(CoreAnnotations.TextAnnotation.class);
				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

				// proper nouns and pronouns are considered
				if("NN".equals(pos) || "NNS".equals(pos) || "NNP".equals(pos) ||
						"NNPS".equals(pos) || "PRP".equals(pos) || "PRP$".equals(pos)){
					nouns.add(word.toLowerCase());
				}
			}
		}

		return nouns;
	}

	public ArrayList<String> getVerbs(String text){
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);

		ArrayList<String> verbs = new ArrayList<String>();

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String word = token.get(CoreAnnotations.TextAnnotation.class);
				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

				// "be" verbs are considered
				if("VB".equals(pos) || "VBD".equals(pos) || "VBG".equals(pos) ||
						"VBN".equals(pos) || "VBP".equals(pos) || "VBZ".equals(pos)){
					verbs.add(word.toLowerCase());
				}
			}
		}

		return verbs;
	}

	public ArrayList<String> getVerbsWithOutBe(String text){
		ArrayList<String> verbsWithOutBe = new ArrayList<String>();

		ArrayList<String> allVerbs = getVerbs(text);

		// TODO: 4/5/18 change variable location
		ArrayList<String> present =new ArrayList<String>(Arrays.asList(
				"be", "is", "are", "am", "being","has","have","do","does"));
		ArrayList<String> past =new ArrayList<String>(Arrays.asList(
				"was", "were", "been","would","should","did"));
		ArrayList<String> future =new ArrayList<String>(Arrays.asList("will", "shall"));

		for(String verb:allVerbs){
			String verbLowerCase = verb.toLowerCase();
			if(present.contains(verbLowerCase) || past.contains(verbLowerCase) || future.contains(verbLowerCase)){
				continue;
			}else {
				verbsWithOutBe.add(verbLowerCase);
			}
		}

		return verbsWithOutBe;
	}

	public ArrayList<String> getAdjectives(String text){
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);

		ArrayList<String> adjectives = new ArrayList<String>();

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String word = token.get(CoreAnnotations.TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

				if("JJ".equals(pos) || "JJR".equals(pos) || "JJS".equals(pos)){
					adjectives.add(word.toLowerCase());
				}
			}
		}

		return adjectives;
	}

	public ArrayList<String> getSubjects(String text){
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);

		ArrayList<String> subjects = new ArrayList<String>();

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for(CoreMap sentence:sentences){
			SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);
//			IndexedWord root = dependencies.getFirstRoot();
//			System.out.printf("root(ROOT-0, %s-%d)%n", root.word(), root.index());
			for(SemanticGraphEdge edge:dependencies.edgeIterable()){
//				System.out.printf ("%s(%s-%d, %s-%d)%n", edge.getRelation().toString(), edge.getGovernor().word(), edge.getGovernor().index(), edge.getDependent().word(), edge.getDependent().index());
				String relation = edge.getRelation().toString();
				// nominal subject, passive nominal subject, controlling subject considered
				// clausal subjects not considered
				if("nsubj".equals(relation) || "nsubjpass".equals(relation) ||
						"nsubj:xsubj".equals(relation) || "nsubjpass:xsubj".equals(relation)){
					subjects.add(edge.getDependent().word().toLowerCase());
				}
			}
		}

		return subjects;
	}

	public ArrayList<String> getObjects(String text){
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);

		ArrayList<String> objects = new ArrayList<String>();

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for(CoreMap sentence:sentences){
			SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);
			for(SemanticGraphEdge edge:dependencies.edgeIterable()){
				String relation = edge.getRelation().toString();
				// direct object, indirect object considered
				// prepositional object not considered
				if("dobj".equals(relation) || "iobj".equals(relation)){
					objects.add(edge.getDependent().word().toLowerCase());
				}
			}
		}

		return objects;
	}

	public StanfordCoreNLP getPipeline() {
		return pipeline;
	}
}
