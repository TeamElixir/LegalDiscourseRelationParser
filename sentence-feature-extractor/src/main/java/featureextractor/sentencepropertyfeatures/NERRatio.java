package featureextractor.sentencepropertyfeatures;

import edu.stanford.nlp.pipeline.Annotation;
import utils.NLPUtils;

public class NERRatio {

	private Annotation sourceAnnotation;
	private Annotation targetAnnotation;
	private NLPUtils nlpUtils;

	public NERRatio(Annotation sourceAnnotation, Annotation targetAnnotation, NLPUtils nlpUtils) {
		this.sourceAnnotation = sourceAnnotation;
		this.targetAnnotation = targetAnnotation;
		this.nlpUtils = nlpUtils;
	}

	public double getRatio() {
		int entitiesNoSource = nlpUtils.getEntities(sourceAnnotation).size();
		int entitiesNoTarget = nlpUtils.getEntities(targetAnnotation).size();

		int maxNoEntities = Math.max(entitiesNoSource, entitiesNoTarget);

		if (maxNoEntities == 0) {
			return 0;
		}
		return (double) entitiesNoSource / (double) maxNoEntities;
	}
}
