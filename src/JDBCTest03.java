import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class JDBCTest03 {
    public static void main(String[] args) {
        // 首先调用getAccount()获取用户账号密码
        HashMap<String, String> accMap = getAccount();

        // 接下来调用login()方法进行数据库对比操作
        boolean isLogin = login(accMap);

        // 检查是否登录成功
        System.out.println(isLogin ? "登录成功！" : "登录失败，请检查用户名或密码");

    }

    private static boolean login(HashMap<String, String> accMap) {
        boolean isLogin = false;
        // 初始化数据库连接
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String driver = "";
        String url = "";
        String username = "";
        String password = "";
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("Resource\\mysql.properties"));
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
            // 设置不自动提交
            connection.setAutoCommit(false);

            // 提前写好sql语句模板
            String sql = "select * from users where id = ? and password = ?";
            ps = connection.prepareStatement(sql);
            // 查询数据库是否有账户名，且密码是否正确
            ps.setString(1, accMap.get("id"));
            ps.setString(2, accMap.get("password"));

            resultSet = ps.executeQuery(sql);

            // 如果查询到数据则说明数据库中有该账户
            if (resultSet.next()) isLogin = true;

            // 提交
            connection.commit();
        } catch (Exception e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return isLogin;
    }

    private static HashMap<String,String> getAccount() {
        HashMap<String, String> hashMap = new HashMap<>();
        Scanner scanner = new  java.util.Scanner(System.in);
        System.out.print("请输入你的id：");
        hashMap.put("id", scanner.next());
        System.out.print("请输入你的密码：");
        hashMap.put("password",scanner.next());
        return hashMap;
    }
}
