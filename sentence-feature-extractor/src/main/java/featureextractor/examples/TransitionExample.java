package featureextractor.examples;

import featureextractor.sentencepropertyfeatures.TransitionalWords;

public class TransitionExample {
    public static void main(String[] args) {
        TransitionalWords trWords = new TransitionalWords("Therefore,it is good");
        System.out.println("ellaborationScore :" + trWords.ellaborationScore());
        System.out.println("ellaborationScore :" + trWords.changeScore());
    }
}
