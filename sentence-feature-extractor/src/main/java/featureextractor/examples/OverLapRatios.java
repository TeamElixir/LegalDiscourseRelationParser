package featureextractor.examples;

import featureextractor.grammaticalrelationships.GrammarOverlapRatio;
import featureextractor.lexicalsimilarity.OverlapWordRatio;
import utils.NLPUtils;

import java.util.ArrayList;

public class OverLapRatios {

	public static void main(String[] args) {
		String sentence1 = "Julie loves me more than Linda loves me";
		String sentence2 = "Jane likes me more than Julie loves me";

		OverlapWordRatio overlapWordRatio = new OverlapWordRatio();
		ArrayList<Double> overlapScores = overlapWordRatio.getOverlapScore(sentence1, sentence2);
		System.out.println("Word overlapScores :" + overlapScores.toString());

		NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos,depparse");

		GrammarOverlapRatio grammarOverlapRatio = new GrammarOverlapRatio(sentence1, sentence2, nlpUtils);
		System.out.println("Subject Overlap Ratio (S1) : " + grammarOverlapRatio.getSubjectOverlap());
		System.out.println("Object Overlap Ratio (S1) : " + grammarOverlapRatio.getObjectOverlap());
		System.out.println("Subject Noun Overlap Ratio (S1) : " + grammarOverlapRatio.getSubjectNounOverlap());
	}
}
