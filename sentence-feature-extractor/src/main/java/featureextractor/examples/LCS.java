package featureextractor.examples;

import featureextractor.lexicalsimilarity.LongestCommonSubstring;

import java.util.ArrayList;

public class LCS{

    public static void main(String[] args) {
        String sentence1 = "Gathika is a good boy.";
        String sentence2 = "Gathika plays cricket.";
        ArrayList<String> output = new ArrayList<String>();

        LongestCommonSubstring longestCommonSubstring=new LongestCommonSubstring(sentence1.toLowerCase(),sentence2.toLowerCase());
        output = longestCommonSubstring.longestSubstring();
        double value = longestCommonSubstring.lcsValueSentence1();

        System.out.println("output LCS : " + output.toString());
        System.out.println("value LCS: "+ value);

    }
}
