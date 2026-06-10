import java.io.*;
import java.util.List;

public class LibrarySystem implements LibraryADT {
    private BookBST catalogue = new BookBST(); 
    private BorrowStack sessionHistory = new BorrowStack(); 
    
    private final String BOOKS_FILE = "books.txt";
    private final String HISTORY_FILE = "borrow_history.txt";

    public LibrarySystem() {
        loadBooksFromFile();
    }

    private void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    catalogue.insert(
                        Integer.parseInt(parts[0]), 
                        parts[1], 
                        parts[2], 
                        Boolean.parseBoolean(parts[3])
                    );
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet
        } catch (Exception e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    private void saveBooksToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book b : catalogue.getAllBooks()) {
                bw.write(b.isbn + "|" + b.title + "|" + b.author + "|" + b.isBorrowed);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving catalogue: " + e.getMessage());
        }
    }

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
        if (catalogue.search(isbn) != null) {
            return "ERROR: ISBN " + isbn + " already exists!";
        }
        catalogue.insert(isbn, title, author, false); 
        saveBooksToFile();
        return "SUCCESS: Book added and saved to database.";
    }

    @Override
    public String updateBook(int isbn, String title, String author) {
        Book b = catalogue.search(isbn);
        if (b != null) {
            if (!title.isEmpty()) b.title = title;
            if (!author.isEmpty()) b.author = author;
            saveBooksToFile();
            return "SUCCESS: Book details updated.";
        }
        return "ERROR: Book with ISBN " + isbn + " not found.";
    }

    @Override
    public String deleteBook(int isbn) {
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
        try {
            int isbn = Integer.parseInt(query);
            Book b = catalogue.search(isbn);
            if (b != null) return "Found: " + b.toString();
        } catch (NumberFormatException e) {
            // Not a number, search text
        }

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
        
        b.isBorrowed = true;
        saveBooksToFile(); 
        
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
        
        b.isBorrowed = false; // Mark as available
        saveBooksToFile();    // Update text file
        
        String logEntry = "RETURNED: " + b.title + " (ISBN: " + b.isbn + ")";
        sessionHistory.push(logEntry); 
        appendHistoryToFile(logEntry);
        
        return "SUCCESS: Returned \nTitle: " + b.title + "\nAuthor: " + b.author; 
    }

    @Override
    public String getFullHistory() {
        StringBuilder fullHist = new StringBuilder("=== Complete Transaction History ===\n");
        boolean hasData = false;
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                fullHist.append(line).append("\n");
                hasData = true;
            }
        } catch (IOException e) {
            // File might not exist yet
        }

        if (!hasData) return "No history recorded yet.";
        return fullHist.toString().trim();
    }
}