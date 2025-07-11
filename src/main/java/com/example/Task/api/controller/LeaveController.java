package com.example.Task.api.controller;

import com.example.Task.api.model.LeaveSection;
import com.example.Task.api.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "http://localhost:4200")  // allow Angular dev server origin
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/createleave")
    public String createLeave(@RequestBody LeaveSection leaveSection){
        return leaveService.createLeave(leaveSection);
    }

    @GetMapping("/get-all-leaves")
    public List<LeaveSection> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    @GetMapping("/get-leave-ById/{id}")
    public Optional<LeaveSection> getLeaveById(@PathVariable Long id){
        return leaveService.getLeaveById(id);
    }

    @DeleteMapping("/leaveDeleteById/{id}")
    public String leaveDeleteById(@PathVariable Long id){
        return leaveService.deleteLeaveById(id);
    }

    @PutMapping("/updateById/{id}")
    public String updateLeave(@PathVariable Long id, @RequestBody LeaveSection leaveSection){
        return leaveService.updateLeaveById(id, leaveSection);
    }
}
