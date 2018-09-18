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
    )


}
