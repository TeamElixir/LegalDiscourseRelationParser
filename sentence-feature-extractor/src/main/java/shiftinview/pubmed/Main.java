package shiftinview.pubmed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import datasetparser.models.FeatureEntry;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.util.WordNetUtil;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.pipeline.Annotation;
import shiftinview.CombinedShiftDetector;
import shiftinview.ConstituentParser;
import shiftinview.models.ShiftInViewPair;
import shiftinview.pubmed.ollieparser.OllieParser;
import shiftinview.pubmed.ollieparser.OllieSentence;
import utils.NLPUtils;
import utils.SQLiteUtils;
import utils.models.Triple;

public class Main {

	public static void main(String[] args) throws Exception {


		String sentence1 = "Although he has lived in this country for most of his life, Lee is not a United States citizen, and he feared that a criminal conviction might affect his status as a lawful permanent resident.";

		HashMap<String, Double> dictionary = DictionaryCreator.readDic();

		String sentence2
				= "His attorney assured him there was nothing to worry about,the Government would not deport him if he pleaded guilty.";

		ArrayList<FeatureEntry> legalEntries = FeatureEntry.getElaborationLegal();

		OllieParser ollieParser = new OllieParser();
		ArrayList<OllieSentence> tripleSentences = ollieParser.parse();

		TripleAnalyzer tripleAnalyzer = new TripleAnalyzer();

		int j = 0;
		for (int i = 0; i < tripleSentences.size(); i += 2) {
			if (!(tripleSentences.get(i).arrayList.isEmpty() || tripleSentences.get(i + 1).arrayList.isEmpty())) {

				ArrayList<Triple> sourceTriples = tripleSentences.get(i).arrayList;
				ArrayList<Triple> targetTriples = tripleSentences.get(i + 1).arrayList;

				Double val = tripleAnalyzer.analyze(sourceTriples, targetTriples);

				if (val != null) {
					System.out.println("Source : " + tripleSentences.get(i).text);
					System.out.println("Target : " + tripleSentences.get(i + 1).text);
				}
			} else {

			}
			j++;
		}

		System.out.println("done");


	}

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

		try (PrintWriter writer = new PrintWriter("coreferedSentencesNoHis.txt", "UTF-8")) {
			for (FeatureEntry entry : legalEntries) {
				String targetSentence = sentences.get(entry.getTsid() - 1);
				String sourceSentence = sentences.get(entry.getSsid() - 1);

				// text to resolve coreferences
				String corefText = targetSentence + " " + sourceSentence;

				// annotate both sentences in order to resolve coreferences
				Annotation annotation = nlpUtils.annotate(corefText);
				System.out.println("target: " + entry.getTsid() + " ; source" + entry.getSsid());
				ArrayList<String> resolvedSents = nlpUtils.replaceCoreferencesNoHis(annotation);

				// coreferences replaced new sentences
				if (resolvedSents != null) {
					sourceSentence = resolvedSents.get(0);
					targetSentence = resolvedSents.get(1);
				}

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

}
