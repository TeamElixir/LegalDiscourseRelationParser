import static org.slf4j.LoggerFactory.getLogger;

import java.sql.ResultSet;
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
import org.slf4j.Logger;
import svmmodel.DiscourseModel;
import utils.NLPUtils;
import utils.SQLiteUtils;

public class CalLegalType {

	private static final Logger logger = getLogger(CalLegalType.class);

	public static void main(String[] args) throws Exception {

		// sql to take sentences in db
		String sql = "SELECT * FROM LEGAL_SENTENCE;";

		// executes sql and fills up the array list
		SQLiteUtils sqLiteUtils = new SQLiteUtils();
		ResultSet resultSet = sqLiteUtils.executeQuery(sql);
		ArrayList<String> sentences = new ArrayList<>();
		while (resultSet.next()) {
			sentences.add(resultSet.getString("SENTENCE"));
		}
		logger.info(sentences.size() + " sentences fetch from the database.");

		// TODO: 5/4/18 remove "No. 16-327, Pp. 5-13., Id., at 236, 244...." like from sentences

		FeatureEntry featureEntry;

		svm_model model = svm.svm_load_model(
				"/home/thejan/FYP/LegalDisourseRelationParser/sentence-feature-extractor/discourseModel.txt");

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props);

		ArrayList<FeatureEntry> entries = new ArrayList<>();

		DiscourseAPI discourseAPI = new DiscourseAPI();

		for (int i = 0; i < sentences.size(); i++) {

			if (!Citation.checkCitation(sentences.get(i))) {

				featureEntry = discourseAPI.getFeatures(sentences.get(i + 1), sentences.get(i), nlpUtils);
				if (Citation.checkCitation(sentences.get(i + 1))) {
					featureEntry.setType(7);
				} else {
					featureEntry.setType((int) discourseAPI.getType(featureEntry, model));
				}
				featureEntry.setSsid(i + 2);
				featureEntry.setTsid(i + 1);
				logger.info((i + 1) + " and " + (i + 2) + " relation calculated");
				featureEntry.saveLegal();

				if (i <= sentences.size() - 2) {
					featureEntry = discourseAPI.getFeatures(sentences.get(i + 2), sentences.get(i), nlpUtils);
					featureEntry.setType((int) discourseAPI.getType(featureEntry, model));
					featureEntry.setSsid(i + 3);
					featureEntry.setTsid(i + 1);
					logger.info((i + 1) + " and " + (i + 3) + " relation calculated");
					featureEntry.saveLegal();
				}
			}
		}

		// for 5 up and 5 down
		// 50 sentences first
		for (int i = 0; i < 51; i++) {
			for (int j = 1; j < 6; j++) {
				if ((i - j) >= 0) {
					featureEntry = discourseAPI.getFeatures(sentences.get(i), sentences.get(i - j), nlpUtils);
					featureEntry.setType((int) discourseAPI.getType(featureEntry, model));
					featureEntry.setSsid(i + 1);
					featureEntry.setTsid(i - j + 1);
					featureEntry.saveLegal();
				}

				if ((i + j) < 51) {
					featureEntry = discourseAPI.getFeatures(sentences.get(i + j), sentences.get(i), nlpUtils);
					featureEntry.setType((int) discourseAPI.getType(featureEntry, model));
					featureEntry.setSsid(i + j + 1);
					featureEntry.setTsid(i + 1);
					featureEntry.saveLegal();
				}
			}
			System.out.println(i + "done");
		}


		/*
		try{
			FileOutputStream fos= new FileOutputStream("legalfeatureentry");
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(entries);
			oos.close();
			fos.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}


		ArrayList<FeatureEntry> arraylist= new ArrayList<>();
		try
		{
			FileInputStream fis = new FileInputStream("legalfeatureentry");
			ObjectInputStream ois = new ObjectInputStream(fis);
			arraylist = (ArrayList) ois.readObject();
			ois.close();
			fis.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(ClassNotFoundException c){
			System.out.println("Class not found");
			c.printStackTrace();
		}
		for(FeatureEntry tmp: arraylist){
			System.out.println(tmp);
			tmp.saveLegal();
		}
		*/
	}
	/*
	public static void main(String[] args) throws IOException {
		svm_model model = svm.svm_load_model(
				"/media/gathika/Other/repos/LegalDisourseRelationParser/sentence-feature-extractor/discourseModel.txt");

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props);
		CalLegalType calLegalType = new CalLegalType();
		FeatureEntry featureEntry = calLegalType.getFeatures("plane is in the airport.",

				"children are playing cricket.",nlpUtils);
		System.out.println("semScore : "+featureEntry.getSemanticSimilarityScore());
		double type = calLegalType.getType(featureEntry,model);
		System.out.println(type);
	}
	*/

}
