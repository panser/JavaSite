package ua.org.gostroy.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.org.gostroy.domain.User;

/**
 * Created by panser on 5/21/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/etc/spring/applicationContext.xml", "classpath:/etc/spring/applicationContext.d/*"})

public class UserServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;

    private User testUser;
    @Before
    public void setup(){
        testUser = new User();
        testUser.setLogin("jUnitUser");
        testUser.setEmail("jUnitUser@gostroy.org.ua");
        testUser.setEnabled(true);
        testUser.setPassword("jUnitUser");
        userService.save(testUser);
    }
    @After
    public void destroy(){
        userService.delete(testUser);
    }

    @Test
    public void findOne(){
        User newUser = userService.find(testUser.getId());
        Assert.assertEquals(newUser.getId(), testUser.getId());
    }

    @Test
    public void update(){
        User updateUser = userService.find(testUser.getId());
        userService.save(updateUser);
        Assert.assertEquals(updateUser.getId(), testUser.getId());
    }
}
