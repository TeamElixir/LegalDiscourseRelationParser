package featureextractor;

import java.util.ArrayList;

public class FeatureValues {
    private ArrayList<Double> wordSimilarity = new ArrayList<>();
    private ArrayList<Double> semanticSimilarityScore = new ArrayList<>();

    private Double[] wordSimilarityArray ;
    private Double[] semanticSimilarityArray;

    public void initializeArrays(){
        wordSimilarityArray= wordSimilarity.toArray(new Double[wordSimilarity.size()]);
        semanticSimilarityArray = semanticSimilarityScore.toArray(new Double[semanticSimilarityScore.size()]);
    }

    public void calculateCoreference(){

    }


    public void setWordSimilarity(ArrayList<Double> wordSimilarity) {
        this.wordSimilarity = wordSimilarity;
    }

    public void setSemanticSimilarityScore(ArrayList<Double> semanticSimilarityScore) {
        this.semanticSimilarityScore = semanticSimilarityScore;
    }

    public ArrayList<Double> getWordSimilarity() {
        return wordSimilarity;
    }

    public ArrayList<Double> getSemanticSimilarityScore() {
        return semanticSimilarityScore;
    }
}
