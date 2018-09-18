package shiftinview.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ModifierUtils {

    private static ArrayList<String> lessFrequent=new ArrayList<String>(
            Arrays.asList("accidentally","never","not","less", "loosely","rarely" , "sometimes"));
    private static  ArrayList<String> moreFrequent = new ArrayList<String>(
            Arrays.asList("always","often","repeatedly", "regularly"));

    private static ArrayList<String>  amplifiers = new ArrayList<String>(
            Arrays.asList("completely","absolutely","heartily","so","well","really","literally",
                    "simply","for sure"));
    private static ArrayList<String> downtoners = new ArrayList<String>(
            Arrays.asList("kind of", "sort of", "mildly", "to some extent", "almost", "all but")
    );

    private static ArrayList<String> positiveManner = new ArrayList<String>(
            Arrays.asList("elegantly","beautifully", "confidently")
    );

    private static ArrayList<String> negativeManner = new ArrayList<>(
            Arrays.asList("lazily", "ugly", "faint heartedly")
    );

    public static ArrayList<String> getLessFrequent() {
        return lessFrequent;
    }

    public static ArrayList<String> getMoreFrequent() {
        return moreFrequent;
    }

    public static ArrayList<String> getAmplifiers() {
        return amplifiers;
    }

    public static ArrayList<String> getDowntoners() {
        return downtoners;
    }

    public static ArrayList<String> getPositiveManner() {
        return positiveManner;
    }

    public static ArrayList<String> getNegativeManner() {
        return negativeManner;
    }
}
