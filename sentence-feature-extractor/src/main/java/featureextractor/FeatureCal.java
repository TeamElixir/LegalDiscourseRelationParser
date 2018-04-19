package featureextractor;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.Properties;

import datasetparser.models.Relationship;
import edu.stanford.nlp.pipeline.Annotation;
import org.slf4j.Logger;
import utils.NLPUtils;

public class FeatureCal {

	private static final Logger logger = getLogger(FeatureCal.class);

	public static void main(String[] args) throws Exception {

		ArrayList<Relationship> relationships = Relationship.getAll();

		logger.info(relationships.size() + " relationships fetch from the database.");

		Properties props = new Properties();
		props.setProperty("annotators","tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props, "http://corenlp.run", 80, 8);
//		NLPUtils nlpUtils = new NLPUtils(props);


		// iterating through all the relationships
		for (Relationship relationship : relationships) {

			String sourceSentence = relationship.getSourceSent();
			String targetSentence = relationship.getTargetSent();

//			System.out.println(sourceSentence);

			String corefText = targetSentence + " " + sourceSentence;
//
			Annotation annotation = nlpUtils.annotate(corefText);
			ArrayList<String> sents = nlpUtils.replaceCoreferences(annotation, sourceSentence, targetSentence);
			System.out.println(sents.toString());
		}

	}

}
