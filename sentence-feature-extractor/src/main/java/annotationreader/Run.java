package annotationreader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class Run {

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();
		props.setProperty("user", "root");
		props.setProperty("password", "123456");

		Connection connection = DriverManager.getConnection("jdbc:mysql://206.81.15.81:3306/discourse_annotator", props);
		Statement statement = connection.createStatement();

		String sql1 = "select pair_user_annotations.pair_id, cluster_id, user_id, sentence_pairs_from_algorithm.relation,pair_user_annotations.relation as user_annotation from pair_user_annotations left join pair_clusters on pair_clusters.pair_id = pair_user_annotations.pair_id left join sentence_pairs_from_algorithm on pair_user_annotations.pair_id = sentence_pairs_from_algorithm.id ORDER BY `pair_user_annotations`.`pair_id` ASC ";

		ResultSet resultSet1 = statement.executeQuery(sql1);

		ArrayList<AnnotationRelation> arList = new ArrayList<>();

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
		for(AnnotationRelation ar1 :arList ){
			System.out.println(" user1 : "+ar1.user1Id+" user1Rel : "+ar1.user1Relation+ " pairID : " + ar1.pairId + " svmRelation : " + ar1.svmRelation);
			System.out.println("user2 : "+ar1.user2Id+" uer2Rel : "+ar1.user2Relation+ " pairID : " + ar1.pairId);
		}
	}

	public int svmToLegalRelation(int svmRelation){

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



