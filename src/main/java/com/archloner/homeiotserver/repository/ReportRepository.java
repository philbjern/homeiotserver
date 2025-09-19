package com.archloner.homeiotserver.repository;

import com.archloner.homeiotserver.entity.ReportAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportAlert, Long> {
}
