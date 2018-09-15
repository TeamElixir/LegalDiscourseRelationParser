package shiftinview.pubmed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URLClassLoader;

import edu.knowitall.ollie.Ollie;
import edu.knowitall.ollie.OllieExtraction;
import edu.knowitall.ollie.OllieExtractionInstance;
import edu.knowitall.tool.parse.MaltParser;
import edu.knowitall.tool.parse.graph.DependencyGraph;
import edu.knowitall.tool.postag.OpenNlpPostagger;
import edu.knowitall.tool.postag.StanfordPostagger;

/**
 * This is an example class that shows one way of using Ollie from Java.
 */
public class OllieTest {

	// the extractor itself
	private Ollie ollie;

	// the parser--a step required before the extractor
	private MaltParser maltParser;

	// the path of the malt parser model file
	private static final String MALT_PARSER_FILENAME = "engmalt.linear-1.7.mco";

	public OllieTest() throws MalformedURLException {
		File file = new File(MALT_PARSER_FILENAME);
		System.out.println(file.getAbsolutePath());
		// initialize MaltParser
		scala.Option<File> nullOption = scala.Option.apply(null);
//		StanfordPostagger tag = new StanfordPostagger();
		OpenNlpPostagger tag = new OpenNlpPostagger();
		maltParser = new MaltParser(new File(MALT_PARSER_FILENAME).toURI().toURL(),tag, nullOption);		// initialize Ollie
		ollie = new Ollie();
	}

	/**
	 * Gets Ollie extractions from a single sentence.
	 *
	 * @param sentence
	 * @return the set of ollie extractions
	 */
	public Iterable<OllieExtractionInstance> extract(String sentence) {
		// parse the sentence
		DependencyGraph graph = maltParser.dependencyGraph(sentence);

		// run Ollie over the sentence and convert to a Java collection
		Iterable<OllieExtractionInstance> extrs = scala.collection.JavaConversions.asJavaIterable(ollie.extract(graph));
		return extrs;
	}

	public static void main(String args[]) throws Exception {


		 // initialize
		 OllieTest ollieWrapper = new OllieTest();

		 // extract from a single sentence.
		 String sentence = "President Obama will meet with Congressional leaders on Friday, and House Republicans summoned lawmakers back for a Sunday session, in a last-ditch effort to avert a fiscal crisis brought on by automatic tax increases and spending cuts scheduled to hit next week.";
		 Iterable<OllieExtractionInstance> extrs = ollieWrapper.extract(sentence);

		 // print the extractions.
		 for (OllieExtractionInstance inst : extrs) {
		 OllieExtraction extr = inst.extr();
		 System.out.println(extr.arg1().text() + "\t" + extr.rel().text() + "\t" + extr.arg2().text());
		 }


		/**
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "/home/thejan/FYP/Softwares/ollie/ollie-app-latest.jar");
		pb.redirectErrorStream(true);
		Process p = pb.start();

		InputStream in = p.getInputStream();
		OutputStreamWriter osw = new OutputStreamWriter(p.getOutputStream());
		BufferedWriter out = new BufferedWriter(osw);
		InputStreamReader ins = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(ins);

		String line;

		while ((line = br.readLine()) != null) {
			System.out.println(line);
			if(line.equals("Running extractor on standard input...")){
				break;
			}
		}

		System.out.println("hey");

		out.write("His attorney assured him there was nothing to worry about, the Government would not deport him if he pleaded guilty.");
		out.newLine();
		out.flush();

		System.out.println(br.readLine());
		System.out.println(br.readLine());
		System.out.println(br.readLine());
		System.out.println(br.readLine());

		p.waitFor();
		p.destroy();

//		URLClassLoader clr = new URLClassLoader()
		 **/

	}
}
