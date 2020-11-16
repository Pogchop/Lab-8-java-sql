package Lab8;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AtomicTransactionTest {
    public AtomicTransactionTest() {
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookshop", "root", "");

            try {
                Statement stmt = conn.createStatement();

                try {
                    conn.setAutoCommit(false);
                    ResultSet rset = stmt.executeQuery("select id, qty from books where id in (1001,1002)");
                    System.out.println("-- Before UPDATE --");

                    PrintStream var10000;
                    int var10001;
                    while(rset.next()) {
                        var10000 = System.out;
                        var10001 = rset.getInt("id");
                        var10000.println(var10001 + ", " + rset.getInt("qty"));
                    }

                    conn.commit();
                    stmt.executeUpdate("update books set qty = qty + 1 where id = 1001");
                    stmt.executeUpdate("update books set qty = qty + 1 where id = 1002");
                    conn.commit();
                    rset = stmt.executeQuery("select id, qty from books where id in (1001, 1002)");
                    System.out.println("-- after UPDATE and Commit --");

                    while(rset.next()) {
                        var10000 = System.out;
                        var10001 = rset.getInt("id");
                        var10000.println(var10001 + ", " + rset.getInt("qty"));
                    }

                    conn.commit();
                    stmt.executeUpdate("update books set qty = qty - 99 where id = 1001");
                    stmt.executeUpdate("update books set qty = qty - 99 where id = 1002");
                    conn.rollback();
                    rset = stmt.executeQuery("select id, qty from books where id in (1001,1002)");
                    System.out.println("-- after UPDATE and Rollback --");

                    while(rset.next()) {
                        var10000 = System.out;
                        var10001 = rset.getInt("id");
                        var10000.println(var10001 + ", " + rset.getInt("qty"));
                    }

                    conn.commit();
                } catch (Throwable var7) {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }

                    throw var7;
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Throwable var8) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var5) {
                        var8.addSuppressed(var5);
                    }
                }

                throw var8;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var9) {
            var9.printStackTrace();
        }

    }
}
