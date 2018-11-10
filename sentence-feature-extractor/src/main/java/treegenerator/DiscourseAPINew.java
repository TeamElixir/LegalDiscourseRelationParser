package treegenerator;

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
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity2;
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
import utils.models.CombinedFeatureEntry;
import utils.models.NoRelation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class DiscourseAPINew {

	//private static final String svmModelPath = "/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/discourseModel.txt";
	private static final String svmModelPath ="G:\\repos\\ldrp\\LegalDisourseRelationParser\\sentence-feature-extractor\\discourseModel.txt";
	private static NLPUtils nlpUtils;

	private static svm_model model;

	public static void main(String[] args) throws Exception {
		/*model = svm.svm_load_model(svmModelPath);

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,coref");
		props.setProperty("coref.algorithm", "statistical");
		nlpUtils = new NLPUtils(props, "http://104.248.226.230", 9000);

		/*String targetSent = "Lee could not show that he was prejudiced by his attorney’s "
				+ "erroneous advice.";

		String sourceSent = "Lee has demonstrated that he was prejudiced by his "
				+ "counsel’s erroneous advice.";*/

		/*String targetSent="\n" +
				"Cat go after rat.";
		String sourceSent=
				"Dog is the best friend of human.";

		DiscourseAPINew discourseAPI = new DiscourseAPINew();*/



		//System.out.println("final value " + discourseAPI.getDiscourseType(sourceSent, targetSent));
	}

	public int getDiscourseType(String sourceSentence, String targetSentence,NLPUtils nlpUtils) throws IOException {
		model = svm.svm_load_model(svmModelPath);
		this.nlpUtils=nlpUtils;
		int originalRelationshipType;
		int finalRelationshipType;

		NoRelation noRelation=new NoRelation();
		noRelation.setLengths(sourceSentence,targetSentence);

		if (sourceSentence.equals(targetSentence)) {
			originalRelationshipType = 1;
			return originalRelationshipType;
		} else if (!Citation.checkCitation(targetSentence)) {

			CombinedFeatureEntry combinedFeatureEntry=getFeaturesNew(sourceSentence,targetSentence,
					nlpUtils,noRelation);
			//FeatureEntry featureEntry = getFeatures(sourceSentence, targetSentence, nlpUtils);
			FeatureEntry featureEntry=combinedFeatureEntry.featureEntry;
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

			//no relation
			//we can include relationship type 5(shift in view) here too
			if(finalRelationshipType==2){
				//can add  another condition - sum of legths<25
				if(noRelation.sourceLength<10 && noRelation.targetLength<10){
					if(!noRelation.Transition){
						if(noRelation.overlapRatio<0.02){
							//if want can increase overlap ratio and give different similarity value here
							finalRelationshipType=1;
						}else if(noRelation.SemanticSimilarityNew<0.55){
							finalRelationshipType=1;
						}
					}
				}
			}



			System.out.println();
			return finalRelationshipType;
		}else {
			return 1;
		}

		//return -1;
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




		Annotation sourceAnnotation1 = nlpUtils.annotate(sourceSentence);
		Annotation targetAnnotation1 = nlpUtils.annotate(targetSentence);
		SemanticSentenceSimilarity2 semanticSentenceSimilarity2 = new SemanticSentenceSimilarity2(sourceAnnotation1,
				targetAnnotation1, nlpUtils);

		System.out.println("semNew"+ (semanticSentenceSimilarity2.getAverageScore()));

		SemanticSentenceSimilarity semanticSentenceSimilarity3 = new SemanticSentenceSimilarity(sourceAnnotation1,
				targetAnnotation1, nlpUtils);

		System.out.println("semPrev :"+semanticSentenceSimilarity3.getAverageScore());



		// coreferences replaced new sentences
		System.out.println("aaaa");




		if(resolvedSents!=null) {
			sourceSentence = resolvedSents.get(0);
			targetSentence = resolvedSents.get(1);
		}

		ArrayList<Double> overlapWordRatios2 = overlapWordRatio.getOverlapScore(sourceSentence, targetSentence);
		System.out.println("sentences after coref");
		System.out.println(sourceSentence);
		System.out.println(targetSentence);
		System.out.println("overlap: "+overlapWordRatios2.get(1));

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

		System.out.println("semPrevAfterCoref :"+semanticSentenceSimilarity.getAverageScore());

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

	public CombinedFeatureEntry getFeaturesNew(String sourceSentence, String targetSentence, NLPUtils nlpUtils,
															NoRelation noRelation) {
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

		//No relation
		ArrayList<Double> filteredOverlapWordRatios = overlapWordRatio.getRefinedOverlapScore(sourceSentence, targetSentence);
		noRelation.overlapRatio=filteredOverlapWordRatios.get(1);

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

		//no relation
		if(trWords.ellaborationScore()>0 || trWords.changeScore()>0){
			System.out.println("Transition found");
			noRelation.Transition=true;
		}


		Annotation sourceAnnotation1 = nlpUtils.annotate(sourceSentence);
		Annotation targetAnnotation1 = nlpUtils.annotate(targetSentence);

		ArrayList<String> proNouns = nlpUtils.getProNouns(sourceAnnotation1);

		if(proNouns.size()>0){
			noRelation.Pronoun=true;
		}

		SemanticSentenceSimilarity2 semanticSentenceSimilarity2 = new SemanticSentenceSimilarity2(sourceAnnotation1,
				targetAnnotation1, nlpUtils);

		System.out.println("semNew"+ (semanticSentenceSimilarity2.getAverageScore()));

		SemanticSentenceSimilarity semanticSentenceSimilarity3 = new SemanticSentenceSimilarity(sourceAnnotation1,
				targetAnnotation1, nlpUtils);

		System.out.println("semPrev :"+semanticSentenceSimilarity3.getAverageScore());

		//noRelation
		noRelation.SemanticSimilarityNew=semanticSentenceSimilarity2.getAverageScore();
		noRelation.SemanticSimilarityPrev=semanticSentenceSimilarity3.getAverageScore();



		// text to resolve coreferences
		String corefText = targetSentence + " " + sourceSentence;

		// annotate both sentences in order to resolve coreferences
		Annotation annotation = nlpUtils.annotate(corefText);
		ArrayList<String> resolvedSents = nlpUtils.replaceCoreferences(annotation);





		// coreferences replaced new sentences
		System.out.println("aaaa");




		if(resolvedSents!=null) {
			sourceSentence = resolvedSents.get(0);
			targetSentence = resolvedSents.get(1);
		}

		ArrayList<Double> filteredOverlapWordRatios2 = overlapWordRatio.getRefinedOverlapScore(sourceSentence, targetSentence);
		System.out.println("sentences after coref");
		System.out.println(sourceSentence);
		System.out.println(targetSentence);
		System.out.println("overlap: "+filteredOverlapWordRatios2.get(1));

		//noRelation
		if(noRelation.Pronoun){
			noRelation.overlapRatio=filteredOverlapWordRatios2.get(1);
		}

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

		System.out.println("semPrevAfterCoref :"+semanticSentenceSimilarity.getAverageScore());

		SemanticSentenceSimilarity2 semanticSentenceSimilarity4 = new SemanticSentenceSimilarity2(sourceAnnotation,
				targetAnnotation, nlpUtils);

		System.out.println("semNewAfterCoref"+ (semanticSentenceSimilarity4.getAverageScore()));

		if(noRelation.Pronoun){
			noRelation.SemanticSimilarityPrev=semanticSentenceSimilarity.getAverageScore();
			noRelation.SemanticSimilarityNew=semanticSentenceSimilarity4.getAverageScore();
		}

		CombinedFeatureEntry combinedFeatureEntry= new CombinedFeatureEntry();
		combinedFeatureEntry.featureEntry=featureEntry;
		combinedFeatureEntry.noRelation=noRelation;
		return combinedFeatureEntry;
	}


}
