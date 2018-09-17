package shiftinview;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;

import shiftinview.models.Verb;
import shiftinview.models.VerbPair;
import utils.NLPUtils;

import  java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.cmu.lti.jawjaw.pobj.POS;

public class ShiftInViewAnalyzer {



    public static void main(String[] args) {

        List<String> negativeWords = new ArrayList<String>(Arrays.asList("never", "not", "nothing"));
        String targetSentence = "Lee could not show that he was prejudiced by his attorney's erroneous advice.";
        String sourceSentence
                = "Lee has demonstrated that he was prejudiced by his counsel's erroneous advice.";
        ArrayList<Verb> verbsSentence1;
        ArrayList<Verb> verbsSentence2;


        Annotation targetAnnotation;
        Annotation sourceAnnotation;

        ArrayList<String> coreferencedSentences = new ArrayList<>();

        // set up pipeline properties
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
        // use faster shift reduce parser
        props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        props.setProperty("parse.maxlen", "100");
        // set up Stanford CoreNLP pipeline
        // StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        NLPUtils nlpUtils =  new NLPUtils(props);
        // build annotation for a review
        sourceAnnotation = nlpUtils.annotate(sourceSentence);
        targetAnnotation = nlpUtils.annotate(targetSentence);



       /* Coreferencer coreferencer = new Coreferencer();
        coreferencedSentences=Coreferencer.getCoreferencedSentences(sourceSentence,targetSentence,nlpUtils);
        System.out.println(coreferencedSentences.get(0));
        System.out.println(coreferencedSentences.get(1));*/
        ConstituentParser constituentParser = new ConstituentParser();
        //  constituentParser.runConstituentParser(sourceAnnotation,nlpUtils);

        System.out.println("break");

        verbsSentence1 = constituentParser.getVerbRelationships(sourceAnnotation,nlpUtils);



        System.out.println( "targetSentence");
        System.out.println(" ");

        verbsSentence2 = constituentParser.getVerbRelationships(targetAnnotation,nlpUtils);
        String verbSource="";
        String verbTarget="";
        String sourceOther="";
        String targetOther="";
        Verb verbObjectTarget=new Verb();
        Verb verbObjectSource= new Verb();
        String currentSourceVerb="";
        String currentTargetVerb = "";
        Verb secondverb;
        Verb verb;
        ArrayList<VerbPair> closeVerbs = new ArrayList<>();


        System.out.println("array sizes");
        System.out.println(verbsSentence2.size());
        System.out.println(verbsSentence1.size());

        for(int j=0;j<verbsSentence2.size();j++){
            verb = verbsSentence2.get(j);

            if(verb.isVerbIsDep()){
                verbTarget = verb.getDepLemma();
                verbObjectTarget=verb;
            }
            else if(verb.isVerbIsGov()){
                verbTarget=verb.getGovLemma();
                verbObjectTarget=verb;
            }
            for(int i=0;i<verbsSentence1.size();i++){
                secondverb= verbsSentence1.get(i);


                if(secondverb.isVerbIsDep()){
                    verbSource=secondverb.getDepLemma();
                    verbObjectSource=secondverb;
                }
                else if(secondverb.isVerbIsGov()){
                    verbSource=secondverb.getGovLemma();
                    verbObjectSource=secondverb;
                }
                /*System.out.println(" ");
                System.out.println("s  "  +verbSource);
                System.out.println("t  "+ verbTarget);*/
                SemanticSentenceSimilarity semanticSentenceSimilarity= new SemanticSentenceSimilarity();
                double score =semanticSentenceSimilarity.wordSimilarity(verbSource,POS.v,verbTarget,POS.v);
                if(score>=0.8){
                    if(!currentSourceVerb.equals(verbSource) || !currentTargetVerb.equals(verbTarget)){
                        /*String[] verbPair = new String[2];
                        verbPair[0]=verbTarget;
                        verbPair[1]=verbSource;*/
                        currentSourceVerb=verbSource;
                        currentTargetVerb=verbTarget;
                        System.out.println("rel :"+verbObjectTarget.getRelation());
                        System.out.println("rel :" + verbObjectSource.getRelation());

                        if(verbObjectTarget.isVerbIsDep()){
                            System.out.println("other :"+verbObjectTarget.getGovWord());
                            targetOther=verbObjectTarget.getGovWord();
                        }
                        else if(verbObjectTarget.isVerbIsGov()){
                            System.out.println("other :"+verbObjectTarget.getDepWord());
                            targetOther=verbObjectTarget.getDepWord();
                        }
                        if(verbObjectSource.isVerbIsDep()){
                            System.out.println("other :"+verbObjectSource.getGovWord());
                            sourceOther=verbObjectSource.getGovWord();
                        }
                        else if(verbObjectSource.isVerbIsGov()){
                            System.out.println("other :"+verbObjectSource.getDepWord());
                            sourceOther=verbObjectSource.getDepWord();
                        }
                        VerbPair verbPair = new VerbPair();
                        verbPair.setTargetVerb(verbTarget);
                        verbPair.setSourceVerb(verbSource);
                        if(negativeWords.contains(targetOther)){
                            verbPair.setTargetVerbNegated(true);
                        }
                        if(negativeWords.contains(sourceOther)){
                            verbPair.setSourceVerbNegated(true);
                        }

                        closeVerbs.add(verbPair);

                    }
                }


                //System.out.println("score: "+score);

            }

        }

        System.out.println("close verbs");
        for(VerbPair pair:closeVerbs){
            System.out.println("TV :" + pair.getTargetVerb());
            System.out.println("SV :"+pair.getSourceVerb());
            System.out.println("targetNegated: "+ pair.getTargetVerbNegated());
            System.out.println("sourceNegated: " + pair.getSourceVerbNegated());
            System.out.println("");
        }







    }
}
