package src;

import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Medicine {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final IntegerProperty stock;
    private final ObjectProperty<Date> expiryDate;
    private final StringProperty supplier;

    public Medicine(String id, String name, String category, int stock, Date expiryDate, String supplier) {
        if (id == null || id.isEmpty() || name == null || name.isEmpty() || category == null || category.isEmpty() || stock < 0 || expiryDate == null || supplier == null || supplier.isEmpty()) {
            throw new IllegalArgumentException("All medicine properties (ID, name, category, stock, expiry date, supplier) must be valid and non-empty.");
        }
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.stock = new SimpleIntegerProperty(stock);
        this.expiryDate = new SimpleObjectProperty<>(expiryDate);
        this.supplier = new SimpleStringProperty(supplier);
    }

    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty categoryProperty() { return category; }
    public IntegerProperty stockProperty() { return stock; }
    public ObjectProperty<Date> expiryDateProperty() { return expiryDate; }
    public StringProperty supplierProperty() { return supplier; }

    public String getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getCategory() { return category.get(); }
    public int getStock() { return stock.get(); }
    public Date getExpiryDate() { return expiryDate.get(); }
    public String getSupplier() { return supplier.get(); }

    public void setId(String id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setCategory(String category) { this.category.set(category); }
    public void setStock(int stock) { this.stock.set(stock); }
    public void setExpiryDate(Date expiryDate) { this.expiryDate.set(expiryDate); }
    public void setSupplier(String supplier) { this.supplier.set(supplier); }

    @Override
    public String toString() {
        return String.format("Medicine{id='%s', name='%s'}",
                id.get(), name.get());
    }
}
