package com.gym.app.service;

import com.gym.app.model.JoinBatchRequest;
import com.gym.app.repository.JoinRequestRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class JoinRequestService {

    @Autowired
    private JoinRequestRepository repo;

    @Autowired
    private EmailService emailService;

    public String saveAndNotify(JoinBatchRequest request) {
        repo.save(request);

        CompletableFuture.runAsync(() ->
                {
                    emailService.sendConfirmationEmail(request.getEmail(), request.getName(), request.getProgram());
                }
        );

        CompletableFuture.runAsync(() -> {
            emailService.sendAdminNotification("anuragkopulwar5@gmail.com", request);
        });

        return "Success";
    }

    public Page<JoinBatchRequest> getUsers(
            String name,
            String mobile,
            String email,
            String time,
            String program,
            LocalDate registeredDate,
            Pageable pageable
    ) {
        Specification<JoinBatchRequest> spec =
                Specification.where(JoinBatchRequestSpec.nameContains(name))
                        .and(JoinBatchRequestSpec.mobileContains(mobile))
                        .and(JoinBatchRequestSpec.emailContains(email))
                        .and(JoinBatchRequestSpec.timeContains(time))
                        .and(JoinBatchRequestSpec.programContains(program))
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