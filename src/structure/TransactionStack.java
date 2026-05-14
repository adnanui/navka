package structure;

import model.Transaction;

public class TransactionStack {
    private Transaction[] stack;
    private int top;
    private int capacity;

    public TransactionStack(int capacity) {
        this.capacity = capacity;
        this.stack = new Transaction[capacity];
        this.top = -1;
    }

    public void push(Transaction data) {
        if (top < capacity - 1) {
            stack[++top] = data;
        }
    }

    public Transaction pop() {
        if (isEmpty()) return null;
        return stack[top--];
    }

    public Transaction peek() {
        if (isEmpty()) return null;
        return stack[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int getSize() {
        return top + 1;
    }
}