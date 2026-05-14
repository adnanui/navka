package model;

public class TransactionNode {
    public Transaction data;
    public TransactionNode next;
    public TransactionNode prev;

    public TransactionNode(Transaction data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}