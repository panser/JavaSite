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
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.model.User;

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
    @Transactional
    public void setup(){
        testUser = new User();
        testUser.setLogin("jUnitUser");
        testUser.setEmail("jUnitUser@gostroy.org.ua");
        testUser.setEnabled(true);
        testUser.setPassword("jUnitUser");
        userService.create(testUser);
    }
    @After
    @Transactional
    public void destroy(){
        userService.delete(testUser);
    }

    @Test
    @Transactional
    public void findOne(){
        User newUser = userService.find(testUser.getId());
        Assert.assertEquals(newUser.getId(), testUser.getId());
    }

    @Test
    @Transactional
    public void update(){
        User updateUser = userService.find(testUser.getId());
        userService.update(updateUser);
        Assert.assertEquals(updateUser.getId(), testUser.getId());
    }
}
