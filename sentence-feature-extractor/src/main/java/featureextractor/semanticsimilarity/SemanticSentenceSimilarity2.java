package featureextractor.semanticsimilarity;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.*;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import edu.stanford.nlp.pipeline.Annotation;
import utils.NLPUtils;

import java.util.ArrayList;
import java.util.List;

public class SemanticSentenceSimilarity2 {

    private String sentence1;
    private String sentence2;

    //new method
    ArrayList<String> nouns1 = new ArrayList<>();
    ArrayList<String> nouns2 = new ArrayList<>();
    ArrayList<String> verbs1 = new ArrayList<>();
    ArrayList<String> verbs2 = new ArrayList<>();

    //previous method

   /* ArrayList<String> nouns = new ArrayList<>();
    ArrayList<String> verbs = new ArrayList<>();*/


    private int nounCount = 0;
    private int verbCount = 0;
    private double aggregateNounDistance = 0;
    private double aggregateVerbDistance = 0;
    private Annotation annotation1;
    private Annotation annotation2;
    private boolean hasValue = false;

    NLPUtils nlpUtils;
    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator rcWuPalmer = new WuPalmer(db);

    // the following is in the alphabetical order. do not change.
    private static RelatednessCalculator[] rcs = {
//            new HirstStOnge(db),     // 0
//            new JiangConrath(db),    // 1
//            new LeacockChodorow(db), // 2
//            new Lesk(db),            // 3
            new Lin(db)        // 4
//            new Path(db),       // 5
//            new Resnik(db),     // 6
//            new WuPalmer(db)    //7
    };

    public SemanticSentenceSimilarity2(Annotation stc1, Annotation stc2, NLPUtils nlpUtls) {
        annotation1 = stc1;
        annotation2 = stc2;
        nlpUtils = nlpUtls;
    }

    public SemanticSentenceSimilarity2() {

    }

    private double compute(String word1, String word2) {
        WS4JConfiguration.getInstance().setMFS(true);
        double s = new WuPalmer(db).calcRelatednessOfWords(word1, word2);
        return s;
    }

