package com.ainfo.mynotes.repository;

import com.ainfo.mynotes.domain.Note;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MyNotesJpaRepositoryTest {
    @Autowired
    private MyNotesJpaRepository repository;

    @Test
    public void testFindByTitleLikeIgnoreCaseFound() {
        List<Note> notes = repository.findByTitleContainingIgnoreCase("%itle%");
        assertEquals(3, notes.size());
    }

    @Test
    public void testFindByTitleLikeIgnoreCaseNotFound() {
        List<Note> notes = repository.findByTitleContainingIgnoreCase("%itle%");
        assertEquals(3, notes.size());
    }

    @Test
    public void testFindByIdFound() {
        Optional<Note> note = repository.findById(1L);
        assertTrue(note.isPresent());
        assertEquals("Title-1", note.get().getTitle());
        assertEquals("Content 1", note.get().getContent());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Note> note = repository.findById(100L);
        assertFalse(note.isPresent());
    }
}
