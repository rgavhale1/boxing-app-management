package com.boxing.app.controller;

import com.boxing.app.repository.JoinRequestRepository;
import com.boxing.app.service.JoinRequestService;
import com.boxing.app.model.JoinBatchRequest;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class JoinRequestController {
    @Autowired
    private JoinRequestService joinRequestService;

    @Autowired
    private JoinRequestRepository repo;
    @PostMapping("/join")
    public String saveJoinRequest(@RequestBody JoinBatchRequest request) throws MessagingException {

        return joinRequestService.saveAndNotify(request);

    }


    // 🔥 MAIN API YOU ASKED
    @GetMapping("/joinedusers")
    public List<JoinBatchRequest> getAllJoinedUsers() {
        return repo.findAll();
    }
    @GetMapping("/find/joinedusers")
    public Page<JoinBatchRequest> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String programType,
            @RequestParam(required = false) String service,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate registeredDate,
            Pageable pageable
    ) {
        return joinRequestService.getUsers(name, mobile, email, time, programType,service, registeredDate, pageable);
    }
}