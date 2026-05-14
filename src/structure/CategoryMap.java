package structure;

import java.util.HashMap;
import java.util.Map;

public class CategoryMap {
    private HashMap<String, Double> categoryTotals;
    private HashMap<String, Double> categoryLimits;

    public CategoryMap() {
        this.categoryTotals = new HashMap<>();
        this.categoryLimits = new HashMap<>();
    }

    public void update(String kategori, double nominal) {
        categoryTotals.put(kategori, categoryTotals.getOrDefault(kategori, 0.0) + nominal);
    }

    public void reduce(String kategori, double nominal) {
        categoryTotals.put(kategori, categoryTotals.getOrDefault(kategori, 0.0) - nominal);
    }

    public double getTotal(String kategori) {
        return categoryTotals.getOrDefault(kategori, 0.0);
    }

    public void setLimit(String kategori, double limit) {
        categoryLimits.put(kategori, limit);
    }

    public double getLimit(String kategori) {
        return categoryLimits.getOrDefault(kategori, 0.0);
    }

    public boolean isNearLimit(String kategori) {
        double total = getTotal(kategori);
        double limit = getLimit(kategori);
        return limit > 0 && total >= limit * 0.8;
    }

    public Map<String, Double> getAll() {
        return categoryTotals;
    }

    public Map<String, Double> getAllLimits() {
        return categoryLimits;
    }
}