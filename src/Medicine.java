package src;

import java.util.Date;

/**
 * Kelas entity untuk merepresentasikan data obat (Medicine).
 * Sesuai dengan perancangan pada dokumen DPPLOO-02.
 */
public class Medicine {
    private String id;
    private String name;
    private int stock;
    private Date expiryDate;

    public Medicine(String id, String name, int stock, Date expiryDate) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.expiryDate = expiryDate;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public Date getExpiryDate() { return expiryDate; }
}