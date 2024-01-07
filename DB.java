import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Jijah4321.*";

    public static Connection connect() throws SQLException {
        try {
            // Memuat driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Membuat koneksi ke database
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan.", e);
        }
    }

    public static void main(String[] args) {
        try {
            // Membuat koneksi
            Connection connection = connect();
            System.out.println("Koneksi ke database berhasil!");
            // Lakukan operasi database di sini

            // Tutup koneksi setelah selesai
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
