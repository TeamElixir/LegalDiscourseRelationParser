package annotationreader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class Run {
	static ArrayList<AnnotationRelation> arList = new ArrayList<>();
	static ArrayList<ArrayList<Double>> clusterAnnotations = new ArrayList<>();
	static ArrayList<Integer> clusterIds = new ArrayList<>();


	public static void main(String[] args) throws Exception {

		Properties props = new Properties();
		props.setProperty("user", "root");
		props.setProperty("password", "123456");

		Connection connection = DriverManager.getConnection("jdbc:mysql://206.81.15.81:3306/discourse_annotator", props);
		Statement statement = connection.createStatement();

		String sql1 = "select pair_user_annotations.pair_id, cluster_id, user_id, sentence_pairs_from_algorithm.relation,pair_user_annotations.relation as user_annotation from pair_user_annotations left join pair_clusters on pair_clusters.pair_id = pair_user_annotations.pair_id left join sentence_pairs_from_algorithm on pair_user_annotations.pair_id = sentence_pairs_from_algorithm.id ORDER BY `pair_user_annotations`.`pair_id` ASC ";

		ResultSet resultSet1 = statement.executeQuery(sql1);



		DBRelation dbrP = new DBRelation();

		AnnotationRelation ar;

		boolean count = true;
		while (resultSet1.next()) {

			if(count){
				dbrP.pairId = resultSet1.getInt("pair_id");
				dbrP.clusterId = resultSet1.getInt("cluster_id");
				dbrP.userId = resultSet1.getInt("user_id");
				dbrP.svmRelation = resultSet1.getInt("relation");
				dbrP.userRelation = resultSet1.getInt("user_annotation");
				count=false;
			}else{
				if (dbrP.pairId == resultSet1.getInt("pair_id")) {
					ar = new AnnotationRelation();

					ar.pairId = resultSet1.getInt("pair_id");
					ar.clusterId = resultSet1.getInt("cluster_id");
					ar.user1Id = dbrP.userId;
					ar.user2Id = resultSet1.getInt("user_id");
					ar.svmRelation = getRelation(resultSet1.getInt("relation"));
					ar.user1Relation = dbrP.userRelation;
					ar.user2Relation = resultSet1.getInt("user_annotation");

					arList.add(ar);
				}

				dbrP.pairId = resultSet1.getInt("pair_id");
				dbrP.clusterId = resultSet1.getInt("cluster_id");
				dbrP.userId = resultSet1.getInt("user_id");
				dbrP.svmRelation = resultSet1.getInt("relation");
				dbrP.userRelation = resultSet1.getInt("user_annotation");
			}
		}

		System.out.println("ok");
		/*for(AnnotationRelation ar1 :arList ){
			System.out.println(" user1 : "+ar1.user1Id+" user1Rel : "+ar1.user1Relation+ " pairID : " + ar1.pairId + " svmRelation : " + ar1.svmRelation);
			System.out.println("user2 : "+ar1.user2Id+" uer2Rel : "+ar1.user2Relation+ " pairID : " + ar1.pairId);
		}*/
		//System.out.println(checkPrecision(4));
		//System.out.println(checkRecall(1));
		analyzeShiftinView();
		//generateClusters();
	}

	public static double checkPrecision(int type){
				double svmCount = 0;
				double userCount = 0;
				for(AnnotationRelation ar2:arList){
					if(ar2.svmRelation==type){
						if(ar2.user1Relation==ar2.user2Relation){
							svmCount++;
							if(ar2.user1Relation==ar2.svmRelation){
								userCount++;
							}
						}
					}
				}

				double precision= userCount/svmCount;
		System.out.println(userCount);
		System.out.println(svmCount);
				return precision;


	}

	public static double checkRecall(int type){
		double userCount=0;
		double svmCount=0;
		for (AnnotationRelation ar3:arList){
			if(ar3.user1Relation == type && ar3.user1Relation == ar3.user2Relation){
				userCount++;
				if(ar3.svmRelation==type){
					svmCount++;
				}
			}
		}
		System.out.println(svmCount);
		System.out.println(userCount);
		double recall = svmCount/userCount;
		return recall;
	}

	private static void analyzeShiftinView(){
		double bothAgree = 0;
		double oneAgree = 0;
		double otherElaboration = 0;
		double fullPairs = 0;
		double systemElaboration = 0;
		for (AnnotationRelation ar4:arList){
			fullPairs++;
			if(ar4.user1Relation==ar4.user2Relation && ar4.user1Relation==5){
				bothAgree ++;
				if(ar4.svmRelation==2){
					//systemElaboration++;
				}
			}else if(ar4.user1Relation==5){
				oneAgree++;
				if(ar4.user2Relation==2){
					otherElaboration++;
				}
				if(ar4.svmRelation==2){
					systemElaboration++;
				}
			}else if(ar4.user2Relation==5){
				oneAgree++;
				if(ar4.user1Relation==2){
					otherElaboration++;
				}
				if(ar4.svmRelation==2){
					systemElaboration++;
				}
			}
		}
		System.out.println("bothAgree : " + bothAgree);
		System.out.println("oneAgree : " + oneAgree);
		System.out.println("otherElaboration : "+otherElaboration);
		System.out.println("fullpairs :" + fullPairs);
		System.out.println("systemElaboration : "+systemElaboration);

	}

	private static int getRelation(int no){
		/** Elaboration **/
		if(no==2 || no==8 || no==4 || no==13 || no==12 || no==11 ||
				no==18 || no==14 || no==15 || no==6 || no==16 || no==9){
			return 2;
		/** Redundancy **/
		}else if(no==1){
			return 3;
		/** Citation **/
		}else if(no==7){
			return 4;
		/** Shift in View **/
		}else if(no==17 || no==5){
			return 5;
		/** No Relation **/
		}else {
			return 1;
		}
	}

	public static void generateClusters(){
		int clusterID = -1;
		double similarUS1US2 = 0;
		double similarSyUS1 = 0;
		double similarSyUS2 = 0;


		for (AnnotationRelation ar5:arList){

			if(clusterIds.contains(ar5.clusterId)){
				    int clusterIndex =clusterIds.indexOf(ar5.clusterId);
					ArrayList<Double> ratios1=clusterAnnotations.get(clusterIndex);
				double US1US2 = ratios1.get(0);
				double SUS1 = ratios1.get(1);
				double SUS2 = ratios1.get(2);
				if(ar5.user1Relation==ar5.user2Relation){
					US1US2++;
					ratios1.set(0,US1US2);

				}
				if(ar5.svmRelation==ar5.user1Relation) {
					SUS1++;
					ratios1.set(1,SUS1);
				}
				if(ar5.svmRelation==ar5.user2Relation){
					SUS2++;
					ratios1.set(2,SUS2);
				}
				clusterAnnotations.set(clusterIndex,ratios1);

			}
			else{

				ArrayList<Double> ratios= new ArrayList<>();
				ratios.add(0.0);
				ratios.add(0.0);
				ratios.add(0.0);
				double US1US2 = 0.0;
				double SUS1 = 0.0;
				double SUS2 = 0.0;
				if(ar5.user1Relation==ar5.user2Relation){
					US1US2++;
					ratios.set(0,US1US2);

				}
				if(ar5.svmRelation==ar5.user1Relation) {
					SUS1++;
					ratios.set(1,SUS1);
				}
				if(ar5.svmRelation==ar5.user2Relation){
					SUS2++;
					ratios.set(2,SUS2);
				}
				clusterIds.add(ar5.clusterId);
				clusterAnnotations.add(ratios);
			}
		}
		for (int i=0;i<clusterAnnotations.size();i++){
			similarUS1US2 += clusterAnnotations.get(i).get(0)/5.0;
			similarSyUS1 +=clusterAnnotations.get(i).get(1)/5.0;
			similarSyUS2 +=clusterAnnotations.get(i).get(2)/5.0;

		};
		double finalUS1US2 = similarUS1US2/clusterAnnotations.size();
		double finalSyUS1 = similarSyUS1/clusterAnnotations.size();
		double finalSyUS2 = similarSyUS2/clusterAnnotations.size();
		double finalSyUS2US1 = (finalSyUS1+finalSyUS2)/2.0;
		System.out.println("US1US2 : " + finalUS1US2);
		System.out.println("SyUS1 : "+finalSyUS1);
		System.out.println("SyUS2 : "+finalSyUS2);
		System.out.println("SyUS1US2 : "+finalSyUS2US1);
	}

}

class DBRelation {

	public int pairId;

	public int clusterId;

	public int userId;

	public int svmRelation;

	public int userRelation;
}

class AnnotationRelation {

	public int pairId;

	public int clusterId;

	public int user1Id;

	public int user2Id;

	public int svmRelation;

	public int user1Relation;

	public int user2Relation;
}



