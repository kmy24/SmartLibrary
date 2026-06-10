public class Book {
    int isbn;
    String title, author;
    boolean isBorrowed;
    Book left, right;

    public Book(int isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        String status = isBorrowed ? "[BORROWED]" : "[AVAILABLE]";
        return status + " ISBN: " + isbn + " | Title: " + title + " | Author: " + author;
    }
}