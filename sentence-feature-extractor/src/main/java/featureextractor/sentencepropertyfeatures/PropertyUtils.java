package featureextractor.sentencepropertyfeatures;

import java.util.ArrayList;
import java.util.Arrays;

public class PropertyUtils {

    private static final ArrayList<String> ellaborationPhrases = new ArrayList<String>(
            Arrays.asList("To make that", "in the first place", "not only",
                    "but also", "as well as", "as a matter of fact", "together with", "in like manner",
                     "of course", "in addition", "coupled with",
                    "in the same fashion", "in the same way",
                     "not to mention", "to say nothing of",
                     "equally important", "by the same token",
                    "different from", "on the other hand",  "at the same time",  "in spite of",  "even so ",
                    "even though","as much as",
                     "be that as it may",
                    "after all", "with this intention","so that","even if","with this in mind","because of this",
                    "due to this", "as mentioned here","in other words",
                    "in fact","in general", "including this","in this case","for example","for this reason","for instance",
                    "to put it", "to demonstrate this", "that is to say", "on the positive side",
                     "with this in mind", "as a result",
                    "in that case", "in effect"

            ));

    private static final ArrayList<String> ellaborationWords = new ArrayList<String>(
            Arrays.asList( "again", "to", "moreover", "more","and", "also","then", "likewise",
                    "equally","comparatively", "identically","correspondingly","first","uniquely",
                    "similarly", "like", "furthermore", "as", "additionally","too","although" ,
                    "but","still", "instead","unlike","whereas", "despite","yet", "conversely",
                    "otherwise","nevertheless","besides", "rather","nonetheless","regardless","above",
                    "notwithstanding","that", "those", "such","indeed","especially","consequently", "thus",
                    "therefore","thereupon","forthwith","hence", "accordingly", "henceforth")
    );

    private static final ArrayList<String> changeOfTopicsWords = new ArrayList<String>(
            Arrays.asList("in contrast","on the contrary","on the negative side",
                    "however","albeit"
                    )
    );

    public static ArrayList<String> getEllaborationWords() {
        return ellaborationWords;
    }

    public static ArrayList<String> getEllaborationPhrases() {
        return ellaborationPhrases;
    }

    public static ArrayList<String> getChangeOfTopicsWords() {
        return changeOfTopicsWords;
    }
}
