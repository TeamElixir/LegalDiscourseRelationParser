package featureextractor;

import java.util.ArrayList;

import datasetparser.models.FeatureEntryDB;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.*;

public class FeatureValues {
    private ArrayList<Double> feature1List = new ArrayList<>();
    private ArrayList<Double> feature2List = new ArrayList<>();

    private Double[] feature1Array;
    private Double[] feature2Array;

    public void initializeArrays(){

        feature1Array = feature1List.toArray(new Double[feature1List.size()]);
        feature2Array = feature2List.toArray(new Double[feature2List.size()]);
    }

    public double calculateCorrelation(){
        initializeArrays();
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double cc = pc.correlation(ArrayUtils.toPrimitive(feature1Array),
                ArrayUtils.toPrimitive(feature2Array));

		return cc;
    }

    public void setFeature1List(ArrayList<Double> feature1List) {
        this.feature1List = feature1List;
    }

    public void setFeature2List(ArrayList<Double> feature2List) {
        this.feature2List = feature2List;
    }

    public ArrayList<Double> getFeature1List() {
        return feature1List;
    }

    public ArrayList<Double> getFeature2List() {
        return feature2List;
    }

	public static void main(String[] args) throws Exception{
		ArrayList<FeatureEntryDB> featureEntryDBS = FeatureEntryDB.getAll();

		ArrayList<Double> feature1 = new ArrayList<>();
		ArrayList<Double> feature2 = new ArrayList<>();

		for(FeatureEntryDB entry: featureEntryDBS){
			feature1.add(entry.getWordSimilarity());
			feature2.add(entry.getSemanticSimilarityScore());
		}

		FeatureValues featureValues = new FeatureValues();
		featureValues.setFeature1List(feature1);
		featureValues.setFeature2List(feature2);

		System.out.println("Pearson\'s coefficient of correlation: " + featureValues.calculateCorrelation());

	}
}
