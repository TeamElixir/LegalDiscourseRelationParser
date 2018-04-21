package featureextractor;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.*;

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
        initializeArrays();
        System.out.print("Pearson\'s coefficient of correlation: ");
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double cc = pc.correlation(ArrayUtils.toPrimitive(wordSimilarityArray),
                ArrayUtils.toPrimitive(semanticSimilarityArray));
        System.out.println(cc);

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
