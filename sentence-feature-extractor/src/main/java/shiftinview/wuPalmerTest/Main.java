package shiftinview.wuPalmerTest;

import shiftinview.wuPalmerTest.controllers.SentencePairsController;
import shiftinview.wuPalmerTest.models.Sentence;
import shiftinview.wuPalmerTest.models.SentencePair;
import utils.NLPUtils;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<SentencePair> allSentencePairs = SentencePairsController.getAllSentencePairs();
        NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos");
        int i = 0;
        for (SentencePair sentencePair : allSentencePairs) {
            ArrayList<String> sourceVerbs = getSourceVerbs(sentencePair.getSourceSentence(), nlpUtils);
            ArrayList<String> targetVerbs = getTargetVerbs(sentencePair.getTargetSentence(), nlpUtils);

            ArrayList<String> common = new ArrayList<String>(sourceVerbs);
            common.retainAll(targetVerbs);

            if (common.size() > 0) {
                System.out.println("PairID: " + sentencePair.getId());
                System.out.println(common);
                System.out.println();
                i++;
            }
        }
        System.out.print("Number of pairs with common verbs: " + i);
        System.out.println(", out of " + allSentencePairs.size() + " pairs.");
    }

    private static ArrayList<String> getSourceVerbs(Sentence sourceSentence, NLPUtils nlpUtils) {
        return nlpUtils.getVerbs(nlpUtils.annotate(sourceSentence.getSentence()));
    }

    private static ArrayList<String> getTargetVerbs(Sentence targetSentence, NLPUtils nlpUtils) {
        return nlpUtils.getVerbs(nlpUtils.annotate(targetSentence.getSentence()));
    }
}
