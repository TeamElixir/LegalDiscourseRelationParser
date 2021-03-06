package shiftinview;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.stanford.nlp.pipeline.Annotation;
import featureextractor.semanticsimilarity.SemanticSentenceSimilarity;
import shiftinview.models.VerbPair;
import shiftinview.models.VerbRelation;
import shiftinview.utils.ModifierUtils;
import utils.NLPUtils;

import java.util.ArrayList;
import java.util.Properties;

public class ShiftInViewAnalyzer {

    public static void main(String[] args) {

        String targetSentence = "Petitioner Jae Lee moved to the United States from South Korea with Petitioner Jae Lee's parents when Petitioner Jae Lee was 13.";
        String sourceSentence
                = "In the 35 years Petitioner Jae Lee has spent in the 35 years, Petitioner Jae Lee has never returned to South Korea, nor has Petitioner Jae Lee become aU.S. citizen, living instead as a lawful permanent resident.";

        // set up pipeline properties
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
        // use faster shift reduce parser
        props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        props.setProperty("parse.maxlen", "100");
        // set up Stanford CoreNLP pipeline
        // StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        NLPUtils nlpUtils = new NLPUtils(props);
        Integer value = checkRelationsForOppositeness(nlpUtils, targetSentence, sourceSentence);
        System.out.println("Returned value: " + value);
    }

    public static Integer checkRelationsForOppositeness(NLPUtils nlpUtils, String targetSentence, String sourceSentence) {

        ArrayList<VerbRelation> verbsSentence1;
        ArrayList<VerbRelation> verbsSentence2;

        Annotation targetAnnotation;
        Annotation sourceAnnotation;

        ArrayList<String> coreferencedSentences = new ArrayList<>();

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

        verbsSentence1 = constituentParser.getVerbRelationships(sourceAnnotation, nlpUtils);

        System.out.println("targetSentence");
        System.out.println(" ");

        verbsSentence2 = constituentParser.getVerbRelationships(targetAnnotation, nlpUtils);
        String verbSource = "";
        String verbTarget = "";
        Integer verbSourceId = -1;
        Integer verbTargetId = -1;
        String sourceOther = "";
        String targetOther = "";
        VerbRelation verbObjectTarget = new VerbRelation();
        VerbRelation verbObjectSource = new VerbRelation();
        String currentSourceVerb = "";
        String currentTargetVerb = "";
        int currentSourceVerbID = -1;
        int currentTargetVerbID = -1;
        VerbRelation secondverb;
        VerbRelation verb;
        ArrayList<VerbPair> closeVerbs = new ArrayList<>();

       /* System.out.println("array sizes");
        System.out.println(verbsSentence2.size());
        System.out.println(verbsSentence1.size());*/

        for (int j = 0; j < verbsSentence2.size(); j++) {
            verb = verbsSentence2.get(j);
	        System.out.println("verbSize"+verbsSentence2.size());
            if (verb.isVerbIsDep()) {
                verbTarget = verb.getDepLemma();
                verbTargetId = verb.getId();
                verbObjectTarget = verb;
            } else if (verb.isVerbIsGov()) {
                verbTarget = verb.getGovLemma();
                verbTargetId = verb.getId();
                verbObjectTarget = verb;
            }
            for (int i = 0; i < verbsSentence1.size(); i++) {
                secondverb = verbsSentence1.get(i);

                if (secondverb.isVerbIsDep()) {

                    verbSource = secondverb.getDepLemma();
                    verbSourceId = verb.getId();
                    verbObjectSource = secondverb;
                } else if (secondverb.isVerbIsGov()) {
                    verbSource = secondverb.getGovLemma();
                    verbSourceId = verb.getId();
                    verbObjectSource = secondverb;
                }


                /*System.out.println(" ");
                System.out.println("s  "  +verbSource);
                System.out.println("t  "+ verbTarget);*/
                SemanticSentenceSimilarity semanticSentenceSimilarity = new SemanticSentenceSimilarity();
//				double score = semanticSentenceSimilarity.wordSimilarity(verbSource, POS.v, verbTarget, POS.v);
                // using Lin instead of WuPalmer

	            System.out.println("ssder");
                double score = semanticSentenceSimilarity.getAllWordSimilarityScores(verbSource, POS.v, verbTarget, POS.v)[0];
	            System.out.println("jj");
                if (score >= 0.86) {
                    if (currentSourceVerbID != verbSourceId || currentTargetVerbID != verbTargetId) {
                        /*String[] verbPair = new String[2];
                        verbPair[0]=verbTarget;
                        verbPair[1]=verbSource;*/
                        currentSourceVerbID = verbSourceId;
                        currentTargetVerbID = verbTargetId;
                        //System.out.println("rel :"+verbObjectTarget.getRelation());
                        //System.out.println("rel :" + verbObjectSource.getRelation());

                        if (verbObjectTarget.isVerbIsDep()) {
                            //System.out.println("other :"+verbObjectTarget.getGovWord());
                            targetOther = verbObjectTarget.getGovWord();
                        } else if (verbObjectTarget.isVerbIsGov()) {
                            //System.out.println("other :"+verbObjectTarget.getDepWord());
                            targetOther = verbObjectTarget.getDepWord();
                        }
                        if (verbObjectSource.isVerbIsDep()) {
                            //System.out.println("other :"+verbObjectSource.getGovWord());
                            sourceOther = verbObjectSource.getGovWord();
                        } else if (verbObjectSource.isVerbIsGov()) {
                            //System.out.println("other :"+verbObjectSource.getDepWord());
                            sourceOther = verbObjectSource.getDepWord();
                        }
                        VerbPair verbPair = new VerbPair();
                        verbPair.setTargetVerb(verbTarget);
                        verbPair.setSourceVerb(verbSource);
                        if (verbObjectTarget.getRelation().equals("neg")) {
                            verbPair.setTargetVerbNegated(true);
                        }
                        if (verbObjectSource.getRelation().equals("neg")) {
                            verbPair.setSourceVerbNegated(true);
                        }
                        if (ModifierUtils.getNegativeWords().contains(targetOther)) {
                            verbPair.setTargetVerbNegated(true);
                        }
                        if (ModifierUtils.getNegativeWords().contains(sourceOther)) {
                            verbPair.setSourceVerbNegated(true);
                        }

                        if (ModifierUtils.getLessFrequent().contains(targetOther)) {
                            verbPair.setLessFrequentTarget(true);
                        }
                        if (ModifierUtils.getMoreFrequent().contains(targetOther)) {
                            verbPair.setMoreFrequentTarget(true);
                        }
                        if (ModifierUtils.getDowntoners().contains(targetOther)) {
                            verbPair.setDowntonerTarget(true);
                        }
                        if (ModifierUtils.getAmplifiers().contains(targetOther)) {
                            verbPair.setAmplifierTarget(true);
                        }
                        if (ModifierUtils.getNegativeManner().contains(targetOther)) {
                            verbPair.setNegativeMannerTarget(true);
                        }
                        if (ModifierUtils.getPositiveManner().contains(targetOther)) {
                            verbPair.setPositiveMannerTarget(true);
                        }

                        if (ModifierUtils.getLessFrequent().contains(sourceOther)) {
                            verbPair.setLessFrequentSource(true);
                        }
                        if (ModifierUtils.getMoreFrequent().contains(sourceOther)) {
                            verbPair.setMoreFrequentSource(true);
                        }
                        if (ModifierUtils.getDowntoners().contains(sourceOther)) {
                            verbPair.setDowntonerSource(true);
                        }
                        if (ModifierUtils.getAmplifiers().contains(sourceOther)) {
                            verbPair.setAmplifierSource(true);
                        }
                        if (ModifierUtils.getNegativeManner().contains(sourceOther)) {
                            verbPair.setNegativeMannerSource(true);
                        }
                        if (ModifierUtils.getPositiveManner().contains(sourceOther)) {
                            verbPair.setPositiveMannerSource(true);
                        }

                        closeVerbs.add(verbPair);

                    }
                }

                System.out.println("score: "+score);

            }

        }
        Integer value = detectNegation(closeVerbs);
        System.out.println("val :" +value);
        return value;

    }

