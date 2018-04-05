package featureextractor.examples;

import featureextractor.cosinesimilarity.Similarity;
import featureextractor.cosinesimilarity.WordSimilarity;

public class CosineWordSimilarity {

    public static void main(String[] args) {
        Similarity similarity = new WordSimilarity(
                "Julie loves me more than Linda loves me",
                "Jane likes me more than Julie loves me") ;
        double scoreWordsSimilarity = similarity.similarityScore();

        System.out.println( "wordSimilarityScore: "+ scoreWordsSimilarity);
    }
}
