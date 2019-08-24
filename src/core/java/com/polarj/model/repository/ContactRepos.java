package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polarj.model.Contact;

public interface ContactRepos extends JpaRepository<Contact, Integer>
{
}
