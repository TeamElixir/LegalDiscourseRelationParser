package annotationreader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Run {

	public static void main(String[] args) throws Exception{

		Properties props = new Properties();
		props.setProperty("user", "root");
		props.setProperty("password", "123456");

		Connection connection = DriverManager.getConnection("jdbc:mysql://206.81.15.81:3306/discourse_annotator",props);

		System.out.println("ok");

	}

}
