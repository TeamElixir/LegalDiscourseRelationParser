package shiftinview.wuPalmerTest;

import edu.cmu.lti.jawjaw.pobj.POS;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import shiftinview.wuPalmerTest.models.Sentence;
import shiftinview.wuPalmerTest.models.SentencePair;
import utils.NLPUtils;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
//        ArrayList<SentencePair> allSentencePairs = SentencePairsController.getAllSentencePairs();
        ArrayList<SentencePair> allSentencePairs = new ArrayList<>();
        SentencePair pair = new SentencePair(1,
                // source sentence
                new Sentence(" he feared that a criminal conviction might affect his status " +
                        "as a lawful permanent resident"),
                // target sentence
                new Sentence("His attorney assured him there was nothing to worry about--" +
                        "the Government would not deport him if he pleaded guilty."),
                "no relation");

        allSentencePairs.add(pair);

        NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos,lemma");
        int i = 0;
        for (SentencePair sentencePair : allSentencePairs) {
            ArrayList<String> sourceVerbs = getSourceVerbs(sentencePair.getSourceSentence(), nlpUtils);
            ArrayList<String> targetVerbs = getTargetVerbs(sentencePair.getTargetSentence(), nlpUtils);
            System.out.println("Source verbs: " + sourceVerbs);
            System.out.println("Target verbs: " + targetVerbs);
            ArrayList<String[]> verbPairs = new ArrayList<>();

            SemanticSentenceSimilarity similarity = new SemanticSentenceSimilarity();
            for (String sVerb : sourceVerbs) {
                for (String tVerb : targetVerbs) {
                    double wordSimilarity = similarity.wordSimilarity(sVerb, POS.v, tVerb, POS.v);
                    if (wordSimilarity > 0.7) {
                        verbPairs.add(new String[]{sVerb, tVerb});
                        System.out.println(sVerb + " : " + tVerb);
                    }
                }
            }

        }
    }

    private static ArrayList<String> getSourceVerbs(Sentence sourceSentence, NLPUtils nlpUtils) {
        return nlpUtils.getLemmaVerbsWithOutBe(nlpUtils.annotate(sourceSentence.getSentence()));
    }

    private static ArrayList<String> getTargetVerbs(Sentence targetSentence, NLPUtils nlpUtils) {
        return nlpUtils.getLemmaVerbsWithOutBe(nlpUtils.annotate(targetSentence.getSentence()));
    }
}
