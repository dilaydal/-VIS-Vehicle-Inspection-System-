package model;

public class Inspection {
    private int id;
    private int appointmentId;
    private boolean status;

    public Inspection(int id, int appointmentId, boolean status) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.status = status;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
    }
    public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

}
