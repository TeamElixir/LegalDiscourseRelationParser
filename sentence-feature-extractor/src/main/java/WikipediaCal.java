import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import utils.SQLiteUtils;

public class WikipediaCal {

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

	public static void main(String[] args) throws Exception{
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

}
