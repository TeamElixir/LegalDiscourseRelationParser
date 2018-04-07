package featureextractor.sentencepropertyfeatures;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransitionalWords {




    Pattern p = Pattern.compile("[a-zA-Z]+");
    Matcher m2;
    String sentence;
    int wordCount = 5;
    String firstWord;
    String subsentence="";
    boolean wordFound = false;

    public TransitionalWords(String sourceSentence){
        sentence=sourceSentence;
        m2 = p.matcher(sourceSentence);
        for(int i=0; i< wordCount; i++){
            if(m2.find()) {
                String word = m2.group().toLowerCase();
                if (i == 0) {
                    firstWord = word;
                    wordFound = true;
                }
                subsentence = subsentence + " " + word;
            }
        }

    }

    public int checkEllaborationWords(String sourceSentence){

        if(wordFound){
            if(PropertyUtils.getEllaborationWords().contains(firstWord)){
                return 1;
            }
        }

        return 0;

    }

    public int checkEllaborationPhrase(String sourceSentence){

        if (wordFound){
            ArrayList<String> transitionPhrases=PropertyUtils.getEllaborationPhrases();
            for(int i=0;i<transitionPhrases.size();i++){
                Pattern p = Pattern.compile("("+transitionPhrases.get(i)+")", Pattern.MULTILINE);
                Matcher m = p.matcher(subsentence);
                if(m.find()){
                    return 1;
                }

            }
        }
        return 0;

    }

}
