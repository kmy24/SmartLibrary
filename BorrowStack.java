import java.util.Stack;

// A wrapper around Java's built-in Stack to keep track of session borrow/return actions
public class BorrowStack {
    private Stack<String> stack = new Stack<>();

    // Pushes a new transaction log onto the top of the stack
    public void push(String log) {
        stack.push(log);
    }

    // Retrieves the session's transaction history in reverse chronological order
    public String getSessionHistory() {
        if (stack.isEmpty()) return "No books borrowed in this session.";
        
        StringBuilder sb = new StringBuilder("--- Current Session Activity ---\n");
        // Iterate backward to show the most recent actions first
        for (int i = stack.size() - 1; i >= 0; i--) { 
            sb.append(stack.get(i)).append("\n");
        }
        return sb.toString();
    }
}