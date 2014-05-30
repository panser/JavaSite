package ua.org.gostroy.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ua.org.gostroy.entity.User;
import ua.org.gostroy.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:/etc/spring/applicationContext.xml", "classpath:/etc/spring/applicationContext.d/*",
        "classpath:/etc/spring/servletContext.xml", "classpath:/etc/spring/servletContext.d/*"})
@DirtiesContext
public class TestControllerTests {
    private MockMvc mockMvc;

//    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;

    private User testUser;
    @Before
    public void setup(){
        this.mockMvc = webAppContextSetup(this.wac).build();

        testUser = new User();
        testUser.setLogin("jUnitUserTestController");
        testUser.setEmail("jUnitUser@gostroy.org.ua");
        testUser.setEnabled(true);
        testUser.setPassword("jUnitUser");
        testUser.setRole("ROLE_ADMIN");
        userService.save(testUser);

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(testUser.getLogin(), testUser.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
    }
    @After
    public void destroy(){
        userService.delete(testUser);
    }


    @Test
    public void hello() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"));
    }

    @Test
    public void helloSecurity() throws Exception {
        mockMvc.perform(get("/test/security"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"));
    }
}
