import java.sql.*;
import java.util.Scanner;

public class Lib_user2 {
    public static void main(String args[]) {
        try {
            // connection to the database //
            String db_url = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db5";
            String db_username = "Group5";
            String db_password = "password";

            Connection connection = DriverManager.getConnection(db_url, db_username, db_password);

            // 3 operations listed for the library user //
            System.out.println("Choose the Search Criterion: ");
            System.out.println("1. call number ");
            System.out.println("2. title ");
            System.out.println("3. author ");

            // store the input entered by the library user //
            System.out.print("Enter Your Choice: ");
            Scanner id_in = new Scanner(System.in);
            int input_in = id_in.nextInt();

            // the query without the input of the Search Keyword //
            String query_operation = "";

            if (input_in == 1) {
                query_operation = "select DISTINCT E.callnumber, C.noofcopy - COUNT(B.copynumber), C.title, D.bcname, " +
                        "C.author, C.rating " +
                        "from Book C, Book_category D, Copy E, Borrow B " +
                        "where C.callnumber = '%s' AND C.callnumber = E.callnumber AND C.bcid = D.bcid AND E.copynumber = B.copynumber " +
                        "AND B.returndate is NULL Order by E.callnumber";
            }
            else if (input_in == 2) {
                query_operation = "select E.callnumber, C.noofcopy - COUNT(DISTINCT(B.copynumber)), C.title, D.bcname, " +
                        "C.author, C.rating " +
                        "from Book C, Book_category D, Copy E, Borrow B " +
                        "where C.title like '%%%s%%' AND C.callnumber = E.callnumber AND C.bcid = D.bcid AND E.copynumber = B.copynumber " +
                        "AND C.callnumber = B.callnumber AND B.returndate is NULL " +
                        "Group By E.callnumber";
            }
            else if (input_in == 3) {
                query_operation = "select E.callnumber, C.noofcopy - COUNT(DISTINCT(B.copynumber)), C.title, D.bcname, " +
                        "C.author, C.rating " +
                        "from Book C, Book_category D, Copy E, Borrow B " +
                        "where C.author like '%%%s%%' AND C.callnumber = E.callnumber AND C.bcid = D.bcid AND E.copynumber = B.copynumber " +
                        "AND C.callnumber = B.callnumber AND B.returndate is NULL " +
                        "Group By E.callnumber Order by E.callnumber";
            }

            // time for the input of the Search Keyword //
            System.out.print("Type in the Search Keyword: ");
            Scanner id_in_2 = new Scanner(System.in);
            String input_in_2 = id_in_2.nextLine();

            // add back the Search Keyword to the query //
            query_operation = String.format(query_operation, input_in_2);

            // used to query //
            Statement stmt = connection.createStatement();
            // the result of the query is stored at the resultSet //
            ResultSet resultSet = stmt.executeQuery(query_operation);

            //title list of the query result //
            System.out.println("|CallNum|Title|Book Category|Author|Rating|Available No. of Copy|");
            // print out the result through cursor //
            while (resultSet.next()) {
                System.out.print("|");
                System.out.print(resultSet.getString("callnumber"));
                System.out.print("|");

                System.out.print(resultSet.getString("title"));
                System.out.print("|");

                System.out.print(resultSet.getString("bcname"));
                System.out.print("|");

                System.out.print(resultSet.getString("author"));
                System.out.print("|");

                System.out.print(resultSet.getFloat("rating"));
                System.out.print("|");

                if (input_in == 1) {
                    System.out.print(resultSet.getInt("C.noofcopy - COUNT(B.copynumber)"));
                    System.out.println("|");
                }

                if (input_in == 2) {
                    System.out.print(resultSet.getInt("C.noofcopy - COUNT(DISTINCT(B.copynumber))"));
                    System.out.println("|");
                }

                if (input_in == 3) {
                    System.out.print(resultSet.getInt("C.noofcopy - COUNT(DISTINCT(B.copynumber))"));
                    System.out.println("|");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // END //
        System.out.println("End Of Query");
    }
}