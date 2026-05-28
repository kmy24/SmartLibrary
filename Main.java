import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Welcome to the Smart Library System ===");

        while (running) {
            System.out.println("\n--- SmartLibrary Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Search (BST)");
            System.out.println("3. Borrow (Stack)");
            System.out.println("4. History");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            // Read the entire line to prevent Scanner buffering issues
            String input = sc.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1:
                    handleAddBook(system, sc);
                    break;
                case 2:
                    handleSearchBook(system, sc);
                    break;
                case 3:
                    handleBorrowBook(system, sc);
                    break;
                case 4:
                    System.out.println(system.getFullHistory());
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please select a number from 1 to 5.");
            }
        }
        sc.close();
    }

    private static void handleAddBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter ISBN (Integer only): ");
        int isbn;
        try {
            isbn = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Input Error: ISBN must be a valid integer.");
            return; // Abort this operation and return to main menu
        }

        System.out.print("Enter Title: ");
        String title = sc.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Input Error: Title cannot be empty.");
            return;
        }

        System.out.print("Enter Author: ");
        String author = sc.nextLine().trim();
        if (author.isEmpty()) {
            System.out.println("Input Error: Author cannot be empty.");
            return;
        }

        System.out.println(system.addBook(isbn, title, author));
    }

    private static void handleSearchBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter ISBN to search: ");
        try {
            int isbn = Integer.parseInt(sc.nextLine().trim());
            System.out.println(system.searchBook(isbn));
        } catch (NumberFormatException e) {
            System.out.println("Input Error: ISBN must be a valid integer.");
        }
    }

    private static void handleBorrowBook(LibrarySystem system, Scanner sc) {
        System.out.print("Enter ISBN to borrow: ");
        try {
            int isbn = Integer.parseInt(sc.nextLine().trim());
            System.out.println(system.borrowBook(isbn));
        } catch (NumberFormatException e) {
            System.out.println("Input Error: ISBN must be a valid integer.");
        }
    }
}