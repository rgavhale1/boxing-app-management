package com.boxing.app.service;

import com.boxing.app.model.JoinBatchRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class JoinBatchRequestSpec {

    public static Specification<JoinBatchRequest> nameContains(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<JoinBatchRequest> mobileContains(String mobile) {
        return (root, query, cb) ->
                mobile == null || mobile.isEmpty()
                        ? null
                        : cb.like(root.get("mobile"), "%" + mobile + "%");
    }

    public static Specification<JoinBatchRequest> emailContains(String email) {
        return (root, query, cb) ->
                email == null || email.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<JoinBatchRequest> timeContains(String time) {
        return (root, query, cb) ->
                time == null || time.isEmpty()
                        ? null
                        : cb.like(root.get("time"), "%" + time + "%");
    }

    public static Specification<JoinBatchRequest> programTypeContains(String programType) {
        return (root, query, cb) ->
                programType == null || programType.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("programType")), "%" + programType.toLowerCase() + "%");
    }

    public static Specification<JoinBatchRequest> dateEquals(LocalDate date) {
        return (root, query, cb) ->
                date == null
                        ? null
                        : cb.equal(root.get("registeredDate"), date);
    }

    public static Specification<JoinBatchRequest> serviceContains(String service) {
        return (root, query, cb) ->
                service == null || service.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("service")), "%" + service.toLowerCase() + "%");
    }
}