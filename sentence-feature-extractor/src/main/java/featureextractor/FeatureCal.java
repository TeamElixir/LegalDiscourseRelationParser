package featureextractor;

import java.util.ArrayList;

import datasetparser.models.Relationship;

public class FeatureCal {

	public static void main(String[] args) throws Exception{

		ArrayList<Relationship> relationships = Relationship.getAll();
		System.out.println(relationships.size());

	}

}
