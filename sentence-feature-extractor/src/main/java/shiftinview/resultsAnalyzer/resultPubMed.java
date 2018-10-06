package shiftinview.resultsAnalyzer;

import java.io.PrintWriter;
import java.util.ArrayList;

import datasetparser.models.FeatureEntry;
import shiftinview.models.ShiftInViewPair;
import shiftinview.wuPalmerTest.models.Sentence;

public class resultPubMed {

	public static void main(String[] args) throws Exception {

		double threshold = 0.9;

		ArrayList<ShiftInViewPair> resultPairs = ShiftInViewPair.getPubMed(threshold);

		System.out.println("PAIR COUNT : " + resultPairs.size());
		System.out.println("----------------------------------------");

//		PrintWriter writer = new PrintWriter("pubMedResults_0.5.csv", "UTF-8");

		for (ShiftInViewPair resultPair : resultPairs) {
			FeatureEntry featureEntry = FeatureEntry.getFeatureEntryLegal(resultPair.getRelationshipId());

			String sourceSentence = Sentence.getSentenceDB(featureEntry.getSsid()).getSentence();
			String targetSentence = Sentence.getSentenceDB(featureEntry.getTsid()).getSentence();

			System.out.println("PUBMED ID : " + resultPair.getDbId());
			System.out.println("PAIR ID : " + resultPair.getRelationshipId());
			System.out.println("Source : " + sourceSentence);
			System.out.println("Target : " + targetSentence);
			System.out.println("----------------------------------------");
//			System.out.println(resultPair.getRelationshipId());

//			writer.println(resultPair.getRelationshipId() + "; \"\"\"" + sourceSentence + "\"\"\"; \"\"\"" + targetSentence + "\"\"\"");
		}

//		writer.close();
	}

}
