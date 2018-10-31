package annotationreader;

import shiftinview.resultsAnalyzer.resultShiftInView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Run {
	static ArrayList<AnnotationRelation> arList = new ArrayList<>();
	static ArrayList<ArrayList<Double>> clusterAnnotations = new ArrayList<>();
	static ArrayList<Integer> clusterIds = new ArrayList<>();
	public static ArrayList verbMethod;
	public static ArrayList<Integer> userShifts=new ArrayList<>(
			Arrays.asList(21,25,31,33,569,77,81,1137,627,125,695,199,
					201,753,1793,277,805,815,427,489,181,189,263,363,395,401));


	public static void main(String[] args) throws Exception {

		Properties props = new Properties();
		props.setProperty("user", "root");
		props.setProperty("password", "123456");

		Connection connection = DriverManager.getConnection("jdbc:mysql://142.93.244.96:3306/discourse_annotator", props);
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

					/*if(arList.size()<180){
						arList.add(ar);
					}*/
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
		for(AnnotationRelation relation:arList){
			if(relation.svmRelation==1 && relation.user1Relation==1 && relation.user2Relation==1){
				//System.out.println(relation);
			}
		}



		/*for(AnnotationRelation ar1 :arList ){
			System.out.println(" user1 : "+ar1.user1Id+" user1Rel : "+ar1.user1Relation+ " pairID : " + ar1.pairId + " svmRelation : " + ar1.svmRelation);
			System.out.println("user2 : "+ar1.user2Id+" uer2Rel : "+ar1.user2Relation+ " pairID : " + ar1.pairId);
		}*/
		//checkCitationOR();
		//System.out.println(checkPrecision(2));

		//System.out.println(checkRecall(2));

		/*System.out.println(precisionOR(1));
		System.out.println("Recall");
		System.out.println(checkRecallOR(1));*/
		//analyzeShiftinView();
		//generateClusters();
		//System.out.println("Elabo");
		//confusionMatrixBoth(5);
		/*System.out.println("");
		System.out.println("No Relation");
		confusionMatrixBoth(1);
		System.out.println("Shift In View");
		confusionMatrixBoth(5);
		System.out.println("");
		System.out.println("Redundancy");
		confusionMatrixBoth(3);
		System.out.println("");
		confusionMatrixBoth(4);
		System.out.println("");
		systemPredictCountFromBoth();
		System.out.println("");
		User1User2();*/

		//getShiftInView();
		//checkPrecision(1);
		checkRecall(2);


		/*System.out.println("user");
		usersCorelationByType(1);
		System.out.println("system");
		corelationByType1(1);*/ //correct way
		//systemusersCorelationByType(1); //will not be useful
	}

	public static double checkPrecision(int type){
		verbMethod= resultShiftInView.getShiftInView();
				double svmCount = 0;
				double userCount = 0;
				for(AnnotationRelation ar2:arList){
					if(ar2.svmRelation==type){
						if(ar2.user1Relation==ar2.user2Relation){
							svmCount++;
							boolean shift=false;
							if(verbMethod.contains(ar2.pairId)){
								System.out.println("pId"+ar2.pairId);
								shift=true;
							}
							if(ar2.user1Relation==ar2.svmRelation){
								userCount++;
								/*if(shift) {
									System.out.println("pId" + ar2.pairId);
								}*/
							}
						}
					}
				}

				double precision= userCount/svmCount;
		System.out.println("ElaborationUser :"+ userCount);
		System.out.println("ElaborationSVm : "+ svmCount);
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
					if(userShifts.contains(ar3.pairId)){
						System.out.println("pID : "+ar3.pairId);
					}
				}
			}
		}
		System.out.println("SVM : "+svmCount);
		System.out.println("user : "+userCount);
		double recall = svmCount/userCount;
		return recall;
	}

	public static double checkRecallOR(int type){
		double userCount = 0;
		double svmCount = 0;
		for (AnnotationRelation ar3:arList){
			if (ar3.user1Relation == type || ar3.user2Relation==type){
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

	public static int getRelation(int no){
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

	public static void systemPredictCountFromBoth(){

		double elabo = 0;
		double noRelation = 0;
		double citation = 0;
		double shift_in_view = 0;
		double redundancy = 0;

		for (AnnotationRelation ar6 : arList){
			if(ar6.user1Relation==ar6.user2Relation){

					if(ar6.svmRelation == 1){
						noRelation++;
					}
					if(ar6.svmRelation==2) {
						elabo++;
					}
					if (ar6.svmRelation==3){
						redundancy++;
					}
					if (ar6.svmRelation==4){
						citation++;
					}
					if(ar6.svmRelation==5){
						shift_in_view++;
					}
				}

		}

		System.out.println("systemElabo : "+elabo);
		System.out.println("systemNoRelation : "+noRelation);
		System.out.println("system : "+ redundancy);
		System.out.println("systemElabo : "+citation);
		System.out.println("systemElabo : "+shift_in_view);
	}

	public  static void User1User2(){
		double user1user2=0;
		for (AnnotationRelation ar : arList){
			if(ar.user1Relation==ar.user2Relation){
				user1user2++;
			}
		}
		System.out.println("User 1 and User 2: "+ user1user2);
	}


	public static void confusionMatrixBoth(int type){
		double totalUser = 0;
		double elabo = 0;
		double noRelation = 0;
		double citation = 0;
		double shift_in_view = 0;
		double redundancy = 0;


		for (AnnotationRelation ar6 : arList){
			if(ar6.user1Relation==ar6.user2Relation){

				if(ar6.user1Relation == type){
					totalUser++;
					if(ar6.svmRelation == 1){
						noRelation++;
					}else if(ar6.svmRelation==2){
						elabo++;
					}else if (ar6.svmRelation==3){
						redundancy++;
					}else if (ar6.svmRelation==4){
						citation++;
					}else if(ar6.svmRelation==5){
						shift_in_view++;
					}
				}
			}
		}

		double elaboRatio = elabo/totalUser;
		double noRelationRatio = noRelation/totalUser;
		double shift_in_view_Ratio = shift_in_view/totalUser;
		double citationRatio = citation/totalUser;
		double redundancyRatio = redundancy/totalUser;
		System.out.println(elabo);
		System.out.println(totalUser);
		System.out.println("elabo: "+elaboRatio);
		System.out.println("noRelation: "+noRelationRatio);
		System.out.println("shiftInView: "+shift_in_view_Ratio);
		System.out.println("Citation: "+citationRatio);
		System.out.println("Redundancy: "+redundancyRatio);
		System.out.println("Total: "+totalUser);
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

	public static double precisionOR(int type){
		double svmCount = 0;
		double userCount = 0;
		for(AnnotationRelation ar2:arList){
			if(ar2.svmRelation==type){
				svmCount++;
				if(ar2.user1Relation==type|| ar2.user2Relation==type){
						userCount++;
				}
			}
		}

		double precision= userCount/svmCount;
		System.out.println(userCount);
		System.out.println(svmCount);
		return precision;
	}

	public static void corelationByType1(int type){
             double US1=0;
             double US2=0;
             double Sys=0;
             double US1US2=0;
             double SysUS1=0;
             double SysUS2=0;
             double SysUS1US2=0;
             double total=0;
		int cS1U1=0;
		int cS1U2=0;

		for (AnnotationRelation ar7:arList) {
			int user1 = ar7.user1Relation;
			int user2 = ar7.user2Relation;
			int system = ar7.svmRelation;


			if( user1==type || user2==type || system==type){
				total++;
				if(user1==type && user2==type && system==type){
					SysUS1US2++;
				}else if(user1==type && user2==type){
					US1US2++;
				}else if (user1==type && system==type){
					SysUS1++;
				}else if (user2==type && system==type){
					SysUS2++;
				}else if(user1==type){
					user1++;
				}else if(user2==type){
					user2++;
				}else if(system==type){
					system++;
				}
			}
			if (user1==type && system==type && user2!=type){
				cS1U1++;
			}
			if (user1!=type && system==type && user2==type){
				cS1U2++;
			}

		}

		double allAgree=SysUS1US2;
		double usersAgree=SysUS1US2+US1US2;
		double systemUsersAgree=SysUS1US2+SysUS1+SysUS2;

		double atleast1Put = total;
		double userCorelation = usersAgree/atleast1Put;
		double systemUserCorelation = (systemUsersAgree)/atleast1Put;

		/*System.out.println("systemUserAgree "+ systemUsersAgree);
		System.out.println("SysUS1US2 "+ SysUS1US2);
		System.out.println(cS1U1);
		System.out.println(cS1U2);

		System.out.println("userCorelation: "+userCorelation);*/
		System.out.println("systemUserCorelation: "+systemUserCorelation);


	}

	public static void usersCorelationByType(int type){
		double US1=0;
		double US2=0;
		double Sys=0;
		double US1US2=0;
		double SysUS1=0;
		double SysUS2=0;
		double SysUS1US2=0;
		double total=0;

		for (AnnotationRelation ar7:arList) {
			int user1 = ar7.user1Relation;
			int user2 = ar7.user2Relation;
			int system = ar7.svmRelation;

			if( user1==type || user2==type ){
				total++;
				 if(user1==type && user2==type){
					US1US2++;
				}
			}
		}


		double userCorelation = US1US2/total;
		System.out.println("userCorelation "+userCorelation);


	}

	public static void systemusersCorelationByType(int type){
		double US1=0;
		double US2=0;
		double Sys=0;
		double US1US2=0;
		double SysUS1=0;
		double SysUS2=0;
		double SysUS1US2=0;
		double totalUS1=0;
		double totalUS2=0;

		for (AnnotationRelation ar7:arList) {
			int user1 = ar7.user1Relation;
			//int user2 = ar7.user2Relation;
			int system = ar7.svmRelation;

			if( user1==type || system==type ){
				totalUS1++;
				if(user1==type && system==type){
					SysUS1++;
				}
			}
		}

		for (AnnotationRelation ar7:arList) {
			//int user1 = ar7.user1Relation;
			int user2 = ar7.user2Relation;
			int system = ar7.svmRelation;

			if( user2==type || system==type ){
				totalUS2++;
				if(user2==type && system==type){
					SysUS2++;
				}
			}
		}
		double user1Corelation = SysUS1/totalUS1;
		double user2Corelation = SysUS2/totalUS2;
		double userCorelation=(user1Corelation*totalUS1+user2Corelation*totalUS2)/(totalUS1+totalUS2);
		System.out.println("userSystemCorelation "+userCorelation);


	}

	public static void checkCitationOR(){
		int count=0;
		for (AnnotationRelation ar:
			 arList) {
			if (ar.user1Relation==4 || ar.user2Relation==4){
				count++;
				System.out.println(ar.pairId);
			}

		}

	}

	public static void getShiftInView(){
		int count=0;
		for (AnnotationRelation ar:
				arList) {
			if (ar.user1Relation==5 || ar.user2Relation==5){
				System.out.println("pairdID");
				System.out.println(ar.pairId);

				count++;
			}


		}
		System.out.println(count);
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



