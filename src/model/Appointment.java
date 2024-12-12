package model;

public class Appointment {
    private int id;
    private int customerId;
    private int mechanicId;
    private String vehicleType;
    private String appointmentDate;
    private String appointmentTime;

    public Appointment(int id, int customerId, int mechanicId, String vehicleType, String appointmentDate, String appointmentTime) {
        this.id = id;
        this.customerId = customerId;
        this.mechanicId = mechanicId;
        this.vehicleType = vehicleType;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getMechanicId() { return mechanicId; }
    public void setMechanicId(int mechanicId) { this.mechanicId = mechanicId; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

}
