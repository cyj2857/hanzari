package com.hancom.hanzari.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancom.hanzari.model.EmployeeAdditionalInfo;

@Repository
public interface EmployeeAdditionalInfoRepository extends JpaRepository<EmployeeAdditionalInfo, String> {
}