    public static Integer detectNegation(ArrayList<VerbPair> closeVerbs) {
        System.out.println("close verbs");
        Integer count = 0;

        for (VerbPair pair : closeVerbs) {
            System.out.println("TV :" + pair.getTargetVerb());
            System.out.println("SV :" + pair.getSourceVerb());
            System.out.println("targetNegated: " + pair.getTargetVerbNegated());
            System.out.println("sourceNegated: " + pair.getSourceVerbNegated());
            if (pair.getTargetVerbNegated()) {
                if (!pair.getSourceVerbNegated()) {
                    count++;
                }
            } else {
                if (pair.getSourceVerbNegated()) {
                    count++;
                }
            }
            if (pair.getLessFrequentTarget() && pair.getMoreFrequentSource() ||
                    pair.getMoreFrequentTarget() && pair.getLessFrequentSource()) {
                count++;

            }
            if (pair.getAmplifierTarget() && pair.getDowntonerSource() ||
                    pair.getDowntonerTarget() && pair.getAmplifierSource()) {
                count++;
            }
            if (pair.getPositiveMannerTarget() && pair.getNegativeMannerSource() ||
                    pair.getNegativeMannerSource() && pair.getPositiveMannerSource()) {
                count++;
            }

            System.out.println("");
        }
        System.out.println(count);
        return count;
    }

}
