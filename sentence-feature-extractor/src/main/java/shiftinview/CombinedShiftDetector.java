package shiftinview;

import SentimentAnnotator.ParseTreeSplitter;
import datasetparser.models.FeatureEntry;
import featureextractor.sentencepropertyfeatures.TransitionalWords;
import shiftinview.models.ShiftInViewPair;
import shiftinview.pubmed.ollieparser.OllieSentence;
import utils.NLPUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class CombinedShiftDetector {

	public static void main(String[] args) throws Exception {
		/*
		String targetSentence = "Lee contends that he can make this showing because he never would have " +
				"accepted a guilty plea had he known the result would be deportation.";

		String sourceSentence =
				"The Government contends that Lee cannot show prejudice from accepting a plea where his only hope at trial was that"
						+
						" something unexpected and unpredictable might occur that would lead to acquittal.";
		*/

		ArrayList<String> coreferedSentences = readCoreferedSentences();
		ArrayList<FeatureEntry> legalEntries = FeatureEntry.getElaborationLegal();

		// set up pipeline properties
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,sentiment");
		// use faster shift reduce parser
		props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
		props.setProperty("parse.maxlen", "100");

		NLPUtils nlpUtils = new NLPUtils(props);

		int j = 0;
		for (int i = 0; i < coreferedSentences.size(); i += 2) {
			String sourceSentence = coreferedSentences.get(i);
			String targetSentence = coreferedSentences.get(i + 1);

			ShiftInViewPair resultPair = new ShiftInViewPair();
			resultPair.setRelationshipId(legalEntries.get(j).getDbId());

			resultPair.setPubMedCal(0);
			resultPair.setPubMedVal(0);

			System.out.println("RID: " + resultPair.getRelationshipId());
			System.out.println("Source: " + sourceSentence);
			System.out.println("Target: " + targetSentence);

			TransitionalWords checkTransition = new TransitionalWords(sourceSentence);

			if (!(checkTransition.checkEllaborationShiftWords() || checkTransition.checkShiftEllaborationPhrase())) {
				int bValue = combinedDetector(nlpUtils, targetSentence, sourceSentence);
				if (bValue == 1) {
					System.out.println("final ::: Truee");
					resultPair.setLinShift(1);
				} else if (bValue == 2) {
					System.out.println("final - True 2");
					resultPair.setLinShift(2);
				} else {
					System.out.println("final ::: False");
					resultPair.setLinShift(0);
				}
			} else {
				System.out.println("final ::: False");
				resultPair.setLinShift(0);
			}

			j++;
			resultPair.save();
		}

	}

	public static Integer combinedDetector(NLPUtils nlpUtils, String targetSentence, String sourceSentence) {

		Integer value = ShiftInViewAnalyzer.checkRelationsForOppositeness(nlpUtils, targetSentence, sourceSentence);

		if (ParseTreeSplitter.subjectSentiment(nlpUtils, targetSentence, sourceSentence)) {
			return 1;
		} else if (value > 0) {
			return 2;
		}
		return 0;
	}

	public static ArrayList<String> readCoreferedSentences() throws Exception {
		String filePath = "/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/coreferedSentences.txt";

		Scanner sc = new Scanner(new File(filePath));

		ArrayList<String> sentenceList = new ArrayList<>();

		while (sc.hasNextLine()) {
			String nextLine = sc.nextLine();
			if (!(nextLine.length() == 0)) {
				sentenceList.add(nextLine);
			}
		}

		return sentenceList;
	}
}
