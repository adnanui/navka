package structure;

import model.Transaction;
import model.TransactionNode;
import java.util.ArrayList;
import java.util.List;

public class DoublyLinkedList {
    private TransactionNode head;
    private TransactionNode tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void addFirst(Transaction data) {
        TransactionNode newNode = new TransactionNode(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(Transaction data) {
        TransactionNode newNode = new TransactionNode(data);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public boolean removeById(String id) {
        if (head == null) return false;
        TransactionNode current = head;
        while (current != null) {
            if (current.data.getId().equals(id)) {
                if (current == head) {
                    head = head.next;
                    if (head != null) {
                        head.prev = null;
                    } else {
                        tail = null;
                    }
                } else if (current == tail) {
                    tail = tail.prev;
                    if (tail != null) {
                        tail.next = null;
                    } else {
                        head = null;
                    }
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public List<Transaction> getAll() {
        List<Transaction> result = new ArrayList<>();
        TransactionNode current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    public Transaction getLast() {
        if (tail == null) return null;
        return tail.data;
    }

    public int getSize() {
        return size;
    }
}