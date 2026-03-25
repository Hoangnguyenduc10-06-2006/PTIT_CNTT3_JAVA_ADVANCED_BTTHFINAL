
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {

    private static final  String DRIVER ="com.mysql.cj.jdbc.Driver";
    private static final  String URL ="jdbc:mysql://localhost:3306/JDBS_JAVA_ADVANCED_BTHHFINAL?createDatabaseIfNotExist=true";
    private static final  String USERNAME ="root";
    private static final  String PASSWORD ="Hoangduc2006";

    public static Connection openConnection(){
        Connection connection ;
        try {
            Class.forName(DRIVER);

            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

        } catch (ClassNotFoundException |SQLException e) {
            throw new RuntimeException(e);
        }
        return connection ;
    }
}
