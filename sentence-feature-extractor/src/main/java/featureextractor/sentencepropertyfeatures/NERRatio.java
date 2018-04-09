package featureextractor.sentencepropertyfeatures;

import utils.NLPUtils;

public class NERRatio {

	private String sourceSentence;
	private String targetSentence;
	private NLPUtils nlpUtils;

	public NERRatio(String sourceSentence, String targetSentence, NLPUtils nlpUtils) {
		this.sourceSentence = sourceSentence;
		this.targetSentence = targetSentence;
		this.nlpUtils = nlpUtils;
	}

	public double getRatio() {
		int entitiesNoSource = nlpUtils.getEntities(sourceSentence).size();
		int entitiesNoTarget = nlpUtils.getEntities(targetSentence).size();

		int maxNoEntities = Math.max(entitiesNoSource, entitiesNoTarget);

		if (maxNoEntities == 0) {
			return 0;
		}
		return (double) entitiesNoSource / (double) maxNoEntities;
	}
}
