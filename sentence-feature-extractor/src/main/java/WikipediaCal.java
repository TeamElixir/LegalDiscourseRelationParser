import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import datasetparser.models.FeatureEntry;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import libsvm.svm;
import libsvm.svm_model;
import org.slf4j.Logger;
import utils.NLPUtils;
import utils.SQLiteUtils;

public class WikipediaCal {

	private static final Logger logger = getLogger(WikipediaCal.class);

	public static String readFile(String fileName) throws Exception{
		BufferedReader br;
		br = new BufferedReader(new FileReader(fileName));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		String everything = sb.toString();

		return everything;
	}

	public static void fillSentenceTable() throws Exception{
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		String globalFilePath = new File("").getAbsolutePath();
		globalFilePath += "/wikipedia-europe.txt";

		String textRaw = readFile(globalFilePath);

		String[] splittedParagraphs = textRaw.split("\n");

		SQLiteUtils sqLiteUtils = new SQLiteUtils();

		for (String text : splittedParagraphs) {
			// create an empty Annotation just with the given text
			Annotation document = new Annotation(text);

			// run all Annotators on this text
			pipeline.annotate(document);

			// these are all the sentences in this document
			// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
			List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

			for (CoreMap sentence : sentences) {
				String sentenceString = sentence.toString();
				sentenceString = sentenceString.replaceAll("\"","'");
				String sql = "INSERT INTO WIKIPEDIA_SENTENCE (SENTENCE) VALUES ("+"\""+sentenceString+"\""+");";
				System.out.println(sql);
				sqLiteUtils.executeUpdate(sql);
			}   // for each sentence
		}
	}

	public static void calFeatureType() throws Exception{
		// sql to take sentences only from 1st case in db
		String sql = "SELECT * FROM WIKIPEDIA_SENTENCE;";

		// executes sql and fills up the array list
		SQLiteUtils sqLiteUtils = new SQLiteUtils();
		ResultSet resultSet = sqLiteUtils.executeQuery(sql);
		ArrayList<String> sentences = new ArrayList<>();
		while (resultSet.next()) {
			sentences.add(resultSet.getString("SENTENCE"));
		}
		logger.info(sentences.size() + " sentences fetch from the database.");

		FeatureEntry featureEntry;
		CalLegalType calLegalType = new CalLegalType();

		svm_model model = svm.svm_load_model(
				"/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/discourseModel.txt");

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props);

		ArrayList<FeatureEntry> entries = new ArrayList<>();

		// for 5 up and 5 down
		for (int i = 0; i < 101; i++) {
			for (int j = 1; j < 6; j++) {
				if ((i - j) >= 0) {
					featureEntry = calLegalType.getFeatures(sentences.get(i), sentences.get(i - j), nlpUtils);
					featureEntry.setType((int) calLegalType.getType(featureEntry, model));
					featureEntry.setSsid(i + 1);
					featureEntry.setTsid(i - j + 1);
					featureEntry.saveWiki();
					logger.info(featureEntry.getSsid() + " + " + featureEntry.getTsid());
				}

				if ((i + j) < 101) {
					featureEntry = calLegalType.getFeatures(sentences.get(i + j), sentences.get(i), nlpUtils);
					featureEntry.setType((int) calLegalType.getType(featureEntry, model));
					featureEntry.setSsid(i + j + 1);
					featureEntry.setTsid(i + 1);
					featureEntry.saveWiki();
					logger.info(featureEntry.getSsid() + " + " + featureEntry.getTsid());
				}
			}
			logger.info( i+1 + " completed.");
		}

	}

	public static void main(String[] args) throws Exception{
//		fillSentenceTable();

		calFeatureType();
	}


}
