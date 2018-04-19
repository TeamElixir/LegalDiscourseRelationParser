package featureextractor.semanticsimilarity;

import edu.stanford.nlp.pipeline.Annotation;
import utils.NLPUtils;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;


import java.util.ArrayList;
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
                double distance = compute(nouns.get(i), nouns.get(j));
                aggregateNounDistance+= distance;
                System.out.println(nouns.get(i) +" -  " +  nouns.get(j) + " = " + distance);
                nounCount++;
                System.out.println(nounCount);
            }
        }
        for(int i=0; i<verbs.size()-1; i++){
            for(int j=i+1; j<verbs.size(); j++){
                double distance = compute(verbs.get(i), verbs.get(j));
                aggregateVerbDistance+=distance;
                System.out.println(verbs.get(i) +" -  " +  verbs.get(j) + " = " + distance);
                verbCount++;
                System.out.println(verbCount);
            }
        }
    }

    public double getAverageScore(){
        double score = (aggregateNounDistance+aggregateVerbDistance)/(double)(nounCount+verbCount);
        return score;
    }



}
