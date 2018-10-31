package featureextractor;

import java.util.ArrayList;
import java.util.Properties;

import datasetparser.models.FeatureEntry;
import utils.NLPUtils;
import utils.SQLiteUtils;

/**
 * To Run any temporary algorithms
 */
public class Run {

	public static void main(String[] args) throws Exception {
		/**
		ArrayList<FeatureEntry> featureEntries = FeatureEntry.getAll();
		SQLiteUtils sqLiteUtils = new SQLiteUtils();

		for (int i = 0; i < featureEntries.size(); i++) {
			double semScore = featureEntries.get(i).getSemanticSimilarityScore();

			semScore = semScore * 2.0;
			if (semScore > 1) {
				semScore = 1;
			}

			String sql = "UPDATE FEATURE_ENTRY SET SEMANTIC_SCORE=" + semScore +
					" WHERE ID=" +featureEntries.get(i).getDbId();

			sqLiteUtils.executeUpdate(sql);
		}
		 **/

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref");
		props.setProperty("coref.algorithm", "statistical");
		NLPUtils nlpUtils = new NLPUtils(props, "http://104.248.226.230", 9000);

		String text = "group attack was very cornerstone of Government's case, and virtually every witness to crime agreed that Fuller was killed by large group";

		nlpUtils.annotate(text);

		System.out.println("done");

	}

}
