package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class WordUtils {

    private static ArrayList<String> stopWords =new ArrayList<>(Arrays.asList("a","the"));


    public static ArrayList<String> getStopWords() {
        return stopWords;
    }
}
