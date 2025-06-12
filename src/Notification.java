package src;

import java.util.Date;
import java.util.Objects; // Import Objects for utility methods
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Notification {
    private final StringProperty id;
    private final StringProperty message;
    private final ObjectProperty<Date> date;
    private final StringProperty type; 

    public Notification(String id, String message, Date date, String type) {
        if (id == null || id.isEmpty() || message == null || message.isEmpty() || date == null || type == null || type.isEmpty()) {
            throw new IllegalArgumentException("All notification properties (ID, message, date, type) must be valid and non-empty.");
        }
        this.id = new SimpleStringProperty(id);
        this.message = new SimpleStringProperty(message);
        this.date = new SimpleObjectProperty<>(date);
        this.type = new SimpleStringProperty(type);
    }

    public String getId() { return id.get(); }
    public String getMessage() { return message.get(); }
    public Date getDate() { return date.get(); }
    public String getType() { return type.get(); } 

    public StringProperty idProperty() { return id; }
    public StringProperty messageProperty() { return message; }
    public ObjectProperty<Date> dateProperty() { return date; }
    public StringProperty typeProperty() { return type; } 

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id.get() + '\'' +
                ", message='" + message.get() + '\'' +
                ", date=" + date.get() +
                ", type='" + type.get() + '\'' +
                '}';
    }

    // Implement equals and hashCode based on the unique ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id.get(), that.id.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.get());
    }
}
