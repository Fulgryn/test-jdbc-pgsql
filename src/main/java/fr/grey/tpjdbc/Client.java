package fr.grey.tpjdbc;

public class Client {
    private int id;
    private String lastname;
    private String firstname;
    private Gender gender;
    private Book favoriteBook;

    public Client(String lastname, String firstname, Gender gender, Book favoriteBook) {
        this(lastname, firstname, gender);
        this.favoriteBook = favoriteBook;
    }

    public Client(String lastname, String firstname, Gender gender ) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.gender = gender;
    }

    public Client(int id, String lastname, String firstname, Gender gender, Book favoriteBook) {
        this(lastname, firstname, gender, favoriteBook);
        this.id = id;
    }

    public Client(int id, String lastname, String firstname, Gender gender ) {
        this(lastname, firstname, gender);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public Gender getGender() {
        return gender;
    }

    public Book getFavoriteBook() {
        return favoriteBook;
    }



}
