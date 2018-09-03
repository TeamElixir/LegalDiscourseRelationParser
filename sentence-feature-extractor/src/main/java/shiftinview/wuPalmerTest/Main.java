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
            if (i++ == 9) break;
            ArrayList<String> sourceVerbs = getSourceVerbs(sentencePair.getSourceSentence(), nlpUtils);
            System.out.println(sourceVerbs);

            ArrayList<String> targetVerbs = getTargetVerbs(sentencePair.getTargetSentence(), nlpUtils);
            System.out.println(targetVerbs);

            System.out.println("");
        }
    }

    private static ArrayList<String> getSourceVerbs(Sentence sourceSentence, NLPUtils nlpUtils) {
        return nlpUtils.getVerbs(nlpUtils.annotate(sourceSentence.getSentence()));
    }

    private static ArrayList<String> getTargetVerbs(Sentence targetSentence, NLPUtils nlpUtils) {
        return nlpUtils.getVerbs(nlpUtils.annotate(targetSentence.getSentence()));
    }
}
