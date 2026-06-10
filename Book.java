// Represents a single Book node in the Binary Search Tree
public class Book {
    int isbn;
    String title, author;
    boolean isBorrowed;
    Book left, right; // Pointers to the left and right children in the BST

    // Constructor to initialize a newly created Book object
    public Book(int isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isBorrowed = false; // Books are available by default when added
        this.left = null;
        this.right = null;
    }

    // Returns a formatted string displaying the book's details and current availability
    @Override
    public String toString() {
        String status = isBorrowed ?
            "[BORROWED]" : "[AVAILABLE]";
        return status + " ISBN: " + isbn + " | Title: " + title + " | Author: " + author;
    }
}