package structure;

import model.Transaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionBST {
    private class BSTNode {
        LocalDate date;
        List<Transaction> transactions;
        BSTNode left, right;

        BSTNode(LocalDate date) {
            this.date = date;
            this.transactions = new ArrayList<>();
            this.left = null;
            this.right = null;
        }
    }

    private BSTNode root;

    public TransactionBST() {
        this.root = null;
    }

    public void insert(Transaction t) {
        root = insertRec(root, t);
    }

    private BSTNode insertRec(BSTNode node, Transaction t) {
        if (node == null) {
            BSTNode newNode = new BSTNode(t.getTanggal());
            newNode.transactions.add(t);
            return newNode;
        }
        int cmp = t.getTanggal().compareTo(node.date);
        if (cmp == 0) {
            node.transactions.add(t);
        } else if (cmp < 0) {
            node.left = insertRec(node.left, t);
        } else {
            node.right = insertRec(node.right, t);
        }
        return node;
    }

    public List<Transaction> findByRange(LocalDate from, LocalDate to) {
        List<Transaction> result = new ArrayList<>();
        findByRangeRec(root, from, to, result);
        return result;
    }

    private void findByRangeRec(BSTNode node, LocalDate from, LocalDate to, List<Transaction> result) {
        if (node == null) return;
        if (node.date.compareTo(from) >= 0 && node.date.compareTo(to) <= 0) {
            result.addAll(node.transactions);
        }
        if (node.date.compareTo(from) > 0) {
            findByRangeRec(node.left, from, to, result);
        }
        if (node.date.compareTo(to) < 0) {
            findByRangeRec(node.right, from, to, result);
        }
    }
}