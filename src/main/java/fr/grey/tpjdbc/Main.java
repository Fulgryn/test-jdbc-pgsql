package fr.grey.tpjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        SqlStuff.createDB();
        Book book1 = new Book("Le Mythe de Cthulhu","H.P. LOVECRAFT");
        SqlStuff.createBook(book1);
        book1.toString();
        Book book2 = new Book("Dagon","H.P. LOVECRAFT");
        SqlStuff.createBook(book2);
        Book book3 = new Book("Les Misérables","Victor Hugo");
        SqlStuff.createBook(book3);

        Client client1 =  SqlStuff.createClient("Schmorgl","Michel", Gender.M);
        Client client2 = SqlStuff.createClient("Ricard","Paulette", Gender.F);

        SqlStuff.purchase(client1,book1);
        SqlStuff.purchase(client1,book3);

        ResultSet rs = SqlStuff.getPurchasedBooks(client1);

        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            Book book = new Book(id,title,author);
            book.toString();
        }

    }
}
