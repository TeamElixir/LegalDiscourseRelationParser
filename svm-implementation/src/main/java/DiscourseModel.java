import libsvm.*;
import models.FeatureEntryDB;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DiscourseModel {
    static double numberOfFeatures = 16;
    static double[][] train ;
    static ArrayList<Integer> types= new ArrayList();
    static int zeroCount =0;
    public static void main(String[] args) {
       /*initializeTrainingData();
        svm_model svmModel = svmTrain();
        try {
            svm.svm_save_model("discourseModel.txt",svmModel);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        double[] featureTest = {1.0,
                0.0,
                0.0,
                0.0,
                0.03870967741935484,
                0.44642857142857145,
                0.5,
                0.0,
                0.0,
                0.24878336464751513,
                0.0,
                0.0,
                0.0,
                0.0,
                0.18181818181818182,
                0.14814814814814814,
                0.16395645894598826,

        };
        System.out.println(types.toString());
        System.out.println(types.size());
        try {
            double v = evaluate(featureTest,svm.svm_load_model("discourseModel.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static svm_model svmTrain() {
        svm_problem prob = new svm_problem();
        int dataCount = train.length;
        prob.y = new double[dataCount];
        prob.l = dataCount;
        prob.x = new svm_node[dataCount][];

        for (int i = 0; i < dataCount; i++){
            double[] features = train[i];
            prob.x[i] = new svm_node[features.length-1];
            for (int j = 1; j < features.length; j++){
                svm_node node = new svm_node();
                node.index = j;
                node.value = features[j];
                prob.x[i][j-1] = node;
            }
            prob.y[i] = features[0];
        }

        svm_parameter param = new svm_parameter();
        param.probability = 1;
        param.gamma = 1/numberOfFeatures; //1/number of features
        param.nu = 0.5;
        param.C = 1;
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;
        param.cache_size = 20000;
        param.eps = 0.001;

        svm_model model = svm.svm_train(prob, param);

        return model;
    }

    private static void initializeTrainingData(){
        FeatureEntryDB fEDB = new FeatureEntryDB();
        try {
            ArrayList<FeatureEntryDB> featureEntryDBS = fEDB.getAll();
            int trainingSetSize = featureEntryDBS.size();
            train = new double[trainingSetSize][];
            for(int i=0;i<trainingSetSize;i++){
                FeatureEntryDB featureEntryDB = featureEntryDBS.get(i);
                if(!types.contains(featureEntryDB.getType())){
                    types.add(featureEntryDB.getType());
                }

                double[] vals = {
                        featureEntryDB.getType(),
                        featureEntryDB.getAdjectiveSimilarity(),
                        featureEntryDB.getChangeTransitionScore(),
                        featureEntryDB.getEllaborationTransitionScore(),
                        featureEntryDB.getLcs(),
                        featureEntryDB.getLengthRatio(),
                        featureEntryDB.getNerRatio(),
                        featureEntryDB.getNounSimilarity(),
                        featureEntryDB.getObjectOverlap(),
                        featureEntryDB.getSemanticSimilarityScore(),
                        featureEntryDB.getSubjectNounOverlap(),
                        featureEntryDB.getSubjectOverlap(),
                        featureEntryDB.getTosScore(),
                        featureEntryDB.getVerbSimilarity(),
                        featureEntryDB.getWordOverlapSSent(),
                        featureEntryDB.getWordOverlapTSent(),
                        featureEntryDB.getWordSimilarity()
                };
                train[i]= vals;
            }
            System.out.println("readFeatureEntryDBs");


        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("typees:"+types);

    }

    public static double evaluate(double[] features, svm_model model)
    {
        svm_node[] nodes = new svm_node[features.length-1];
        for (int i = 1; i < features.length; i++)
        {
            svm_node node = new svm_node();
            node.index = i;
            node.value = features[i];

            nodes[i-1] = node;
        }

        int totalClasses = 15;
        int[] labels = new int[totalClasses];
        svm.svm_get_labels(model,labels);

        double[] prob_estimates = new double[totalClasses];
        double v = svm.svm_predict_probability(model, nodes, prob_estimates);

        for (int i = 0; i < totalClasses; i++){
            System.out.print("(" + labels[i] + ":" + prob_estimates[i] + ")");
        }
        System.out.println("(Actual:" + features[0] + " Prediction:" + v + ")");

        return v;
    }

}
