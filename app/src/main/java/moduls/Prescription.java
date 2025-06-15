package main.java.moduls;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import java.util.Objects;

public class Prescription {
    private final StringProperty id;
    private final StringProperty patientId;
    private final StringProperty doctorId;
    private final StringProperty medicineName;
    private final StringProperty dosage;
    private final StringProperty quantity;
    private final StringProperty notes;
    private final ObjectProperty<Date> issueDate;
    private final StringProperty status;

    public Prescription(String id, String patientId, String doctorId, String medicineName, String dosage, String quantity, String notes) {
        if (id == null || id.isEmpty() || patientId == null || patientId.isEmpty() || doctorId == null || doctorId.isEmpty() ||
            medicineName == null || medicineName.isEmpty() || dosage == null || dosage.isEmpty() || quantity == null || quantity.isEmpty()) {
            throw new IllegalArgumentException("All core prescription properties must be valid and non-empty.");
        }

        this.id = new SimpleStringProperty(id);
        this.patientId = new SimpleStringProperty(patientId);
        this.doctorId = new SimpleStringProperty(doctorId);
        this.medicineName = new SimpleStringProperty(medicineName);
        this.dosage = new SimpleStringProperty(dosage);
        this.quantity = new SimpleStringProperty(quantity);
        this.notes = (notes == null) ? new SimpleStringProperty("") : new SimpleStringProperty(notes);
        this.issueDate = new SimpleObjectProperty<>(new Date()); 
        this.status = new SimpleStringProperty("Pending"); 
    }

    public StringProperty idProperty() { return id; }
    public StringProperty patientIdProperty() { return patientId; }
    public StringProperty doctorIdProperty() { return doctorId; }
    public StringProperty medicineNameProperty() { return medicineName; }
    public StringProperty dosageProperty() { return dosage; }
    public StringProperty quantityProperty() { return quantity; }
    public StringProperty notesProperty() { return notes; }
    public ObjectProperty<Date> issueDateProperty() { return issueDate; }
    public StringProperty statusProperty() { return status; }

    public String getId() { return id.get(); }
    public String getPatientId() { return patientId.get(); }
    public String getDoctorId() { return doctorId.get(); }
    public String getMedicineName() { return medicineName.get(); }
    public String getDosage() { return dosage.get(); }
    public String getQuantity() { return quantity.get(); }
    public String getNotes() { return notes.get(); }
    public Date getIssueDate() { return issueDate.get(); }
    public String getStatus() { return status.get(); }

    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }
        this.status.set(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return Objects.equals(id.get(), that.id.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.get());
    }
}
