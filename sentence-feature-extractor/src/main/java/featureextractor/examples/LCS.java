package featureextractor.examples;

import featureextractor.lexicalsimilarity.LongestCommonSubstring;

import java.util.ArrayList;

public class LCS{

    public static void main(String[] args) {
        String sentence1 = "CNN.com - Plane hits skyscraper in Milan - April 18, 2002";
        String sentence2 = "CNN.com - Plane hits skyscraper in Milan - April 18, 2002";
        ArrayList<String> output = new ArrayList<String>();

        LongestCommonSubstring longestCommonSubstring=new LongestCommonSubstring(sentence1,sentence2);
        output = longestCommonSubstring.longestSubstring();
        double value = longestCommonSubstring.lcsValueSentence1();

        System.out.println("output LCS : " + output.toString());
        System.out.println("value LCS: "+ value);

    }
}
