package structure;

import model.Transaction;
import java.util.List;

public class TransactionSorter {

    public static void sortByNominal(List<Transaction> list, boolean ascending) {
        quickSort(list, 0, list.size() - 1, ascending, "nominal");
    }

    public static void sortByTanggal(List<Transaction> list, boolean ascending) {
        quickSort(list, 0, list.size() - 1, ascending, "tanggal");
    }

    private static void quickSort(List<Transaction> list, int low, int high, boolean ascending, String by) {
        if (low < high) {
            int pi = partition(list, low, high, ascending, by);
            quickSort(list, low, pi - 1, ascending, by);
            quickSort(list, pi + 1, high, ascending, by);
        }
    }

    private static int partition(List<Transaction> list, int low, int high, boolean ascending, String by) {
        Transaction pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            boolean condition;
            if (by.equals("nominal")) {
                condition = ascending
                        ? list.get(j).getNominal() <= pivot.getNominal()
                        : list.get(j).getNominal() >= pivot.getNominal();
            } else {
                condition = ascending
                        ? list.get(j).getTanggal().compareTo(pivot.getTanggal()) <= 0
                        : list.get(j).getTanggal().compareTo(pivot.getTanggal()) >= 0;
            }
            if (condition) {
                i++;
                Transaction temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Transaction temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }
}