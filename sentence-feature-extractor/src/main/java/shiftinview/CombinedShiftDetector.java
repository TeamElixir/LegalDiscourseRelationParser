package shiftinview;

import SentimentAnnotator.ParseTreeSplitter;
import utils.NLPUtils;

import java.util.Properties;

public class CombinedShiftDetector {



    public static void main(String[] args) {
        String targetSentence = "Lee contends that he can make this showing because he never would have " +
                "accepted a guilty plea had he known the result would be deportation.";

        String sourceSentence = "The Government contends that Lee cannot show prejudice from accepting a plea where his only hope at trial was that" +
                " something unexpected and unpredictable might occur that would lead to acquittal.";

        // set up pipeline properties
        Properties props = new Properties();
        //props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,sentiment");
        // use faster shift reduce parser
        props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        props.setProperty("parse.maxlen", "100");
        // set up Stanford CoreNLP pipeline
        // StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        NLPUtils nlpUtils =  new NLPUtils(props);
        boolean bValue = combinedDetector(nlpUtils,targetSentence,sourceSentence);
        if(bValue){
            System.out.println("final ::: Truee");
        }else{
            System.out.println("final ::: False");
        }



    }

    public static boolean combinedDetector(NLPUtils nlpUtils,String targetSentence, String sourceSentence){
        Integer value =ShiftInViewAnalyzer.checkRelationsForOppositeness(nlpUtils,targetSentence,sourceSentence);
        if(value>0){
            return true;
        }else if(ParseTreeSplitter.subjectSentiment(nlpUtils,targetSentence,sourceSentence)){
            return true;
        }
        return false;

    }
}
