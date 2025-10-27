import java.sql.*;

public class TestH2 {
    public static void main(String[] args) {
        String url = "jdbc:h2:file:/Users/ricardo/Workspace/webdsl-spike/metamodel/metamodel_db";

        System.out.println("Testing H2 connection...");
        System.out.println("JDBC URL: " + url);

        try {
            // Try with empty credentials
            System.out.println("\n1. Trying connection with no user/pass...");
            try (Connection conn = DriverManager.getConnection(url)) {
                System.out.println("SUCCESS! Connected with no credentials.");
                testConnection(conn);
                return;
            } catch (SQLException e) {
                System.out.println("Failed: " + e.getMessage());
            }

            // Try with sa/empty
            System.out.println("\n2. Trying connection with sa/empty...");
            try (Connection conn = DriverManager.getConnection(url, "sa", "")) {
                System.out.println("SUCCESS! Connected with sa/empty.");
                testConnection(conn);
                return;
            } catch (SQLException e) {
                System.out.println("Failed: " + e.getMessage());
            }

            // Try with sa/sa
            System.out.println("\n3. Trying connection with sa/sa...");
            try (Connection conn = DriverManager.getConnection(url, "sa", "sa")) {
                System.out.println("SUCCESS! Connected with sa/sa.");
                testConnection(conn);
                return;
            } catch (SQLException e) {
                System.out.println("Failed: " + e.getMessage());
            }

            System.out.println("\n❌ All connection attempts failed!");

        } catch (Exception e) {
            System.out.println("\n❌ FATAL ERROR:");
            e.printStackTrace();
        }
    }

    private static void testConnection(Connection conn) throws SQLException {
        System.out.println("\n✅ Connection successful!");
        DatabaseMetaData meta = conn.getMetaData();
        System.out.println("Database: " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());

        // List tables
        System.out.println("\nTables found:");
        ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"});
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.println("  " + count + ". " + rs.getString("TABLE_NAME"));
        }

        if (count == 0) {
            System.out.println("  (no tables found - database is empty)");
        }
    }
}
