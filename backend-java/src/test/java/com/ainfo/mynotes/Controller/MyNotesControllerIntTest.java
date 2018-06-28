package com.ainfo.mynotes.Controller;

import com.ainfo.mynotes.domain.Note;
import com.ainfo.mynotes.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MyNotesControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindByIdAccessDenied() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notes/id/1").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
        assertEquals("Access Denied", result.getResponse().getErrorMessage());
    }

    @Test
    public void testCreateNoteSuccess() throws Exception {
        when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(true);
        when(jwtTokenProvider.resolveToken(any())).thenReturn("xxx.yyy.zzz2");
        when(jwtTokenProvider.getAuthentication("xxx.yyy.zzz2")).thenReturn(new UsernamePasswordAuthenticationToken("admin", "admin"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "xxx.yyy.zzz2");
        httpHeaders.add("Content-Type", "application/json");

        Note note2 = new Note(4L, "title-4", "content-4");
        RequestBuilder requestBuilder1 = MockMvcRequestBuilders.post("/notes/create")
                .content(mapper.writeValueAsString(note2))
                .headers(httpHeaders);
        MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
        assertEquals(HttpStatus.CREATED.value(), result1.getResponse().getStatus());
    }

    @Test
    public void testCreateNoteValidateError() throws Exception {
        when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(true);
        when(jwtTokenProvider.resolveToken(any())).thenReturn("xxx.yyy.zzz2");
        when(jwtTokenProvider.getAuthentication("xxx.yyy.zzz2")).thenReturn(new UsernamePasswordAuthenticationToken("admin", "admin"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "xxx.yyy.zzz2");
        httpHeaders.add("Content-Type", "application/json");

        Note note2 = new Note(4L, "", "content-4");
        RequestBuilder requestBuilder1 = MockMvcRequestBuilders.post("/notes/create")
                .content(mapper.writeValueAsString(note2))
                .headers(httpHeaders);
        MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result1.getResponse().getStatus());
        assertTrue(result1.getResponse().getErrorMessage().contains("Validation failed"));
    }

    @Test
    public void testFindByIdSuccess() throws Exception {
        when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(true);
        when(jwtTokenProvider.resolveToken(any())).thenReturn("xxx.yyy.zzz");
        when(jwtTokenProvider.getAuthentication("xxx.yyy.zzz")).thenReturn(new UsernamePasswordAuthenticationToken("admin", "admin"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "xxx.yyy.zzz");

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/notes/id/1")
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mockMvc.perform(requestBuilder2).andReturn();
        assertEquals(HttpStatus.OK.value(), result2.getResponse().getStatus());
        Note returnedNote1 = mapper.readValue(result2.getResponse().getContentAsString(), Note.class);
        Note noteInDB = new Note(1L, "Title-1", "Content 1");
        assertTrue(noteInDB.equals(returnedNote1));
    }

    @Test
    public void testFindByTitileSuccess() throws Exception {
        when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(true);
        when(jwtTokenProvider.resolveToken(any())).thenReturn("xxx.yyy.zzz");
        when(jwtTokenProvider.getAuthentication("xxx.yyy.zzz")).thenReturn(new UsernamePasswordAuthenticationToken("admin", "admin"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "xxx.yyy.zzz");

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/notes/title/Title-9000")
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder2).andReturn();
        JavaType noteListlType = mapper.getTypeFactory()
                .constructParametrizedType(List.class, List.class, Note.class);

        List<Map> returnedObjects = mapper.readValue(result.getResponse().getContentAsString(), noteListlType);

        assertEquals(2, returnedObjects.size());

        Note note1 = new Note(9001L, "Title-9000", "Content 9001");
        Note note2 = new Note(9002L, "Title-9000", "Content 9002");
        assertTrue(returnedObjects.contains(note1));
        assertTrue(returnedObjects.contains(note2));
    }
}
