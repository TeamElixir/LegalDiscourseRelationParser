import static org.slf4j.LoggerFactory.getLogger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import datasetparser.models.FeatureEntry;
import datasetparser.models.Relationship;
import edu.stanford.nlp.pipeline.Annotation;
import featureextractor.FeatureCal;
import featureextractor.cosinesimilarity.AdjectiveSimilarity;
import featureextractor.cosinesimilarity.NounSimilarity;
import featureextractor.cosinesimilarity.VerbSimilarity;
import featureextractor.cosinesimilarity.WordSimilarity;
import featureextractor.grammaticalrelationships.GrammarOverlapRatio;
import featureextractor.lexicalsimilarity.LongestCommonSubstring;
import featureextractor.lexicalsimilarity.OverlapWordRatio;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import featureextractor.sentencepropertyfeatures.NERRatio;
import featureextractor.sentencepropertyfeatures.SentenceLengths;
import featureextractor.sentencepropertyfeatures.TransitionalWords;
import featureextractor.sentencepropertyfeatures.TypeOfSpeech;
import org.slf4j.Logger;
import utils.NLPUtils;
import utils.SQLiteUtils;

public class CalLegalType {

	private static final Logger logger = getLogger(CalLegalType.class);

	public static void main(String[] args) throws Exception {

		// sql to take sentences only from 1st case in db
		String sql = "SELECT * FROM LEGAL_SENTENCE WHERE CASE_FILE='criminal_triples/case_11.txt'";

		// executes sql and fills up the array list
		SQLiteUtils sqLiteUtils = new SQLiteUtils();
		ResultSet resultSet = sqLiteUtils.executeQuery(sql);
		ArrayList<String> sentences = new ArrayList<>();
		while (resultSet.next()) {
			sentences.add(resultSet.getString("SENTENCE"));
		}
		logger.info(sentences.size() + " sentences fetch from the database.");

		// TODO: 5/4/18 remove "No. 16-327, Pp. 5-13., Id., at 236, 244...." like from sentences

		String sourceSentence;
		String targetSentence;

		for (int i = 0; i < sentences.size(); i++) {
			for (int j = 1; j < 6; j++) {

			}
		}

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props);



		logger.info("All features calculated.");


		logger.info("All features entries saved");

	}

	public FeatureEntry getFeatures(String sourceSentence, String targetSentence, NLPUtils nlpUtils){
		// creates a FeatureEntry to hold all feature values
		FeatureEntry featureEntry = new FeatureEntry();

		// word cosine similarity
		WordSimilarity wordSimilarity = new WordSimilarity(sourceSentence, targetSentence);
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

		// annotate both sentences in order to resolve coreferences
		Annotation annotation = nlpUtils.annotate(corefText);
		ArrayList<String> resolvedSents = nlpUtils.replaceCoreferences(annotation, sourceSentence, targetSentence);

		// coreferences replaced new sentences
		sourceSentence = resolvedSents.get(0);
		targetSentence = resolvedSents.get(1);

		// annotated each for other features
		Annotation sourceAnnotation = nlpUtils.annotate(sourceSentence);
		Annotation targetAnnotation = nlpUtils.annotate(targetSentence);

		// noun cosine similarity
		NounSimilarity nounSimilarity = new NounSimilarity(sourceAnnotation, targetAnnotation, nlpUtils);
		featureEntry.setNounSimilarity(nounSimilarity.similarityScore());

		// verb cosine similarity
		VerbSimilarity verbSimilarity = new VerbSimilarity(sourceAnnotation, targetAnnotation, nlpUtils);
		featureEntry.setVerbSimilarity(verbSimilarity.similarityScore());

		// adjective cosine similarity
		AdjectiveSimilarity adjectiveSimilarity = new AdjectiveSimilarity(sourceAnnotation, targetAnnotation, nlpUtils);
		featureEntry.setAdjectiveSimilarity(adjectiveSimilarity.similarityScore());

		// ratio overlap of grammatical relationships
		GrammarOverlapRatio grammarOverlapRatio = new GrammarOverlapRatio(sourceAnnotation, targetAnnotation, nlpUtils);
		featureEntry.setSubjectOverlap(grammarOverlapRatio.getSubjectOverlap());
		featureEntry.setObjectOverlap(grammarOverlapRatio.getObjectOverlap());
		featureEntry.setSubjectNounOverlap(grammarOverlapRatio.getSubjectNounOverlap());

		// NER ratio
		NERRatio nerRatio = new NERRatio(sourceAnnotation, targetAnnotation, nlpUtils);
		featureEntry.setNerRatio(nerRatio.getRatio());

		// Semantic Similarity Score
		SemanticSentenceSimilarity semanticSentenceSimilarity = new SemanticSentenceSimilarity(sourceAnnotation,
				targetAnnotation, nlpUtils);
		featureEntry.setSemanticSimilarityScore(semanticSentenceSimilarity.getAverageScore());

		return featureEntry;
	}

	public double getType(FeatureEntry featureEntry){
		return 0.0;
	}

}
