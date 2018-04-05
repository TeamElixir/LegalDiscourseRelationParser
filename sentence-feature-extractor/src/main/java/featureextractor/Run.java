package featureextractor;

import java.util.ArrayList;
import java.util.Arrays;

import utils.NLPUtils;

public class Run {

	public static void main(String[] args) {

		NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos");

		ArrayList<String> nouns = nlpUtils.getNouns("Thejan loves me more than Linda loves me");
		System.out.println("Nouns : " + nouns.toString());

		ArrayList<String> verbs = nlpUtils.getVerbs("My brown dog is beautiful.");
		ArrayList<String> verbsWithOutBe = nlpUtils.getVerbsWithOutBe("My brown dog is beautiful.");
		System.out.println("Verbs : " + verbs.toString());
		System.out.println("VerbsWithOutBe : " + verbsWithOutBe.toString());

		ArrayList<String> adjectives = nlpUtils.getAdjectives("My brown dog is beautiful.");
		System.out.println("Adjectives : " + adjectives.toString());
	}




}
