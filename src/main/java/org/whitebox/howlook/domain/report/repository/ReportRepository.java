package org.whitebox.howlook.domain.report.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {}
