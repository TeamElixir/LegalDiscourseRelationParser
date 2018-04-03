package datasetparser;

import java.util.ArrayList;

import datasetparser.models.RelationshipEntry;
import datasetparser.models.SentenceEntry;
import datasetparser.parsers.DocsentParser;
import datasetparser.parsers.JudgedataParser;
import datasetparser.utils.SQLiteUtils;

public class Run {

	public static void main(String[] args) throws Exception {

		// Takes sentence entries from xml and save to db

		DocsentParser docsentParser = new DocsentParser();
		ArrayList<SentenceEntry> sentenceEntries = docsentParser.parseFolder("mds");
		sentenceEntries.addAll(docsentParser.parseFolder("gulfair11"));
		sentenceEntries.addAll(docsentParser.parseFolder("milan9"));

		for(SentenceEntry entry:sentenceEntries){
			entry.save();
		}


		// Takes relationship entries from xml and save to db
		/*
		JudgedataParser judgedataParser = new JudgedataParser();
		ArrayList<RelationshipEntry> relationshipEntries = judgedataParser.parseFolder("mds");
		relationshipEntries.addAll(judgedataParser.parseFolder("gulfair11"));
		relationshipEntries.addAll(judgedataParser.parseFolder("milan9"));

		for(RelationshipEntry entry:relationshipEntries){
			entry.save();
		}
		*/

//		SQLiteUtils sqLiteUtils = new SQLiteUtils();
//		String sql = "CREATE TABLE SENTENCE_ENTRY " +
//				"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//				" SNO       INT     NOT NULL, " +
//				" SENT      TEXT        NOT NULL, " +
//				" DID       CHAR(2)     NOT NULL, " +
//				" SOURCE    CHAR(20)    NOT NULL )";
//		sqLiteUtils.executeUpdate(sql);

//		SentenceEntry.getEntry("E7",9);

		System.out.println();


	}

}
