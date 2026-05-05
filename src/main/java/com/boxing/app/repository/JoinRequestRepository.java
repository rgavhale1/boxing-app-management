package com.boxing.app.repository;

import com.boxing.app.model.JoinBatchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JoinRequestRepository
        extends JpaRepository<JoinBatchRequest, Long>,
        JpaSpecificationExecutor<JoinBatchRequest> {
}