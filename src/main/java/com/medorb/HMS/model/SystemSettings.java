package com.medorb.HMS.model;

import jakarta.persistence.*;

@Entity
@Table(name = "system_settings")
public class SystemSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settings_id")
    private Integer settingsId;

    @Column(name = "system_name")
    private String systemName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "maintenance_mode")
    private boolean maintenanceMode;

    // Constructors
    public SystemSettings() {}

    // Getters & Setters
    public Integer getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(Integer settingsId) {
        this.settingsId = settingsId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }
}
