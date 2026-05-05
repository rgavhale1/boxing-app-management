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

    public static Specification<JoinBatchRequest> programContains(String program) {
        return (root, query, cb) ->
                program == null || program.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("program")), "%" + program.toLowerCase() + "%");
    }

    public static Specification<JoinBatchRequest> dateEquals(LocalDate date) {
        return (root, query, cb) ->
                date == null
                        ? null
                        : cb.equal(root.get("registeredDate"), date);
    }
}