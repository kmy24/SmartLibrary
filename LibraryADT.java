public interface LibraryADT {
    String addBook(int isbn, String title, String author);
    String borrowBook(int isbn);
    String searchBook(int isbn);
    String getFullHistory();
}