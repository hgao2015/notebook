package com.ainfo.mynotes.controller;

import com.ainfo.mynotes.domain.Note;
import com.ainfo.mynotes.exception.MyNoteException;
import com.ainfo.mynotes.repository.MyNotesJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping("/notes")
public class MyNotesController {
    @Autowired
    private MyNotesJpaRepository repository;

    @GetMapping(path = {"/id/{id}"}, produces = {APPLICATION_JSON_VALUE})
    public Note getNoteById(@PathVariable("id") long id) {
        return repository.findById(id).orElseThrow(
                () -> new MyNoteException("Note is not found for id:" + id, HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/title/{title}", produces = {APPLICATION_JSON_VALUE})
    public List<Note> getNoteByTitle(@PathVariable("title") String title) {
        List<Note> notes = repository.findByTitleContainingIgnoreCase("%" + title + "%");
        if (CollectionUtils.isEmpty(notes)) {
            throw new MyNoteException("Note is not found for title:" + title, HttpStatus.NOT_FOUND);
        }
        return notes;
    }

    @PostMapping(path = "/create", headers = {"Accept=application/json"})
    public ResponseEntity createNote(@RequestBody Note note) {
        if (repository.findById(note.getId()).isPresent()) {
            throw new MyNoteException("Note already exists for note id: " + note.getId(), HttpStatus.BAD_REQUEST);
        }
        repository.saveAndFlush(note);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
