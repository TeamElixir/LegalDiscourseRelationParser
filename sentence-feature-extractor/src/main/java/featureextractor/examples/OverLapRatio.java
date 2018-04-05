package featureextractor.examples;

import featureextractor.lexicalsimilarity.OverlapWordRatio;

import java.util.ArrayList;

public class OverLapRatio {

    public static void main(String[] args) {
        OverlapWordRatio overlapWordRatio = new OverlapWordRatio();
        ArrayList<Double> overlapScores =overlapWordRatio.getOverlapScore( "Julie loves me more than Linda loves me",
                "Jane likes me more than Julie loves me");
        System.out.println("overlapScores :" + overlapScores.toString());
    }
}
