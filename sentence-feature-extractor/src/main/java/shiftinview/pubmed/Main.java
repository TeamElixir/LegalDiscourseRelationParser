package shiftinview.pubmed;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import datasetparser.models.FeatureEntry;
import edu.stanford.nlp.pipeline.Annotation;
import featureextractor.sentencepropertyfeatures.TransitionalWords;
import shiftinview.models.ShiftInViewPair;
import shiftinview.pubmed.ollieparser.OllieParser;
import shiftinview.pubmed.ollieparser.OllieSentence;
import utils.NLPUtils;
import utils.SQLiteUtils;
import utils.models.Triple;

public class Main {

	public static void main(String[] args) throws Exception{
		coreferAndWrite();
	}

	/*
	public static void main(String[] args) throws Exception {

		String sentence1 = "Although he has lived in this country for most of his life, Lee is not a United States citizen, and he feared that a criminal conviction might affect his status as a lawful permanent resident.";

		String sentence2
				= "His attorney assured him there was nothing to worry about,the Government would not deport him if he pleaded guilty.";

		ArrayList<FeatureEntry> legalEntries = FeatureEntry.getElaborationLegal();

		ArrayList<ShiftInViewPair> pairs = ShiftInViewPair.getAll();

		OllieParser ollieParser = new OllieParser();
		ArrayList<OllieSentence> tripleSentences = ollieParser.parse();

		TripleAnalyzer tripleAnalyzer = new TripleAnalyzer();

		System.out
				.println("------------------------------------------Read finished-----------------------------------------");

		int j = 0;
		for (int i = 0; i < tripleSentences.size(); i += 2) {
			// TODO: 10/4/18 change
			String sourceSentence = tripleSentences.get(i).text;
			String targetSentence = tripleSentences.get(i + 1).text;

			System.out.println("Source : " + sourceSentence);
			System.out.println("Target : " + targetSentence);

			ShiftInViewPair pair = pairs.get(j);

			TransitionalWords checkTransition = new TransitionalWords(sourceSentence);

			if (!(checkTransition.checkEllaborationShiftWords() || checkTransition.checkShiftEllaborationPhrase())) {

				if (!(tripleSentences.get(i).arrayList.isEmpty() || tripleSentences.get(i + 1).arrayList.isEmpty())) {

					ArrayList<Triple> sourceTriples = tripleSentences.get(i).arrayList;
					ArrayList<Triple> targetTriples = tripleSentences.get(i + 1).arrayList;

					//				System.out.println("Pair ID: " + );

					Double val = tripleAnalyzer.analyze(sourceTriples, targetTriples);

					if (val != null) {
						System.out.println(
								"-------------------------------------Calculated--------------------------------------------");
						pair.setPubMedCal(1);
						pair.setPubMedVal(val);
						System.out.println("Max SimDiff: " + val);
					} else {
						System.out.println(
								"------------------------------------Not Calculated-------------------------------------");
						pair.setPubMedCal(0);
						pair.setPubMedVal(0.0);
					}

				} else {
					System.out.println(
							"------------------------------------Not Calculated-------------------------------------");
					pair.setPubMedCal(0);
					pair.setPubMedVal(0.0);
				}
			} else {
				System.out.println(
						"------------------------------------Not Calculated-------------------------------------");
				pair.setPubMedCal(0);
				pair.setPubMedVal(0.0);
			}

			System.out.println(
					"----------------------------------------------Pair ENDS--------------------------------------------");

			pair.update();

			j++;
		}

		System.out.println("done");


		/* print all the original elaboration sentences to a file
		// take all elaboration type entries
		ArrayList<FeatureEntry> legalEntries = FeatureEntry.getElaborationLegal();

		// sql to take sentences in db
		String sql = "SELECT * FROM LEGAL_SENTENCE;";

		// executes sql and fills up the array list
		SQLiteUtils sqLiteUtils = new SQLiteUtils();
		ResultSet resultSet = sqLiteUtils.executeQuery(sql);
		ArrayList<String> sentences = new ArrayList<>();
		while (resultSet.next()) {
			sentences.add(resultSet.getString("SENTENCE"));
		}

		try (PrintWriter writer = new PrintWriter("originalSentences.txt", "UTF-8")) {
			for (FeatureEntry entry : legalEntries) {
				String targetSentence = sentences.get(entry.getTsid() - 1);
				String sourceSentence = sentences.get(entry.getSsid() - 1);

				writer.println(sourceSentence);
				writer.println(targetSentence);
				writer.println();
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
	*/

	public static void coreferAndWrite() throws Exception {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,natlog,openie,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props);

		// take all elaboration type entries
		ArrayList<FeatureEntry> legalEntries = FeatureEntry.getElaborationLegal();

		// sql to take sentences in db
		String sql = "SELECT * FROM LEGAL_SENTENCE;";

		// executes sql and fills up the array list
		SQLiteUtils sqLiteUtils = new SQLiteUtils();
		ResultSet resultSet = sqLiteUtils.executeQuery(sql);
		ArrayList<String> sentences = new ArrayList<>();
		while (resultSet.next()) {
			sentences.add(resultSet.getString("SENTENCE"));
		}

		try (PrintWriter writer = new PrintWriter("coreferedSentences.txt", "UTF-8")) {
			for (FeatureEntry entry : legalEntries) {
				String targetSentence = sentences.get(entry.getTsid() - 1);
				String sourceSentence = sentences.get(entry.getSsid() - 1);

				// text to resolve coreferences
				String corefText = targetSentence + " " + sourceSentence;

				// annotate both sentences in order to resolve coreferences
				Annotation annotation = nlpUtils.annotate(corefText);
				System.out.println("target: " + entry.getTsid() + " ; source" + entry.getSsid());
				ArrayList<String> resolvedSents = nlpUtils.replaceCoreferences(annotation);

				// coreferences replaced new sentences
				if (resolvedSents != null) {
					sourceSentence = resolvedSents.get(0);
					targetSentence = resolvedSents.get(1);
				}

				Annotation sourceAnnotation = nlpUtils.annotate(sourceSentence);
				Annotation targetAnnotation = nlpUtils.annotate(targetSentence);

				ArrayList<Triple> sourceTriples = nlpUtils.getTriples(sourceAnnotation);
				ArrayList<Triple> targetTriples = nlpUtils.getTriples(targetAnnotation);

				writer.println(sourceSentence);
				for (Triple triple : sourceTriples) {
					writer.println(triple.toString());
				}
				writer.println();
				writer.println(targetSentence);
				for (Triple triple : targetTriples) {
					writer.println(triple.toString());
				}
				writer.println();
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
