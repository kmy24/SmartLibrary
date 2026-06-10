public interface LibraryADT {
    String addBook(int isbn, String title, String author);
    String borrowBook(int isbn);
    String returnBook(int isbn); 
    String searchBook(String query); 
    String getFullHistory();
    String deleteBook(int isbn);
    String updateBook(int isbn, String title, String author);
    String displayAllBooks();
}