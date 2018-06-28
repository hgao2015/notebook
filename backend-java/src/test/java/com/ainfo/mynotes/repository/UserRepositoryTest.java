package com.ainfo.mynotes.repository;

import com.ainfo.mynotes.domain.Role;
import com.ainfo.mynotes.domain.User;
import com.ainfo.mynotes.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repository;
    
    @Test
    public void testExistsByUsername() {
        User admin = new User();
        admin.setUsername("test1");
        admin.setPassword("test1");
        admin.setEmail("test1@email.com");
        admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));
        userService.signup(admin);
        assertTrue(repository.existsByUsername("test1"));
        assertFalse(repository.existsByUsername("test999"));
    }

    @Test
    public void testFindByUsername() {
        User admin = new User();
        admin.setUsername("test2");
        admin.setPassword("test2");
        admin.setEmail("test2@email.com");
        admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_CLIENT)));
        userService.signup(admin);

        User test2 = repository.findByUsername("test2");
        assertEquals("test2", test2.getUsername());
        assertEquals("test2@email.com", test2.getEmail());
        assertEquals(Role.ROLE_CLIENT, test2.getRoles().get(0));

    }
}
