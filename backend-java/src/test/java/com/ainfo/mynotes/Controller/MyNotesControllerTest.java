package com.ainfo.mynotes.Controller;

import com.ainfo.mynotes.MynotesApplication;
import com.ainfo.mynotes.controller.MyNotesController;
import com.ainfo.mynotes.controller.UserController;
import com.ainfo.mynotes.domain.Note;
import com.ainfo.mynotes.repository.MyNotesJpaRepository;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MyNotesControllerTest.TestConfig.class)
@WebMvcTest(value = MyNotesController.class, secure = false)
public class MyNotesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyNotesJpaRepository myNotesJpaRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindByIdSuccess() throws Exception {
        Note note1 = new Note(1L, "title-1", "content-1");
        when(myNotesJpaRepository.findById(1L)).thenReturn(Optional.of(note1));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notes/id/1").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        Note returnedObject = mapper.readValue(result.getResponse().getContentAsString(), Note.class);
        assertTrue(note1.equals(returnedObject));
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        when(myNotesJpaRepository.findById(2L)).thenReturn(Optional.empty());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notes/id/2").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("Note is not found for id:2", result.getResponse().getErrorMessage());
    }

    @Test
    public void testFindByTitleSuccess() throws Exception {
        Note note1 = new Note(1L, "title-1", "content-1");
        Note note2 = new Note(2L, "title-1", "content-2");
        List<Note> notes = Arrays.asList(note1, note2);
        when(myNotesJpaRepository.findByTitleContainingIgnoreCase("%title-1%")).thenReturn(notes);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notes/title/title-1").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        JavaType noteListlType = mapper.getTypeFactory()
                .constructParametrizedType(List.class, List.class,
                        Note.class);

        List<Map> returnedObjects = mapper.readValue(result.getResponse().getContentAsString(), noteListlType);

        assertEquals(2, returnedObjects.size());
        assertTrue(returnedObjects.contains(note1));
        assertTrue(returnedObjects.contains(note2));
    }

    @Test
    public void testFindByTitleNotFound() throws Exception {
        when(myNotesJpaRepository.findByTitleContainingIgnoreCase("title-1")).thenReturn(Collections.EMPTY_LIST);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notes/title/title-2").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("Note is not found for title:title-2", result.getResponse().getErrorMessage());
    }

    @Test
    public void testCreateNoteSuccess() throws Exception {
        when(myNotesJpaRepository.findById(3L)).thenReturn(Optional.empty());
        Note note2 = new Note(3L, "title-3", "content-3");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders.post("/notes/create")
                .content(mapper.writeValueAsString(note2))
                .headers(httpHeaders);
        MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
        assertEquals(HttpStatus.CREATED.value(), result1.getResponse().getStatus());
    }

    @Profile("test")
    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages = {"com.ainfo.mynotes.domain", "com.ainfo.mynotes.controller"},
            excludeFilters = {
                    @ComponentScan.Filter(
                            type = ASSIGNABLE_TYPE,
                            value = {
                                    MynotesApplication.class,
                                    UserController.class
                            })
            })
    public static class TestConfig {
    }
}
