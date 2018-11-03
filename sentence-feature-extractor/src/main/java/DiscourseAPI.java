import java.util.ArrayList;
import java.util.Properties;

import datasetparser.models.FeatureEntry;
import edu.stanford.nlp.pipeline.Annotation;
import featureextractor.cosinesimilarity.AdjectiveSimilarity;
import featureextractor.cosinesimilarity.NounSimilarity;
import featureextractor.cosinesimilarity.VerbSimilarity;
import featureextractor.cosinesimilarity.WordSimilarity;
import featureextractor.grammaticalrelationships.GrammarOverlapRatio;
import featureextractor.lexicalsimilarity.LongestCommonSubstring;
import featureextractor.lexicalsimilarity.OverlapWordRatio;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import featureextractor.sentenceproperties.Citation;
import featureextractor.sentencepropertyfeatures.NERRatio;
import featureextractor.sentencepropertyfeatures.SentenceLengths;
import featureextractor.sentencepropertyfeatures.TransitionalWords;
import featureextractor.sentencepropertyfeatures.TypeOfSpeech;
import libsvm.svm;
import libsvm.svm_model;
import shiftinview.ShiftInViewAnalyzer;
import svmmodel.DiscourseModel;
import utils.NLPUtils;

public class DiscourseAPI {

	//private static final String svmModelPath = "/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/discourseModel.txt";
	private static final String svmModelPath ="G:\\repos\\ldrp\\LegalDisourseRelationParser\\sentence-feature-extractor\\discourseModel.txt";
	private static NLPUtils nlpUtils;

	private static svm_model model;

	public static void main(String[] args) throws Exception {
		model = svm.svm_load_model(svmModelPath);

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,coref");
		props.setProperty("coref.algorithm", "statistical");
		nlpUtils = new NLPUtils(props, "http://104.248.226.230", 9000);

		String targetSent = "Lee could not show that he was prejudiced by his attorney’s "
				+ "erroneous advice.";

		String sourceSent = "Lee has demonstrated that he was prejudiced by his "
				+ "counsel’s erroneous advice.";

		DiscourseAPI discourseAPI = new DiscourseAPI();

		System.out.println("final value " + discourseAPI.getDiscourseType(sourceSent, targetSent));
	}

	public int getDiscourseType(String sourceSentence, String targetSentence) {

		int originalRelationshipType;
		int finalRelationshipType;

		if (sourceSentence.equals(targetSentence)) {
			originalRelationshipType = 1;
			return originalRelationshipType;
		} else if (!Citation.checkCitation(targetSentence)) {

			FeatureEntry featureEntry = getFeatures(sourceSentence, targetSentence, nlpUtils);
			if (Citation.checkCitation(sourceSentence)) {
				featureEntry.setType(7);
				originalRelationshipType = 7;
			} else {
				featureEntry.setType((int) getType(featureEntry, model));
				originalRelationshipType = (int) getType(featureEntry, model);
			}
			finalRelationshipType = getRelation(originalRelationshipType);

			if (finalRelationshipType == 2) {
				int value = ShiftInViewAnalyzer.checkRelationsForOppositeness(nlpUtils, targetSentence, sourceSentence);
				if (value > 0) {
					finalRelationshipType = 5;
				} else {
					value = ShiftInViewAnalyzer.checkRelationsForOppositeness(nlpUtils, sourceSentence, targetSentence);
					if (value > 0) {
						finalRelationshipType = 5;
					}
				}
			}
			return finalRelationshipType;
		}

		return -1;
	}

	public FeatureEntry getFeatures(String sourceSentence, String targetSentence, NLPUtils nlpUtils) {
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
		ArrayList<String> resolvedSents = nlpUtils.replaceCoreferences(annotation);

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

	public double getType(FeatureEntry featureEntry, svm_model model) {
		double[] arrayToEval = new double[FeatureEntry.FEATURE_NO + 1];
		double[] features = featureEntry.getFeatureArray();

		arrayToEval[0] = 1.0;
		System.arraycopy(features, 0, arrayToEval, 1, features.length);

		return DiscourseModel.evaluate(arrayToEval, model);
	}

	public static int getRelation(int no) {
		/** Elaboration **/
		if (no == 2 || no == 8 || no == 4 || no == 13 || no == 12 || no == 11 ||
				no == 18 || no == 14 || no == 15 || no == 6 || no == 16 || no == 9) {
			return 2;
			/** Redundancy **/
		} else if (no == 1) {
			return 3;
			/** Citation **/
		} else if (no == 7) {
			return 4;
			/** Shift in View **/
		} else if (no == 17 || no == 5) {
			return 5;
			/** No Relation **/
		} else {
			return 1;
		}
	}

}
