package featureextractor;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import datasetparser.models.FeatureEntry;
import datasetparser.models.FeatureEntryDB;

public class FeatureRead {

	public static void main(String[] args) throws Exception{

		ArrayList<FeatureEntry> featureEntries = readFeatureEntryList();

		System.out.println("read features array");

		/*ArrayList<FeatureEntryDB> featureEntryDBS = FeatureEntryDB.getAll();

		System.out.println("read features from db");*/

		//assignValuesToList();

		// read the serialized array and seed db
		/*
		for(FeatureEntry entry:featureEntries){
			FeatureEntryDB newEntry = new FeatureEntryDB();

			newEntry.setRelationshipId(entry.getRelationshipId());
			newEntry.setType(entry.getType());
			newEntry.setAdjectiveSimilarity(entry.getAdjectiveSimilarity());
			newEntry.setNounSimilarity(entry.getNounSimilarity());
			newEntry.setVerbSimilarity(entry.getVerbSimilarity());
			newEntry.setWordSimilarity(entry.getWordSimilarity());

			newEntry.setWordOverlapSSent(entry.getWordOverlapSSent());
			newEntry.setWordOverlapTSent(entry.getWordOverlapTSent());

			newEntry.setEllaborationTransitionScore(entry.getEllaborationTransitionScore());
			newEntry.setChangeTransitionScore(entry.getChangeTransitionScore());

			newEntry.setLcs(entry.getLcs());
			newEntry.setSubjectOverlap(entry.getSubjectOverlap());
			newEntry.setObjectOverlap(entry.getObjectOverlap());
			newEntry.setSubjectNounOverlap(entry.getSubjectNounOverlap());

			newEntry.setNerRatio(entry.getNerRatio());
			newEntry.setLengthRatio(entry.getLengthRatio());
			newEntry.setTosScore(entry.getTosScore());
			newEntry.setSemanticSimilarityScore(entry.getSemanticSimilarityScore());

			newEntry.save();
		}
		*/
	}

	private static ArrayList<FeatureEntry> readFeatureEntryList() throws Exception{
		ArrayList<FeatureEntry> arraylist = new ArrayList<>();

		FileInputStream fis = new FileInputStream("featurearrayfile");
		ObjectInputStream ois = new ObjectInputStream(fis);
		arraylist = (ArrayList) ois.readObject();
		ois.close();
		fis.close();

		return arraylist;
	}

	/*public static void assignValuesToList(){
		try {
			ArrayList<FeatureEntryDB> featureEntryDBS = FeatureEntryDB.getAll();
			FeatureValues featureValues = new FeatureValues();
			ArrayList<Double> wordSimilarity = featureValues.getWordSimilarity();
			ArrayList<Double> semanticSimilarity = featureValues.getSemanticSimilarityScore();
			for(FeatureEntryDB featureEntryDB:featureEntryDBS){
				wordSimilarity.add(featureEntryDB.getWordSimilarity());
				semanticSimilarity.add(featureEntryDB.getSemanticSimilarityScore());
			}
			System.out.println("read to lists");
			featureValues.calculateCoreference();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}*/

}
