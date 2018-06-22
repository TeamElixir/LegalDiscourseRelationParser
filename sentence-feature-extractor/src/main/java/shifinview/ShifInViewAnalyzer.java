package shifinview;

import utils.NLPUtils;

import java.util.ArrayList;
import java.util.Properties;

public class ShifInViewAnalyzer {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref");
        props.setProperty("coref.algorithm", "statistical");
        NLPUtils nlpUtils = new NLPUtils(props);

        String targetSentence = "Although he has lived in this country for most of his life, Lee is not a United States" +
                " citizen, and he feared that a criminal conviction might affect his status as a lawful " +
                "permanent resident.";
        String sourceSentence = "His attorney assured him there was nothing to worry about--the Government would not " +
                "deport him if he pleaded guilty.\n";

        ArrayList<String> coreferencedSentences = new ArrayList<>();

        Coreferencer coreferencer = new Coreferencer();
        coreferencedSentences=Coreferencer.getCoreferencedSentences(sourceSentence,targetSentence,nlpUtils);
        System.out.println(coreferencedSentences.get(0));
        System.out.println(coreferencedSentences.get(1));

    }
}
