package datasetparser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;

import datasetparser.models.RelationshipEntry;
import datasetparser.models.SentenceEntry;
import datasetparser.parsers.DocsentParser;
import datasetparser.parsers.JudgedataParser;
import org.xml.sax.SAXException;

public class Run {

	public static void main(String[] args) throws Exception {
		DocsentParser parser = new DocsentParser();

		ArrayList<SentenceEntry> entries = parser.parseFolder("gulfair11");

		System.out.println();

		JudgedataParser judgedataParser = new JudgedataParser();
		ArrayList<RelationshipEntry> relationshipEntries = judgedataParser.parseFolder("mds");

		System.out.println();
	}

}
