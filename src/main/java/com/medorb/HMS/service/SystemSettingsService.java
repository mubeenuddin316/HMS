package com.medorb.HMS.service;

import com.medorb.HMS.model.SystemSettings;
import java.util.Optional;

public interface SystemSettingsService {
    Optional<SystemSettings> getSystemSettingsById(Integer settingsId);
    SystemSettings saveSystemSettings(SystemSettings systemSettings);
}
