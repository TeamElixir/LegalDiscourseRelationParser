package featureextractor;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import datasetparser.models.FeatureEntry;

public class FeatureRead {

	public static void main(String[] args) throws Exception{

		ArrayList<FeatureEntry> featureEntries = readFeatureEntryList();

		System.out.println("read features array");
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

}
