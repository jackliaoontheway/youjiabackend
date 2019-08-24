package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.EmailDraft;

public interface EmailDraftRepos extends JpaRepository<EmailDraft, Integer>
{
    List<EmailDraft> findFirst10ByStatus(String status);
}
