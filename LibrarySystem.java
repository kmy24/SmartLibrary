public class LibrarySystem implements LibraryADT {
    private BookBST catalogue = new BookBST(); 
    private BorrowStack history = new BorrowStack(); 

    @Override
    public String addBook(int isbn, String title, String author) {
        // LOGIC: Check if ISBN already exists
        Book existingBook = catalogue.search(isbn);
        if (existingBook != null) {
            return "ERROR: ISBN " + isbn + " already exists!\nTitle: " + existingBook.title + "\nAuthor: " + existingBook.author;
        }
        
        catalogue.insert(isbn, title, author); 
        return "SUCCESS: Book added to catalogue.";
    }

    @Override
    public String searchBook(int isbn) {
        Book b = catalogue.search(isbn); 
        if (b != null) {
            String status = b.isBorrowed ? " (Currently Borrowed)" : " (Available)";
            return "Found: " + b.title + " by " + b.author + status;
        }
        return "Not Found: No book with ISBN " + isbn + " exists."; 
    }

    @Override
    public String borrowBook(int isbn) {
        // LOGIC: Check if book exists, then check if already borrowed
        Book b = catalogue.search(isbn); 
        if (b == null) {
            return "ERROR: Book not found in catalogue."; 
        }
        if (b.isBorrowed) {
            return "ERROR: This book is already borrowed!\nTitle: " + b.title + "\nAuthor: " + b.author;
        }
        
        b.isBorrowed = true; // Mark as borrowed
        history.push(b); 
        return "SUCCESS: Borrowed \nTitle: " + b.title + "\nAuthor: " + b.author; 
    }

    @Override
    public String getFullHistory() {
        return history.getHistory(); 
    }
}