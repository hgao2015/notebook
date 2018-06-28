package com.ainfo.mynotes.Controller;

import com.ainfo.mynotes.domain.Role;
import com.ainfo.mynotes.domain.User;
import com.ainfo.mynotes.dto.UserCredential;
import com.ainfo.mynotes.security.JwtTokenProvider;
import com.ainfo.mynotes.service.UserService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerIntTest {
    private static String TOKEN = "xxx.yyy.zzz";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSignin() throws Exception {
        UserCredential userCredential = new UserCredential("admin", "admin");
        when(userService.signin(Mockito.any(UserCredential.class))).thenReturn("xxx.yyy.zzz");

        //RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/signin?username=admin&password=admin");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/signin")
                .content(mapper.writeValueAsString(userCredential))
                .headers(httpHeaders);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals("{\"token\":\"xxx.yyy.zzz\"}", result.getResponse().getContentAsString());
    }

    @Test
    public void testMyUser() throws Exception {
        when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(true);
        when(jwtTokenProvider.resolveToken(any())).thenReturn(TOKEN);
        List<Role> authorities = new ArrayList<>();
        authorities.add(Role.ROLE_ADMIN);
        when(jwtTokenProvider.getAuthentication(TOKEN)).thenReturn(new UsernamePasswordAuthenticationToken("admin", "admin"
                , authorities));

        when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", TOKEN);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/users/me")
                .headers(httpHeaders);

        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setEmail("admin@email.com");
        user.setRoles(Arrays.asList(new Role[]{Role.ROLE_ADMIN}));
        when(userService.whoami(Mockito.any(HttpServletRequest.class))).thenReturn(user);

        MvcResult result2 = mockMvc.perform(requestBuilder2).andReturn();
        assertEquals(HttpStatus.OK.value(), result2.getResponse().getStatus());
    }


}
