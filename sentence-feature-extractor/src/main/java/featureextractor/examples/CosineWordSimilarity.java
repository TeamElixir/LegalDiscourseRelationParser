package featureextractor.examples;

import featureextractor.cosinesimilarity.WordSimilarity;

public class CosineWordSimilarity {

    public static void main(String[] args) {
        WordSimilarity wordSimilarity = new WordSimilarity();
        double scoreWordsSimilarity = wordSimilarity.cosine_word_similarity_score(
                "Julie loves me more than Linda loves me",
                "Jane likes me more than Julie loves me"
                );

        System.out.println( "wordSimilarityScore: "+ scoreWordsSimilarity);
    }
}
