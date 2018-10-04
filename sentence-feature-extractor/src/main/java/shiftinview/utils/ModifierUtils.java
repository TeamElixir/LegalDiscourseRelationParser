package shiftinview.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModifierUtils {

	private static ArrayList<String> lessFrequent = new ArrayList<String>(
			Arrays.asList("accidentally", "never", "not", "less", "loosely", "rarely", "sometimes"));

	private static ArrayList<String> moreFrequent = new ArrayList<String>(
			Arrays.asList("always", "often", "repeatedly", "regularly"));

	private static ArrayList<String> amplifiers = new ArrayList<String>(
			Arrays.asList("completely", "absolutely", "heartily", "so", "well", "really", "literally",
					"simply", "for sure"));

	private static ArrayList<String> downtoners = new ArrayList<String>(
			Arrays.asList("kind of", "sort of", "mildly", "to some extent", "almost", "all but")
	);

	private static ArrayList<String> positiveManner = new ArrayList<String>(
			Arrays.asList("elegantly", "beautifully", "confidently", "gladly", "gracefully",
					"happily", "honestly", "innocently", "inquisitively", "joyously", "kindly",
					"neatly", "obediently", "rightfully", "safely")
	);

	private static ArrayList<String> negativeManner = new ArrayList<>(
			Arrays.asList("lazily", "ugly", "faint heartedly", "cruelly", "angrily", "badly", "blindly",
					"defiantly", "greedily", "hungrily", "irritably", "obnoxiously", "painfully", "poorly",
					"reluctantly")
	);

	private static List<String> negativeWords = new ArrayList<>(
			Arrays.asList("never", "not", "nothing", "no", "insufficient"));

	private static ArrayList<String> transitionWords = new ArrayList<>(
			Arrays.asList("therefore", "thus", "accordingly", "first", "second", "third", "similarly"));

	private static ArrayList<String> transitionPhases = new ArrayList<>(
			Arrays.asList("as a result", "in such cases", "because of that",
					"in conclusion", "according to that", "in other words", "also acknowledge", "on the basis of that",
					"on the basis of those", "on writ of certiorari"));

	public static ArrayList<String> getTransitionWords() {
		return transitionWords;
	}

	public static ArrayList<String> getTransitionPhases() {
		return transitionPhases;
	}

	public static List<String> getNegativeWords() {
		return negativeWords;
	}

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
