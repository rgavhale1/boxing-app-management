package com.boxing.app.service;

import com.boxing.app.model.JoinBatchRequest;
import com.boxing.app.repository.JoinRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class JoinRequestService {

    @Autowired
    private JoinRequestRepository repo;

    @Autowired
    private EmailService emailService;


    @Value("${app.admin.email}")
    private String adminEmail;

    public String saveAndNotify(JoinBatchRequest request) {
        repo.save(request);

        CompletableFuture.runAsync(() ->
                {
                    emailService.sendConfirmationEmail(request.getEmail(), request.getName(), request.getProgramType(),request.getService());
                }
        );

        CompletableFuture.runAsync(() -> {
            emailService.sendAdminNotification(adminEmail, request);
        });

        return "Success";
    }

    public Page<JoinBatchRequest> getUsers(
            String name,
            String mobile,
            String email,
            String time,
            String programType,
            String service,
            LocalDate registeredDate,
            Pageable pageable
    ) {
        Specification<JoinBatchRequest> spec =
                Specification.where(JoinBatchRequestSpec.nameContains(name))
                        .and(JoinBatchRequestSpec.mobileContains(mobile))
                        .and(JoinBatchRequestSpec.emailContains(email))
                        .and(JoinBatchRequestSpec.timeContains(time))
                        .and(JoinBatchRequestSpec.programTypeContains(programType))
                        .and(JoinBatchRequestSpec.serviceContains(service))
                        .and(JoinBatchRequestSpec.dateEquals(registeredDate));

        // Force sorting by registeredDate DESC
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "registeredDate")
        );

        return repo.findAll(spec, sortedPageable);
    }



}