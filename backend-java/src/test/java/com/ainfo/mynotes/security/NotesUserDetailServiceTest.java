package com.ainfo.mynotes.security;

import com.ainfo.mynotes.domain.Role;
import com.ainfo.mynotes.domain.User;
import com.ainfo.mynotes.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class NotesUserDetailServiceTest {
    @InjectMocks
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Test
    public void testLoadUserByUsername() {
        User testUser = new User();
        testUser.setId(1);
        testUser.setUsername("test");
        testUser.setPassword("test");
        testUser.setEmail("test@email.com");
        testUser.setRoles(Arrays.asList(new Role[]{Role.ROLE_ADMIN}));
        when(userRepository.findByUsername("test")).thenReturn(testUser);
        NotesUserDetailsService notesUserDetailsService = new NotesUserDetailsService(userRepository);
        UserDetails userDetails = notesUserDetailsService.loadUserByUsername("test");
        assertEquals("test", userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals(Role.ROLE_ADMIN, new ArrayList<>(userDetails.getAuthorities()).get(0));
    }
}
