package com.ainfo.mynotes.service;

import com.ainfo.mynotes.domain.Role;
import com.ainfo.mynotes.domain.User;
import com.ainfo.mynotes.dto.UserCredential;
import com.ainfo.mynotes.security.JwtTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIntTest {
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @Test
    public void testSignIn() {
        String expectedToken = "xxx.yyy.zzz";
        when(jwtTokenProvider.createToken("admin", Arrays.asList(new Role[]{Role.ROLE_ADMIN}))).thenReturn(expectedToken);
        String token = userService.signin(new UserCredential("admin", "admin"));
        assertEquals(expectedToken, token);
    }

    @Test
    public void testSignUpAndSearch() {
        String expectedToken = "xxx.yyy.zzz";
        when(jwtTokenProvider.createToken("client2", Arrays.asList(new Role[]{Role.ROLE_CLIENT}))).thenReturn(expectedToken);
        User user = new User();
        user.setUsername("client2");
        user.setPassword("client2");
        user.setEmail("client2@email.com");
        user.setRoles(Arrays.asList(new Role[]{Role.ROLE_CLIENT}));
        String token = userService.signup(user);
        assertEquals(expectedToken, token);
        User client2 = userService.search("client2");
        assertEquals(client2.getUsername(), "client2");
        assertEquals(client2.getEmail(), "client2@email.com");
        assertEquals(client2.getRoles().get(0), Role.ROLE_CLIENT);
    }
}
