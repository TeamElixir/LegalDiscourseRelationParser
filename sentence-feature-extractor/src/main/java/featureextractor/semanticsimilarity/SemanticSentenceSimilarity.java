package featureextractor.semanticsimilarity;

import edu.stanford.nlp.pipeline.Annotation;
import utils.NLPUtils;


import java.util.ArrayList;
import java.util.Properties;

public class SemanticSentenceSimilarity {

    String sentence1 = "Gathika and John play cricket";
    ArrayList<String> nouns = new ArrayList<>();
    ArrayList<String> verbs = new ArrayList<>();

    NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos");

    public void getVerbsAndNouns(){
        Annotation annotation =nlpUtils.annotate(sentence1);
        nouns = nlpUtils.getNouns(annotation);
        verbs = nlpUtils.getVerbs(annotation);

        System.out.println("nouns :"+nouns);
        System.out.println("verbs :"+verbs);
    }



}
