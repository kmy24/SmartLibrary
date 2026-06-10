import java.util.ArrayList;
import java.util.List;

public class BookBST {
    private Book root;

    public void insert(int isbn, String t, String a, boolean isBorrowed) {
        root = ins(root, isbn, t, a, isBorrowed); 
    }

    private Book ins(Book r, int i, String t, String a, boolean isBorrowed) {
        if (r == null) {
            Book newBook = new Book(i, t, a);
            newBook.isBorrowed = isBorrowed;
            return newBook;
        }
        if (i < r.isbn) r.left = ins(r.left, i, t, a, isBorrowed); 
        else if (i > r.isbn) r.right = ins(r.right, i, t, a, isBorrowed); 
        return r;
    }

    public Book search(int i) {
        return sea(root, i); 
    }

    private Book sea(Book r, int i) {
        if (r == null || r.isbn == i) return r; 
        return (i < r.isbn) ? sea(r.left, i) : sea(r.right, i); 
    }

    public boolean delete(int isbn) {
        if (search(isbn) == null) return false;
        root = del(root, isbn);
        return true;
    }

    private Book del(Book root, int isbn) {
        if (root == null) return null;
        if (isbn < root.isbn) root.left = del(root.left, isbn);
        else if (isbn > root.isbn) root.right = del(root.right, isbn);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;
                
            Book min = findMin(root.right);
            root.isbn = min.isbn;
            root.title = min.title;
            root.author = min.author;
            root.isBorrowed = min.isBorrowed;
            root.right = del(root.right, root.isbn);
        }
        return root;
    }

    private Book findMin(Book root) {
        Book min = root;
        while (min.left != null) min = min.left;
        return min;
    }

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        inOrderTraverse(root, list);
        return list;
    }

    private void inOrderTraverse(Book r, List<Book> list) {
        if (r != null) {
            inOrderTraverse(r.left, list);
            list.add(r);
            inOrderTraverse(r.right, list);
        }
    }
}