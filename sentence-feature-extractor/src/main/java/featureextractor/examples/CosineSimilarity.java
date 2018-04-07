package featureextractor.examples;

import featureextractor.cosinesimilarity.AdjectiveSimilarity;
import featureextractor.cosinesimilarity.NounSimilarity;
import featureextractor.cosinesimilarity.Similarity;
import featureextractor.cosinesimilarity.VerbSimilarity;
import featureextractor.cosinesimilarity.WordSimilarity;
import utils.NLPUtils;

public class CosineSimilarity {

    public static void main(String[] args) {
        Similarity wordSimilarity = new WordSimilarity(
                "Julie loves me more than Linda loves me",
                "Jane likes me more than Julie loves me") ;
        double scoreWordsSimilarity = wordSimilarity.similarityScore();
        System.out.println( "wordSimilarityScore: "+ scoreWordsSimilarity);

        NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos");

        Similarity nounSimilarity = new NounSimilarity(
                "Julie loves me more than Linda loves me",
                "Jane likes me more than Julie loves me",
                nlpUtils) ;
        double scoreNounsSimilarity = nounSimilarity.similarityScore();
        System.out.println( "nounSimilarityScore: "+ scoreNounsSimilarity);

	    Similarity verbSimilarity = new VerbSimilarity(
			    "Julie loves me more than Linda loves me",
			    "Jane likes me more than Julie loves me",
			    nlpUtils) ;
	    double scoreVerbsSimilarity = verbSimilarity.similarityScore();
	    System.out.println( "verbSimilarityScore: "+ scoreVerbsSimilarity);

	    Similarity adjectiveSimilarity = new AdjectiveSimilarity(
			    "Julie loves me more than Linda loves me",
			    "Jane likes me more than Julie loves me",
			    nlpUtils) ;
	    double scoreAdjectivesSimilarity = adjectiveSimilarity.similarityScore();
	    System.out.println( "adjectiveSimilarityScore: "+ scoreAdjectivesSimilarity);

    }
}
