package com.medorb.HMS.service;

import com.medorb.HMS.model.SystemSettings;
import com.medorb.HMS.repository.SystemSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    private final SystemSettingsRepository systemSettingsRepository;

    @Autowired
    public SystemSettingsServiceImpl(SystemSettingsRepository systemSettingsRepository) {
        this.systemSettingsRepository = systemSettingsRepository;
    }

    @Override
    public Optional<SystemSettings> getSystemSettingsById(Integer settingsId) {
        return systemSettingsRepository.findById(settingsId);
    }

    @Override
    public SystemSettings saveSystemSettings(SystemSettings systemSettings) {
        return systemSettingsRepository.save(systemSettings);
    }
}
