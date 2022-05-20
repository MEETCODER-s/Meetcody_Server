package com.meetcody.meetcodyserver.domain.phonebook;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhonebookRepository extends JpaRepository<Phonebook, Long> {
}
