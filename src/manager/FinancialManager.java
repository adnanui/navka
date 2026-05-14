package manager;

import model.Transaction;
import structure.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FinancialManager {
    private DoublyLinkedList transactionList;
    private TransactionStack undoStack;
    private CategoryMap categoryMap;
    private TransactionBST bst;

    public FinancialManager() {
        this.transactionList = new DoublyLinkedList();
        this.undoStack = new TransactionStack(100);
        this.categoryMap = new CategoryMap();
        this.bst = new TransactionBST();
    }

    public void addTransaction(Transaction t) {
        transactionList.addFirst(t);
        undoStack.push(t);
        if (t.getTipe().equals("PENGELUARAN")) {
            categoryMap.update(t.getKategori(), t.getNominal());
        }
        bst.insert(t);
    }

    public Transaction undoLast() {
        Transaction t = undoStack.pop();
        if (t != null) {
            transactionList.removeById(t.getId());
            if (t.getTipe().equals("PENGELUARAN")) {
                categoryMap.reduce(t.getKategori(), t.getNominal());
            }
        }
        return t;
    }

    public List<Transaction> getAllTransactions() {
        return transactionList.getAll();
    }

    public List<Transaction> getSortedByNominal(boolean ascending) {
        List<Transaction> list = transactionList.getAll();
        TransactionSorter.sortByNominal(list, ascending);
        return list;
    }

    public List<Transaction> getSortedByTanggal(boolean ascending) {
        List<Transaction> list = transactionList.getAll();
        TransactionSorter.sortByTanggal(list, ascending);
        return list;
    }

    public List<Transaction> getByDateRange(LocalDate from, LocalDate to) {
        return bst.findByRange(from, to);
    }

    public Map<String, Double> getCategoryTotals() {
        return categoryMap.getAll();
    }

    public void setBudgetLimit(String kategori, double limit) {
        categoryMap.setLimit(kategori, limit);
    }

    public boolean isNearLimit(String kategori) {
        return categoryMap.isNearLimit(kategori);
    }

    public Map<String, Double> getAllLimits() {
        return categoryMap.getAllLimits();
    }

    public double getTotalPemasukan() {
        double total = 0;
        for (Transaction t : transactionList.getAll()) {
            if (t.getTipe().equals("PEMASUKAN")) total += t.getNominal();
        }
        return total;
    }

    public double getTotalPengeluaran() {
        double total = 0;
        for (Transaction t : transactionList.getAll()) {
            if (t.getTipe().equals("PENGELUARAN")) total += t.getNominal();
        }
        return total;
    }

    public double getSaldo() {
        return getTotalPemasukan() - getTotalPengeluaran();
    }
    public void updateTransaction(String id, String judul, double nominal, String kategori, String tipe, LocalDate tanggal, String keterangan) {
        List<Transaction> list = getAllTransactions();
        for (Transaction t : list) {
            if (t.getId().equals(id)) {
                // Update CategoryMap dulu
                if (t.getTipe().equals("PENGELUARAN")) {
                    categoryMap.reduce(t.getKategori(), t.getNominal());
                }
                // Update atribut
                t.setJudul(judul);
                t.setNominal(nominal);
                t.setKategori(kategori);
                t.setTipe(tipe);
                t.setTanggal(tanggal);
                t.setKeterangan(keterangan);
                // Update CategoryMap dengan data baru
                if (tipe.equals("PENGELUARAN")) {
                    categoryMap.update(kategori, nominal);
                }
                break;
            }
        }
    }
}