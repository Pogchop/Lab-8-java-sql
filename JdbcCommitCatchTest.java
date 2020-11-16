package Lab8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcCommitCatchTest {
    public JdbcCommitCatchTest() {
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookshop", "root", "");

        try {
            Statement stmt = conn.createStatement();

            try {
                try {
                    conn.setAutoCommit(false);
                    stmt.executeUpdate("insert into books values(4001,'Paul Chan','Mahjong 101',4.4,4)");
                    conn.commit();
                    stmt.executeUpdate("insert into books values (4001,'Peter Chan','Mahjong 102',4.4,4)");
                    conn.commit();
                } catch (SQLException var7) {
                    System.out.println("---Rolling back changes---");
                    var7.printStackTrace();
                    conn.rollback();
                }
            } catch (Throwable var8) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var6) {
                        var8.addSuppressed(var6);
                    }
                }

                throw var8;
            }

            if (stmt != null) {
                stmt.close();
            }
        } catch (Throwable var9) {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Throwable var5) {
                    var9.addSuppressed(var5);
                }
            }

            throw var9;
        }

        if (conn != null) {
            conn.close();
        }

    }
}
