package fr.grey.tpjdbc;

import java.sql.*;

public class SqlStuff {
    protected static String url = "jdbc:postgresql://localhost:5432/formation";
    protected static String user = "formation";
    protected static String password = "formation";

    public static void createDB(){
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DROP TABLE IF EXISTS purchase CASCADE; " +
                        "DROP TABLE IF EXISTS client CASCADE; " +
                        "DROP TABLE IF EXISTS book CASCADE; " +
                        "CREATE TABLE book " +
                        "( " +
                        "id serial CONSTRAINT pk_book PRIMARY KEY, " +
                        "title VARCHAR(255), " +
                        "author VARCHAR(255) " +
                        "); " +
                        "CREATE TABLE client " +
                        "( " +
                        "id serial CONSTRAINT pk_client PRIMARY KEY, " +
                        "lastname VARCHAR(255), " +
                        "firstname VARCHAR(255), " +
                        "gender VARCHAR(1), " +
                        "favorite_book INTEGER " +
                        "); " +
                        "ALTER TABLE client ADD CONSTRAINT fk_favbook FOREIGN KEY (favorite_book) REFERENCES book (id); " +
                        "CREATE TABLE purchase " +
                        "( " +
                        "id_client INTEGER NOT NULL, " +
                        "id_book INTEGER NOT NULL, " +
                        "PRIMARY KEY(id_client, id_book) " +
                        "); " +
                        "ALTER TABLE purchase ADD CONSTRAINT fk_clientpurchase FOREIGN KEY (id_client) REFERENCES client (id); " +
                        "ALTER TABLE purchase ADD CONSTRAINT fk_bookpurchase FOREIGN KEY (id_book) REFERENCES book (id);");
                conn.commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createBook(Book book){
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("INSERT INTO book(title,author) VALUES('"+
                        book.getTitle() + "', '"+
                        book.getAuthor() + "')", Statement.RETURN_GENERATED_KEYS);
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                generatedKeys.next();
                book.setId(generatedKeys.getInt("id"));
                conn.commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return book;
    }

    public static Client createClient(String lastName, String firstName, Gender gender){
        int id = 0;
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                id= stmt.executeUpdate("INSERT INTO client(firstname, lastname, gender) VALUES('"+
                        lastName + "', '"+
                        firstName + "', '"+
                        gender + "') RETURNING id");
                conn.commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(id != 0)
            return new Client(id,lastName,firstName,gender);
        else
            return null;
    }

    public static void purchase(Client client, Book book){
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("INSERT INTO purchase VALUES("+
                        client.getId() + ", "+
                        book.getId());
                conn.commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Client createClient(String lastName, String firstName, Gender gender, Book favoriteBook){
        int id = 0;
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                id= stmt.executeUpdate("INSERT INTO client(firstname, lastname, gender, favorite_book) VALUES('"+
                        lastName + "', '"+
                        firstName + "', '"+
                        gender + "', '"+
                        favoriteBook.getId() + "') RETURNING id" );
                conn.commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(id != 0)
            return new Client(id,lastName,firstName,gender,favoriteBook);
        else
            return null;
    }

    public static ResultSet getPurchasedBooks(Client client) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT bo.title, bo.author  FROM book bo JOIN purchase pu ON pu.id_book = bo.id " +
                "JOIN client cl ON pu.id_client = cl.id " +
                "WHERE cl.id = ?");
        stmt.setInt(1, client.getId());
        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                System.out.println(id + ", " + title);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            stmt.close();
            conn.close();
        }
        return resultSet;
    }


}
