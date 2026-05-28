public class Book {
    int isbn;
    String title, author;
    boolean isBorrowed; // Flag to track borrow status
    Book left, right;

    public Book(int isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isBorrowed = false; // Default is available
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        String status = isBorrowed ? "[BORROWED]" : "[AVAILABLE]";
        return status + " ISBN: " + isbn + " | Title: " + title + " | Author: " + author;
    }
}