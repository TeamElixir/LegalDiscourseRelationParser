package SentimentAnnotator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ResultFilter {
    public static void main(String[] args) throws FileNotFoundException {
        String file = "/home/viraj/FYP/Gathika_s/LegalDisourseRelationParser/sentence-feature-extractor/src/main/resources/SentimentResults/sentimentResults.txt";
        Scanner sc = new Scanner(new File(file));

        ArrayList<String> regexList = new ArrayList<>();

        regexList.add("in other words");
        regexList.add("similarly");
        regexList.add("also acknowledge");
        regexList.add("on the basis of that");
        regexList.add("on the basis of those");
        regexList.add("on writ of certiorari");

        while(sc.hasNextLine()){
            String id = sc.nextLine();
            sc.nextLine();
            String line = sc.nextLine();

            for(String regex:regexList){
                if(line.toLowerCase().contains(regex)){
                    System.out.println(id);
                    break;
                }
            }

        }

    }

}
