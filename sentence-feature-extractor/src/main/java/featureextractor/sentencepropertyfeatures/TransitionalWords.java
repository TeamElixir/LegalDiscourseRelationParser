package featureextractor.sentencepropertyfeatures;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransitionalWords {

    public int checkEllaborationWords(String sentence2){

        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m2 = p.matcher(sentence2);
        String firstWord = "";

        if(m2.find()){
            firstWord=m2.group().toLowerCase();
            if(PropertyUtils.getEllaborationWords().contains(firstWord)){
                return 1;
            }
        }

        return 0;

    }

}
