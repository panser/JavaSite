package ua.org.gostroy.repository;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.org.gostroy.entity.User;

/**
 * Created by panser on 5/21/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/etc/spring/applicationContext.xml", "classpath:/etc/spring/applicationContext.d/*",
        "classpath:/etc/spring/servletContext.xml", "classpath:/etc/spring/servletContext.d/*"})
public class UserRepositoryTests {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserRepository userRepository;

    private User testUser;
    @Before
    public void setup(){
        testUser = new User();
        userRepository.save(testUser);
    }
    @After
    public void destroy(){
        userRepository.delete(testUser);
    }

    @Test
    public void findOne(){
        User newUser = userRepository.findOne(testUser.getId());
        Assert.assertEquals(newUser.getId(),testUser.getId());
    }

    @Test
    public void update(){
        User updateUser = userRepository.findOne(testUser.getId());
        User newUser = userRepository.save(updateUser);
        Assert.assertEquals(updateUser.getId(), newUser.getId());
    }
}
