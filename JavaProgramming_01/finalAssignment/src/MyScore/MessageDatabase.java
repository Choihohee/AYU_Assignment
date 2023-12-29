package MyScore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MessageDatabase {
    private static final String JDBC_URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // 메시지 수를 저장할 테이블 생성 SQL
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS StudentMessage (" +
            "studentId VARCHAR(20) PRIMARY KEY," +
            "messageCount INT" +
            ")";

    // 메시지 수를 조회하는 SQL
    private static final String SELECT_MESSAGE_COUNT_SQL = "SELECT messageCount FROM StudentMessage WHERE studentId = ?";

    // 메시지 수를 업데이트하는 SQL
    private static final String UPDATE_MESSAGE_COUNT_SQL = "MERGE INTO StudentMessage (studentId, messageCount) " +
            "KEY (studentId) VALUES (?, COALESCE((SELECT messageCount FROM StudentMessage WHERE studentId = ?), 0) + 1)";

    // H2 데이터베이스 연결
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    // 데이터베이스 초기화 및 연결
    static {
        try (Connection connection = getConnection();
                PreparedStatement createTableStatement = connection.prepareStatement(CREATE_TABLE_SQL)) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 보낸 메시지 갯수를 조회하는 함수
    public static int getSentMessagesCount(String studentId) {
        try (Connection connection = getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(SELECT_MESSAGE_COUNT_SQL)) {
            selectStatement.setString(1, studentId);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("messageCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 보낸 메시지 갯수를 업데이트하는 함수
    public static void updateSentMessagesCount(String studentId) {
        try (Connection connection = getConnection();
                PreparedStatement updateStatement = connection.prepareStatement(UPDATE_MESSAGE_COUNT_SQL)) {
            updateStatement.setString(1, studentId);
            updateStatement.setString(2, studentId);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
