import java.util.Stack;

public class BorrowStack {
    private Stack<String> stack = new Stack<>(); 

    public void push(String log) {
        stack.push(log);
    }

    public String getSessionHistory() {
        if (stack.isEmpty()) return "No books borrowed in this session."; 
        StringBuilder sb = new StringBuilder("--- Current Session Activity ---\n");
        for (int i = stack.size() - 1; i >= 0; i--) { 
            sb.append(stack.get(i)).append("\n");
        }
        return sb.toString();
    }
}