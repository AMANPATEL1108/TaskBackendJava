package com.example.Task.api.repositoey;

import com.example.Task.api.model.LeaveSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<LeaveSection,Long> {
}
