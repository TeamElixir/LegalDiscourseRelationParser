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
import svmmodel.DiscourseModel;
import utils.NLPUtils;

public class DiscourseAPI {

	private static final String svmModelPath = "/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/discourseModel.txt";

	private static NLPUtils nlpUtils;

	private static svm_model model;

	public static void main(String[] args) throws Exception {
		model = svm.svm_load_model(svmModelPath);

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		nlpUtils = new NLPUtils(props, "http://104.248.226.230", 9000);
	}

	public int getDiscourseType(String sourceSentence, String targetSentence) {

		if(sourceSentence.equals(targetSentence)){
			return 1;
		}

		if (!Citation.checkCitation(sourceSentence) ){

			FeatureEntry featureEntry = getFeatures(sourceSentence, targetSentence, nlpUtils);
			if (Citation.checkCitation(targetSentence)) {
				featureEntry.setType(7);
			} else {
				featureEntry.setType((int) getType(featureEntry, model));
			}
		}

		return 0;
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

}
