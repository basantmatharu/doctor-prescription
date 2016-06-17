package com.scholars.doctor.model;

import java.io.Serializable;

/**
 * Created by I311636 on 6/7/2016.
 */
public class Prescription implements Serializable {
    private static final long serialVersionUID = -3545720613893575546L;

    private String id;
    private String medicines;
    private String totalAmount;
    private String patientId;
    private String doctorId;
    private String pharmacistId;
    private String status;
    private String title;

    public Prescription(){

    }

    public Prescription(String medicines,String totalAmount,String patientId,String doctorId,String pharmacistId,String status,String title){
        this.medicines = medicines;
        this.totalAmount = totalAmount;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.pharmacistId=pharmacistId;
        this.status = status;
        this.title = title;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPharmacistId() {
        return pharmacistId;
    }

    public void setPharmacistId(String pharmacistId) {
        this.pharmacistId = pharmacistId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
