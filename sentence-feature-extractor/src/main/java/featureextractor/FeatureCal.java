package featureextractor;

import static org.slf4j.LoggerFactory.getILoggerFactory;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.Properties;

import datasetparser.models.FeatureEntry;
import datasetparser.models.Relationship;
import edu.stanford.nlp.pipeline.Annotation;
import featureextractor.cosinesimilarity.WordSimilarity;
import featureextractor.lexicalsimilarity.LongestCommonSubstring;
import featureextractor.lexicalsimilarity.OverlapWordRatio;
import featureextractor.sentencepropertyfeatures.SentenceLengths;
import featureextractor.sentencepropertyfeatures.TransitionalWords;
import featureextractor.sentencepropertyfeatures.TypeOfSpeech;
import org.slf4j.Logger;
import utils.NLPUtils;

public class FeatureCal {

	private static final Logger logger = getLogger(FeatureCal.class);

	public static void main(String[] args) throws Exception {

		// takes all relationships consisting of sourcesent, targetsent and judgement
		ArrayList<Relationship> relationships = Relationship.getAll();

		logger.info(relationships.size() + " relationships fetch from the database.");

		String sourceSentence;
		String targetSentence;

		FeatureEntry featureEntry;
		ArrayList<FeatureEntry> featureEntries = new ArrayList<>();

		Properties props = new Properties();
		props.setProperty("annotators","tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		//		NLPUtils nlpUtils = new NLPUtils(props, "http://corenlp.run", 80, 8);
		NLPUtils nlpUtils = new NLPUtils(props);

		/**
		 *  This loop iterates through all the relationships and calculate all the
		 *  features based on WORDS.
		 */
		// iterating through all the relationships
		for (Relationship relationship : relationships) {
			// takes two sentences from the relationship
			sourceSentence = relationship.getSourceSent();
			targetSentence = relationship.getTargetSent();

			// creates a FeatureEntry to hold all feature values
			featureEntry = new FeatureEntry();

			// sets relationshipId (for the table reference)
			featureEntry.setRelationshipId(relationship.getDbId());

			// word cosine similarity
			WordSimilarity wordSimilarity = new WordSimilarity(sourceSentence, targetSentence) ;
			featureEntry.setWordSimilarity(wordSimilarity.similarityScore());

			// overlap ratio of words
			OverlapWordRatio overlapWordRatio = new OverlapWordRatio();
			ArrayList<Double> overlapWordRatios = overlapWordRatio.getOverlapScore(sourceSentence, targetSentence);
			featureEntry.setWordOverlapSSent(overlapWordRatios.get(0));
			featureEntry.setWordOverlapTSent(overlapWordRatios.get(1));

			// longest common substring
			LongestCommonSubstring lcs = new LongestCommonSubstring(sourceSentence, targetSentence);
			featureEntry.setLcs(lcs.lcsValueSentence1());

			// lengths of sentences
			SentenceLengths sentenceLengths = new SentenceLengths(sourceSentence, targetSentence);
			featureEntry.setLengthRatio(sentenceLengths.getLengthScore());

			// type of speech
			TypeOfSpeech typeOfSpeech = new TypeOfSpeech(sourceSentence, targetSentence);
			featureEntry.setTosScore(typeOfSpeech.getTOSScore());

			// transitional word
			TransitionalWords trWords = new TransitionalWords(sourceSentence);
			//check words that ellaborate
			featureEntry.setEllaborationTransitionScore(trWords.ellaborationScore());
			//check words that change the topic
			featureEntry.setChangeTransitionScore(trWords.changeScore());

			// text to resolve coreferences
			String corefText = targetSentence + " " + sourceSentence;

			Annotation annotation = nlpUtils.annotate(corefText);
			ArrayList<String> resolvedSents = nlpUtils.replaceCoreferences(annotation, sourceSentence, targetSentence);

			sourceSentence = resolvedSents.get(0);
			targetSentence = resolvedSents.get(1);

			Annotation sourceAnnotation = nlpUtils.annotate(sourceSentence);
			Annotation targetAnnotation = nlpUtils.annotate(targetSentence);



			// at the end
			featureEntries.add(featureEntry);
		}

		logger.info("Features based on words calculated.");

	}

}
