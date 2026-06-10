import java.io.*;
import java.util.List;

// Main backend logic for the library, implementing the rules set by LibraryADT
public class LibrarySystem implements LibraryADT {
    private BookBST catalogue = new BookBST(); // Central database of books
    private BorrowStack sessionHistory = new BorrowStack(); // Tracks actions during runtime
    
    // File paths used for persistent data storage
    private final String BOOKS_FILE = "books.txt";
    private final String HISTORY_FILE = "borrow_history.txt";

    // Constructor automatically loads saved data upon system launch
    public LibrarySystem() {
        loadBooksFromFile();
    }

    // Reads the books.txt file and reconstructs the BST catalogue
    private void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the parsed line using the pipe "|" delimiter
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    // Repopulate the BST with the saved records
                    catalogue.insert(
                        Integer.parseInt(parts[0]), 
                        parts[1], 
                        parts[2], 
                        Boolean.parseBoolean(parts[3])
                    );
                }
            }
        } catch (FileNotFoundException e) {
            // Catch triggered during the first run when the file hasn't been created yet
        } catch (Exception e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    // Writes the current state of the entire catalogue back to the books.txt file
    private void saveBooksToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book b : catalogue.getAllBooks()) {
                // Format details back into the pipe-delimited structure
                bw.write(b.isbn + "|" + b.title + "|" + b.author + "|" + b.isBorrowed);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving catalogue: " + e.getMessage());
        }
    }

    // Appends a new borrowing/returning transaction directly to the history file
    private void appendHistoryToFile(String log) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
            bw.write(log);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    @Override
    public String addBook(int isbn, String title, String author) {
        // Prevent accidental duplicate ISBN entries
        if (catalogue.search(isbn) != null) {
            return "ERROR: ISBN " + isbn + " already exists!";
        }
        // Insert into the tree and immediately update the text file
        catalogue.insert(isbn, title, author, false); 
        saveBooksToFile();
        return "SUCCESS: Book added and saved to database.";
    }

    @Override
    public String updateBook(int isbn, String title, String author) {
        Book b = catalogue.search(isbn);
        if (b != null) {
            // Only update fields if the user provided new data (ignores empty inputs)
            if (!title.isEmpty()) b.title = title;
            if (!author.isEmpty()) b.author = author;
            
            saveBooksToFile();
            return "SUCCESS: Book details updated.";
        }
        return "ERROR: Book with ISBN " + isbn + " not found.";
    }

    @Override
    public String deleteBook(int isbn) {
        // Attempt deletion; save the new state if successful
        if (catalogue.delete(isbn)) {
            saveBooksToFile();
            return "SUCCESS: Book deleted permanently.";
        }
        return "ERROR: Book with ISBN " + isbn + " not found.";
    }

    @Override
    public String displayAllBooks() {
        List<Book> books = catalogue.getAllBooks();
        if (books.isEmpty()) return "The catalogue is currently empty.";
        
        StringBuilder sb = new StringBuilder("=== Complete Library Catalogue ===\n");
        for (Book b : books) sb.append(b.toString()).append("\n");
        return sb.toString().trim();
    }

    @Override
    public String searchBook(String query) {
        // Phase 1: Try evaluating the search query as an exact integer (ISBN lookup)
        try {
            int isbn = Integer.parseInt(query);
            Book b = catalogue.search(isbn);
            if (b != null) return "Found: " + b.toString();
        } catch (NumberFormatException e) {
            // Query is not a number; gracefully fall back to a text-based search
        }

        // Phase 2: Perform a loose text search against titles and authors
        List<Book> allBooks = catalogue.getAllBooks();
        StringBuilder results = new StringBuilder();
        String lowerQuery = query.toLowerCase();

        for (Book b : allBooks) {
            if (b.title.toLowerCase().contains(lowerQuery) || b.author.toLowerCase().contains(lowerQuery)) {
                results.append(b.toString()).append("\n");
            }
        }

        if (results.length() > 0) {
            return "Search Results:\n" + results.toString().trim();
        }
        return "Not Found: No books match your search.";
    }

    @Override
    public String borrowBook(int isbn) {
        Book b = catalogue.search(isbn);
        if (b == null) return "ERROR: Book not found in catalogue."; 
        if (b.isBorrowed) return "ERROR: This book is already borrowed!";
        
        // Mark book as checked out and persist the change
        b.isBorrowed = true;
        saveBooksToFile(); 
        
        // Log the action in both the runtime stack and the persistent text file
        String logEntry = "BORROWED: " + b.title + " (ISBN: " + b.isbn + ")";
        sessionHistory.push(logEntry);
        appendHistoryToFile(logEntry);
        
        return "SUCCESS: Borrowed \nTitle: " + b.title + "\nAuthor: " + b.author;
    }

    // NEW: Return book logic
    @Override
    public String returnBook(int isbn) {
        Book b = catalogue.search(isbn);
        if (b == null) return "ERROR: Book not found in catalogue.";
        if (!b.isBorrowed) return "ERROR: This book is not currently borrowed!";
        
        b.isBorrowed = false;
        // Mark as available
        saveBooksToFile();
        // Update text file
        
        // Record the return transaction
        String logEntry = "RETURNED: " + b.title + " (ISBN: " + b.isbn + ")";
        sessionHistory.push(logEntry); 
        appendHistoryToFile(logEntry);
        
        return "SUCCESS: Returned \nTitle: " + b.title + "\nAuthor: " + b.author;
    }

    @Override
    public String getFullHistory() {
        StringBuilder fullHist = new StringBuilder("=== Complete Transaction History ===\n");
        boolean hasData = false;
        
        // Stream the historical logs directly from the saved file
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                fullHist.append(line).append("\n");
                hasData = true;
            }
        } catch (IOException e) {
            // Handles case where no history has been written yet
        }

        if (!hasData) return "No history recorded yet.";
        return fullHist.toString().trim();
    }
}