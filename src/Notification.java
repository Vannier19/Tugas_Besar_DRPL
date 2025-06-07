package src;

import java.util.Date;

/**
 * Kelas entity untuk merepresentasikan data Notifikasi.
 * Sesuai dengan perancangan pada dokumen DPPLOO-02.
 */
public class Notification {
    private String id;
    private String message;
    private Date date;

    public Notification(String id, String message, Date date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    // Getters
    public String getId() { return id; }
    public String getMessage() { return message; }
    public Date getDate() { return date; }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}