    public double[] getAllWordSimilarityScores(String word1, POS posWord1, String word2, POS posWord2) {
        double[] maxScores = {0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D};
        try {
            for (int i = 0; i < rcs.length; i++) {
                WS4JConfiguration.getInstance().setMFS(true);
                List<Concept> synsets1 = (List<Concept>) db.getAllConcepts(word1, posWord1.name());
                List<Concept> synsets2 = (List<Concept>) db.getAllConcepts(word2, posWord2.name());
                for (Concept synset1 : synsets1) {
                    for (Concept synset2 : synsets2) {
                        Relatedness relatedness = rcs[i].calcRelatednessOfSynset(synset1, synset2);
                        double score = relatedness.getScore();

                        if (score > maxScores[i]) {
                            maxScores[i] = score;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < rcs.length; i++) {
            if (maxScores[i] > 1) {
                maxScores[i] = 1;
            }
        }
        return maxScores;
    }

    public double wordSimilarity(String word1, POS posWord1, String word2, POS posWord2) {
        double maxScore = 0D;
        try {
            WS4JConfiguration.getInstance().setMFS(true);
            List<Concept> synsets1 = (List<Concept>) db.getAllConcepts(word1, posWord1.name());
            List<Concept> synsets2 = (List<Concept>) db.getAllConcepts(word2, posWord2.name());
            for (Concept synset1 : synsets1) {
                for (Concept synset2 : synsets2) {
                    Relatedness relatedness = rcWuPalmer.calcRelatednessOfSynset(synset1, synset2);
                    double score = relatedness.getScore();

                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
            }
          /*  System.out.println("s1 : "+synsets1.toString());
            System.out.println("s2 : "+synsets2.toString());
            System.out.println("Similarity score of " + word1 + " & " + word2 + " : " + maxScore);*/
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
        if (maxScore >= 1) {
            return 1;
        }
        return maxScore;
    }

    public void getVerbsAndNouns() {
        //new method


        nouns1.addAll(nlpUtils.getNouns(annotation1));
        nouns2.addAll(nlpUtils.getNouns(annotation2));
        verbs1.addAll(nlpUtils.getLemmaVerbsWithOutBe(annotation1));
        verbs2.addAll(nlpUtils.getLemmaVerbsWithOutBe(annotation2));



        //previous method
        /*nouns.addAll(nlpUtils.getNouns(annotation1));
        nouns.addAll(nlpUtils.getNouns(annotation2));
        verbs.addAll(nlpUtils.getVerbs(annotation1));
        verbs.addAll(nlpUtils.getVerbs(annotation2));*/

        //  System.out.println("nouns1 :"+nouns1);
        //  System.out.println("verbs1 :"+verbs1);
    }

    public void checkSimilarity() {

        getVerbsAndNouns();
        if (nouns1.size() > 0 && nouns2.size() > 0) {
            hasValue = true;
            for (int i = 0; i < nouns2.size(); i++) {
                double maxdistance = 0;
                for (int j = 0; j < nouns1.size(); j++) {
                    double distance;
                    if (nouns2.get(i).equals(nouns1.get(j))) {
                        distance = 1;
                    } else {

                        distance = wordSimilarity(nouns2.get(i), POS.n, nouns1.get(j), POS.n);

                    }
                    if (maxdistance < distance) {
                        maxdistance = distance;
                    }
                    //compute(nouns1.get(i), nouns1.get(j));

                }
                aggregateNounDistance += maxdistance;
//                    System.out.println(nouns1.get(i) + " -  " + nouns1.get(j) + " = " + distance);


                nounCount++;

                //System.out.println(nounCount);
            }
        }
        if (verbs1.size() > 0 && verbs2.size() > 0) {
            hasValue = true;
            for (int i = 0; i < verbs2.size(); i++) {
                double maxdistance = 0;
                for (int j = 0; j < verbs1.size(); j++) {

                    double distance;
                    if (verbs2.get(i).equals(verbs1.get(j))) {
                        distance = 1;
                    } else {
                        distance = wordSimilarity(verbs2.get(i), POS.v, verbs1.get(j), POS.v);

                        //compute(verbs1.get(i), verbs1.get(j));
                    }
                    if (maxdistance < distance) {
                        maxdistance = distance;
                    }

                }
                aggregateVerbDistance += maxdistance;
                //System.out.println(verbs1.get(i) + " -  " + verbs1.get(j) + " = " + distance);
                verbCount++;
                //System.out.println(verbCount);
            }

        }



        //previos method

   /*     if(nouns.size()>1) {
            hasValue=true;
            for (int i = 0; i < nouns.size() - 1; i++) {
                for (int j = i + 1; j < nouns.size(); j++) {
                    double distance;
                    if (nouns.get(i).equals(nouns.get(j))) {
                        distance = 1;
                    } else {

                        distance = wordSimilarity(nouns.get(i), POS.n, nouns.get(j), POS.n);

                    }
                    //compute(nouns.get(i), nouns.get(j));
                    aggregateNounDistance += distance;
//                    System.out.println(nouns.get(i) + " -  " + nouns.get(j) + " = " + distance);
                    nounCount++;
                    //System.out.println(nounCount);
                }
            }
        }
        if (verbs.size()>1) {
            hasValue=true;
            for (int i = 0; i < verbs.size() - 1; i++) {
                for (int j = i + 1; j < verbs.size(); j++) {

                    double distance;
                    if (verbs.get(i).equals(verbs.get(j))) {
                        distance = 1;
                    } else {
                        distance = wordSimilarity(verbs.get(i), POS.v, verbs.get(j), POS.v);

                        //compute(verbs.get(i), verbs.get(j));
                    }
                    aggregateVerbDistance += distance;
                    //System.out.println(verbs.get(i) + " -  " + verbs.get(j) + " = " + distance);
                    verbCount++;
                    //System.out.println(verbCount);
                }
            }

        }*/

    }

    public double getAverageScore() {
        checkSimilarity();
        if (nounCount == 0 && verbCount == 0) {

            return 0.0;
        } else if (!hasValue) {

            return 0.0;
        }

        double score = (aggregateNounDistance + aggregateVerbDistance) / (double) ((nounCount + verbCount));
        /*double finalScore = score * 2;
        if(finalScore >= 1){
           return 1;
        }*/

        return score;

    }

    public double getWuPalmerRelatedness(String word1, String word2) {
        WS4JConfiguration.getInstance().setMFS(true);
        double s = new WuPalmer(db).calcRelatednessOfWords(word1, word2);
        return s;
    }


}
