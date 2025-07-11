package com.example.Task.api.service;

import com.example.Task.api.model.LeaveSection;
import com.example.Task.api.repositoey.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public String createLeave(LeaveSection leaveSection) {
        // Set requestdate when creating a leave
        leaveSection.setRequestdate(new Date());
        leaveRepository.save(leaveSection);
        return "Leave Created";
    }

    public List<LeaveSection> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public Optional<LeaveSection> getLeaveById(Long id) {
        return leaveRepository.findById(id);
    }

    public String deleteLeaveById(Long id) {
        leaveRepository.deleteById(id);
        return "Deleted SuccessFully Leave";
    }

    public String updateLeaveById(Long id, LeaveSection leaveSection) {
        Optional<LeaveSection> optionalLeaveSection = leaveRepository.findById(id);
        if (optionalLeaveSection.isPresent()) {
            LeaveSection existingLeaveSection = optionalLeaveSection.get();

            if (leaveSection.getSubject() != null) {
                existingLeaveSection.setSubject(leaveSection.getSubject());
            }
            if (leaveSection.getDescription() != null) {
                existingLeaveSection.setDescription(leaveSection.getDescription());
            }
            if (leaveSection.getLeavedate() != null) {
                existingLeaveSection.setLeavedate(leaveSection.getLeavedate());
            }
            if (leaveSection.getDaytype() != null) {
                existingLeaveSection.setDaytype(leaveSection.getDaytype());
            }
            if (leaveSection.getStatusofleave() != null) {
                existingLeaveSection.setStatusofleave(leaveSection.getStatusofleave());
            }
            // Do NOT update requestdate on update
            // if (leaveSection.getRequestdate() != null) {
            //     existingLeaveSection.setRequestdate(leaveSection.getRequestdate());
            // }

            // Set updateDate to current date on update
            existingLeaveSection.setUpdateDate(new Date());

            if (leaveSection.getReasonfordeclineleave() != null) {
                existingLeaveSection.setReasonfordeclineleave(leaveSection.getReasonfordeclineleave());
            }

            leaveRepository.save(existingLeaveSection);
            return "Leave Updated";
        } else {
            return "Leave not found";
        }
    }
}
