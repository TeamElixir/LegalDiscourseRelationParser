import libsvm.*;

public class aa {
     private svm_model svmTrain() {
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
          param.gamma = 0.5;
          param.nu = 0.5;
          param.C = 1;
          param.svm_type = svm_parameter.C_SVC;
          param.kernel_type = svm_parameter.LINEAR;
          param.cache_size = 20000;
          param.eps = 0.001;

          svm_model model = svm.svm_train(prob, param);

          return model;
     }
}
