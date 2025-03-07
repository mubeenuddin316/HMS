package com.medorb.HMS.repository;

import com.medorb.HMS.model.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemSettingsRepository extends JpaRepository<SystemSettings, Integer> {
    // No custom methods needed for this minimal example
}
