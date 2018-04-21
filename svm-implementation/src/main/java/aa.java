import libsvm.*;

import java.io.IOException;

public class aa {
     static double[][] train = new double[1000][];
     static double[][] test = new double[3][];

     public static void main(String[] args) {
          double[] featureTest1 = {1,0,600};
          test[0] = featureTest1;
          double[] featureTest2 = {1,0,700};
          test[1] = featureTest2;
          double[] featureTest3 = {0,0,-200};
          test[2] = featureTest3;

          for (int i = 0; i < train.length; i++){
               if (i+1 > (train.length/2)){        // 50% positive
                    double[] vals = {1,0,i+i};
                    train[i] = vals;
               } else {
                    double[] vals = {0,0,i-i-i-2}; // 50% negative
                    train[i] = vals;
               }
          }
          //svm_model svmModel = svmTrain();
          try {
               double vv = evaluate(featureTest1,svm.svm_load_model("model.txt"));
          } catch (IOException e) {
               e.printStackTrace();
          }
         /* try {
               svm.svm_save_model("model.txt",svmModel);
          } catch (IOException e) {
               e.printStackTrace();
          }*/

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
          param.probability = 0;
          param.gamma = 0.5; //1/number of features
          param.nu = 0.5;
          param.C = 1;
          param.svm_type = svm_parameter.C_SVC;
          param.kernel_type = svm_parameter.RBF;
          param.cache_size = 20000;
          param.eps = 0.001;

          svm_model model = svm.svm_train(prob, param);

          return model;
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

          int totalClasses = 2;
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
