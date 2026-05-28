import java.util.Stack;

class BookBST {
    private Book root;

    public void insert(int isbn, String t, String a) {
        root = ins(root, isbn, t, a); 
    }

    private Book ins(Book r, int i, String t, String a) {
        if (r == null) return new Book(i, t, a); 
        if (i < r.isbn) r.left = ins(r.left, i, t, a); 
        else if (i > r.isbn) r.right = ins(r.right, i, t, a); 
        return r;
    }

    public Book search(int i) {
        return sea(root, i); 
    }

    private Book sea(Book r, int i) {
        if (r == null || r.isbn == i) return r; 
        return (i < r.isbn) ? sea(r.left, i) : sea(r.right, i); 
    }
}

class BorrowStack {
    private Stack<Book> stack = new Stack<>(); 

    public void push(Book b) {
        stack.push(b);
    }

    public String getHistory() {
        if (stack.isEmpty()) return "History is empty."; 
        StringBuilder sb = new StringBuilder("--- Recent Activity ---\n");
        // LIFO Order: Loop backwards
        for (int i = stack.size() - 1; i >= 0; i--) { 
            sb.append(stack.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}