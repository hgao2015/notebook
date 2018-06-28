package com.ainfo.mynotes.repository;

import com.ainfo.mynotes.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyNotesJpaRepository extends JpaRepository<Note, Long> {
    Optional<Note> findById(Long id);

    List<Note> findByTitleContainingIgnoreCase(String title);
}
