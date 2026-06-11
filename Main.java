import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem(); // Initiates the library database engine
        Scanner sc = new Scanner(System.in);
        boolean running = true; // Loop flag to keep the console menu active

        System.out.println("=== Welcome to the Smart Library System ===");
        while (running) {
            // Render the main menu view
            System.out.println("\n--- SmartLibrary Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Display All Books");
            System.out.println("3. Search (ISBN, Title, or Author)");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Borrow Book");
            System.out.println("7. Return Book");         
            System.out.println("8. View History logs");            
            System.out.println("9. Exit");
            System.out.print("Choice: ");

            String input = sc.nextLine().trim();
            int choice;

            // Validate that the user actually inputted a number
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Enter a number between 1 and 9.");
                continue; // Restart the loop if input fails parsing
            }

            // Route the validated choice to the corresponding backend handler
            switch (choice) {
                case 1: handleAddBook(system, sc);
                    break;
                case 2: System.out.println(system.displayAllBooks()); break;
                case 3: handleSearchBook(system, sc); break;
                case 4: handleUpdateBook(system, sc); break;
                case 5: handleDeleteBook(system, sc); break;
                case 6: handleBorrowBook(system, sc); break;
                case 7: handleReturnBook(system, sc); break;
                case 8: System.out.println(system.getFullHistory());
                    break;
                case 9: 
                    running = false; // Disables the loop flag to kill the program
                    System.out.println("Exiting system. Data has been saved. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please select a number from 1 to 9.");
            }
        }
        sc.close(); // Clean up system resources
    }

    // Console prompter logic for formatting new additions
    private static void handleAddBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter ISBN (Integer): ");
        try {
            int isbn = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Enter Title: ");
            String title = sc.nextLine().trim();
            System.out.print("Enter Author: ");
            String author = sc.nextLine().trim();
            
            // Rejects submission if string fields are missing text
            if (title.isEmpty() || author.isEmpty()) {
                System.out.println("Input Error: Title and Author cannot be empty.");
                return;
            }
            System.out.println(system.addBook(isbn, title, author));
        } catch (NumberFormatException e) {
            System.out.println("Input Error: ISBN must be a valid integer.");
        }
    }

    // Console prompter logic for modifying an existing record
    private static void handleUpdateBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter ISBN of book to update: ");
        try {
            int isbn = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Enter New Title (leave blank to keep current): ");
            String title = sc.nextLine().trim();
            System.out.print("Enter New Author (leave blank to keep current): ");
            String author = sc.nextLine().trim();
            
            System.out.println(system.updateBook(isbn, title, author));
        } catch (NumberFormatException e) {
            System.out.println("Input Error: ISBN must be a valid integer.");
        }
    }

    // Console prompter logic to process deletion
    private static void handleDeleteBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter ISBN of book to delete: ");
        try {
            int isbn = Integer.parseInt(sc.nextLine().trim());
            System.out.println(system.deleteBook(isbn));
        } catch (NumberFormatException e) {
            System.out.println("Input Error: ISBN must be a valid integer.");
        }
    }

    // Console prompter logic capturing lookup strings
    private static void handleSearchBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter search term (ISBN, Title, or Author): ");
        String query = sc.nextLine().trim();
        System.out.println(system.searchBook(query));
    }

    // Console prompter logic triggering checkouts
    private static void handleBorrowBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter ISBN to borrow: ");
        try {
            int isbn = Integer.parseInt(sc.nextLine().trim());
            System.out.println(system.borrowBook(isbn));
        } catch (NumberFormatException e) {
            System.out.println("Input Error: ISBN must be a valid integer.");
        }
    }

    // NEW: Helper method to handle returning a book and reversing checkout
    private static void handleReturnBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter ISBN to return: ");
        try {
            int isbn = Integer.parseInt(sc.nextLine().trim());
            System.out.println(system.returnBook(isbn));
        } catch (NumberFormatException e) {
            System.out.println("Input Error: ISBN must be a valid integer.");
        }
    }
}