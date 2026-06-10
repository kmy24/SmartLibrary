import java.util.ArrayList;
import java.util.List;

// Binary Search Tree implementation to store and query Book objects
public class BookBST {
    private Book root; // The topmost node of the BST

    // Public wrapper method to insert a new book into the tree
    public void insert(int isbn, String t, String a, boolean isBorrowed) {
        root = ins(root, isbn, t, a, isBorrowed);
    }

    // Recursive helper method to place the book in the correct position based on ISBN
    private Book ins(Book r, int i, String t, String a, boolean isBorrowed) {
        if (r == null) {
            Book newBook = new Book(i, t, a);
            newBook.isBorrowed = isBorrowed;
            return newBook;
        }
        // Navigate left if the new ISBN is smaller, right if larger
        if (i < r.isbn) r.left = ins(r.left, i, t, a, isBorrowed);
        else if (i > r.isbn) r.right = ins(r.right, i, t, a, isBorrowed); 
        return r;
    }

    // Public wrapper method to search for a book by its ISBN
    public Book search(int i) {
        return sea(root, i);
    }

    // Recursive helper method to traverse the tree and locate the specific book
    private Book sea(Book r, int i) {
        if (r == null || r.isbn == i) return r;
        return (i < r.isbn) ? sea(r.left, i) : sea(r.right, i);
    }

    // Public wrapper method to delete a book from the BST
    public boolean delete(int isbn) {
        // Verify the book actually exists before attempting deletion
        if (search(isbn) == null) return false;
        root = del(root, isbn);
        return true;
    }

    // Recursive helper method to remove a node and appropriately restructure the tree
    private Book del(Book root, int isbn) {
        if (root == null) return null;

        // Traverse the tree to find the target node
        if (isbn < root.isbn) root.left = del(root.left, isbn);
        else if (isbn > root.isbn) root.right = del(root.right, isbn);
        else {
            // Target node found. Handle Case 1 & Case 2: Node with zero or one child
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;
                
            // Case 3: Node has two children. Find the inorder successor (smallest in right subtree)
            Book min = findMin(root.right);
            root.isbn = min.isbn;
            root.title = min.title;
            root.author = min.author;
            root.isBorrowed = min.isBorrowed;
            
            // Delete the inorder successor node
            root.right = del(root.right, root.isbn);
        }
        return root;
    }

    // Helper method to find the node with the minimum ISBN (leftmost node)
    private Book findMin(Book root) {
        Book min = root;
        while (min.left != null) min = min.left;
        return min;
    }

    // Retrieves a list of all books in the catalog, sorted sequentially by ISBN
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        inOrderTraverse(root, list);
        return list;
    }

    // Recursive helper method to perform an in-order traversal (Left, Root, Right)
    private void inOrderTraverse(Book r, List<Book> list) {
        if (r != null) {
            inOrderTraverse(r.left, list);
            list.add(r); // Add the current node
            inOrderTraverse(r.right, list);
        }
    }
}