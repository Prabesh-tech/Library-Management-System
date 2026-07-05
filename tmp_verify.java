import java.sql.Connection;
import util.DataBaseConnection;

public class tmp_verify {
    public static void main(String[] args) {
        Connection conn = DataBaseConnection.getConnection();
        System.out.println(conn == null ? "null" : "connected");
    }
}
