import java.sql.*;

public class JDBCTest01 {
    public static void main(String[] args) {
        // ���������������
        Statement statement = null;
        Connection connection = null;

        try {
            /*
                1��ע������
            */
            Driver driver = new com.mysql.jdbc.Driver(); // ʹ�ö�̬
            DriverManager.registerDriver(driver);

            /*
               2����ȡ����
               URL:ͳһ��Դ��λ����������ĳ����Դ�ľ���·��)https://www.baidu.com/�����URL��
               URL����������
                 1.Э��
                 2.IP
                 3.PORT
                 4.��Դ��
               - ����: http://182.61.200.7:80/index.html
               http://          ͨ��Э��
               182.61.200.7     ������IP��ַ
               80               ������������Ķ˿�
               index.html       ��������ĳ����Դ��

               - �����磺jdbc:mysql://127.0.0.1:3306/mydatabase
               jdbc:mysql://        Э��
               127.0.0.1            ����IP
               3306                 ���ݿ�˿ں�
               mydatabase           MySQl���ݿ�ʾ����
            */
            String url = "jdbc:mysql://127.0.0.1:3306/mydatabase"; // ��Ҫ���ʵ����ݿ�Ϊmydatabase
            String user = "root";   // MySQL�˻�
            String password = "123456";  // MySQL����
            // ���������������MySQL������
            connection = DriverManager.getConnection(url, user, password);

            // ��������
            System.out.println("���ݿ����Ӷ���=" + connection);
            /*
                3. ��ȡ���ݿ��������  Statement SQL
            */
            statement = connection.createStatement();

            /*
                4. ִ��SQL���
                 ִ������SQL�������
                 insert into students values("2022010105", "����", " ��" , 18, 220101)
            */
            String sql = "insert into students values(\"2022010105\", \"����\", \" ��\" , 18, 220101)";
            int count = statement.executeUpdate(sql);

            // ��鷵��ֵ ����ֵΪ1��˵��ִ�гɹ�, ��֮�쳣
            System.out.println("ִ�����: "+ (count==1?"����ɹ�":"����ʧ��"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
            5. �ͷ���Դ
            Ϊ�˱�֤��Դһ���ͷţ���finally�����н��йرղ���
            ����Ҫ��ѭ��С�����Դ˹ر�
            �ֱ����try catch ���� ȷ��һ���ر�������Դ
        */ finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
