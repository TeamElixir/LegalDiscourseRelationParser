package featureextractor.semanticsimilarity;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.stanford.nlp.pipeline.Annotation;
import utils.NLPUtils;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SemanticSentenceSimilarity {

    private String sentence1 ;
    private String sentence2;

    ArrayList<String> nouns = new ArrayList<>();
    ArrayList<String> verbs = new ArrayList<>();
    private int nounCount=0;
    private int verbCount=0;
    private double aggregateNounDistance = 0;
    private double aggregateVerbDistance = 0;
    private Annotation annotation1 ;
    private Annotation annotation2 ;

    NLPUtils nlpUtils;
    private static ILexicalDatabase db = new NictWordNet();
    static RelatednessCalculator rc = new WuPalmer(db);

    public SemanticSentenceSimilarity(Annotation stc1, Annotation stc2,NLPUtils nlpUtls){
        annotation1 = stc1;
        annotation2 = stc2;
        nlpUtils = nlpUtls;
    }

    private double compute(String word1, String word2) {
        WS4JConfiguration.getInstance().setMFS(true);
        double s = new WuPalmer(db).calcRelatednessOfWords(word1, word2);
        return s;
    }

    public double wordSimilarity(String word1, POS posWord1, String word2, POS posWord2) {
        double maxScore = 0D;
        try {
            WS4JConfiguration.getInstance().setMFS(true);
            List<Concept> synsets1 = (List<Concept>) db.getAllConcepts(word1, posWord1.name());
            List<Concept> synsets2 = (List<Concept>) db.getAllConcepts(word2, posWord2.name());
            for (Concept synset1 : synsets1) {
                for (Concept synset2 : synsets2) {
                    Relatedness relatedness = rc.calcRelatednessOfSynset(synset1, synset2);
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
            System.out.println(("Exception : "+ e));
        }
        if(maxScore>=1){
            return 1;
        }
        return maxScore;
    }

    public void getVerbsAndNouns(){

        nouns.addAll(nlpUtils.getNouns(annotation1));
        nouns.addAll(nlpUtils.getNouns(annotation2));
        verbs.addAll(nlpUtils.getVerbs(annotation1));
        verbs.addAll(nlpUtils.getVerbs(annotation2));

        System.out.println("nouns :"+nouns);
        System.out.println("verbs :"+verbs);
    }

    public void checkSimilarity() {

        getVerbsAndNouns();
        for(int i=0; i<nouns.size()-1; i++){
            for(int j=i+1; j<nouns.size(); j++){
                double distance;
                if(nouns.get(i).equals(nouns.get(j))){
                    distance = 1;
                }else {

                    distance = wordSimilarity(nouns.get(i), POS.n, nouns.get(j), POS.n);

                }
                        //compute(nouns.get(i), nouns.get(j));
                aggregateNounDistance+= distance;
                System.out.println(nouns.get(i) +" -  " +  nouns.get(j) + " = " + distance);
                nounCount++;
                System.out.println(nounCount);
            }
        }
        for(int i=0; i<verbs.size()-1; i++){
            for(int j=i+1; j<verbs.size(); j++){

                double distance;
                if(verbs.get(i).equals(verbs.get(j))){
                    distance = 1;
                }else {
                    distance = wordSimilarity(nouns.get(i), POS.v, nouns.get(j), POS.v);

                    //compute(verbs.get(i), verbs.get(j));
                }
                aggregateVerbDistance+=distance;
                System.out.println(verbs.get(i) +" -  " +  verbs.get(j) + " = " + distance);
                verbCount++;
                System.out.println(verbCount);
            }
        }
    }

    public double getAverageScore(){
        checkSimilarity();
        if(nounCount==0 && verbCount==0) {
            return 0.0;
        }
        double score = (aggregateNounDistance + aggregateVerbDistance) / (double) (nounCount + verbCount);
        return score;
    }



}
