package com.example.demo.controller;

import com.example.demo.dto.ReportRequestDto;
import com.example.demo.service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //유저 차단
    @PostMapping("/report-users")
    public void reportUsers(@RequestBody ReportRequestDto reportRequestDto) {
        adminService.reportUsers(reportRequestDto.getUserIds());
    }
}
