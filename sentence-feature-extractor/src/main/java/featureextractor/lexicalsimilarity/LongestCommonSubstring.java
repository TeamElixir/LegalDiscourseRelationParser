package featureextractor.lexicalsimilarity;

import java.util.ArrayList;

public class LongestCommonSubstring {

    public static ArrayList<String> longestSubstring(String sentence1, String sentence2) {

        ArrayList<String> longestCommonSubstrings = new ArrayList<String>();
        StringBuilder longestSubString = new StringBuilder();


        if (sentence1 == null ||  sentence2 == null ) {
            return null;
        }

// not case sensitive
        sentence1 = sentence1.toLowerCase();
        sentence2 = sentence2.toLowerCase();

// java initializes them already with 0

        //two dimensional array
        int[][] num = new int[sentence1.length()][sentence2.length()];
        //current length of longest substring
        int currentMaxLength = 0;
        int lastSubsBegin = 0;

        for (int i = 0; i < sentence1.length(); i++) {
            for (int j = 0; j < sentence2.length(); j++) {
                if (sentence1.charAt(i) == sentence2.charAt(j)) {
                    if ((i == 0) || (j == 0))
                        num[i][j] = 1;
                    else
                        //diagonal previous value+1
                        num[i][j] = 1 + num[i - 1][j - 1];

                    if (num[i][j] > currentMaxLength) {
                        currentMaxLength = num[i][j];
                        // generate substring from str1 => i
                        int thisSubsBegin = i - num[i][j] + 1;
                        if (lastSubsBegin == thisSubsBegin) {
                            //if the current LCS is the same as the last time this block ran
                            longestSubString.append(sentence1.charAt(i));
                        } else {
                            //this block resets the string builder if a different LCS is found
                            lastSubsBegin = thisSubsBegin;
                            longestSubString = new StringBuilder();
                            longestSubString.append(sentence1.substring(lastSubsBegin, i + 1));
                        }
                        longestCommonSubstrings=new ArrayList<String>();
                        longestCommonSubstrings.add(longestSubString.toString());
                    }else if (num[i][j]==currentMaxLength){
                        int thisSubsBegin = i - num[i][j] + 1;
                        if (lastSubsBegin == thisSubsBegin) {
                            //if the current LCS is the same as the last time this block ran
                            longestSubString.append(sentence1.charAt(i));
                        } else {
                            //this block resets the string builder if a different LCS is found
                            lastSubsBegin = thisSubsBegin;
                            longestSubString = new StringBuilder();
                            longestSubString.append(sentence1.substring(lastSubsBegin, i + 1));
                        }
                        longestCommonSubstrings.add(longestSubString.toString());
                    }
                }
            }}

        return longestCommonSubstrings;
    }
}
