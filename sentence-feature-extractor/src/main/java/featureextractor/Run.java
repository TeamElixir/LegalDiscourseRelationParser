package featureextractor;

import utils.NLPUtils;

public class Run {

	public static void main(String[] args) {

		NLPUtils nlpUtils = new NLPUtils("tokenize,ssplit,pos");

		nlpUtils.getNouns("My brown dog also likes eating sausage.");

	}




}
