package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.entity.User;
import ua.org.gostroy.repository.UserRepository;

import java.util.List;

/**
 * Created by panser on 5/21/14.
 */
@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public User find(Long id) {
        log.trace("find ...");
        User user = userRepository.findOne(id);
        log.trace("find.");
        return user;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        log.trace("findByEmail ...");
        User user = userRepository.findByEmail(email);
        log.trace("findByEmail.");
        return user;
    }
    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        log.trace("findByLogin ...");
        User user = userRepository.findByLogin(login);
        log.trace("findByLogin.");
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.trace("findAll ...");
        List<User>  users = userRepository.findAll();
        log.trace("findAll.");
        return users;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long save(User user) {
        log.trace("save ...");
        userRepository.save(user);
        log.trace("save.");
        return user.getId();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public void delete(User user) {
        log.trace("delete ...");
        userRepository.delete(user);
        log.trace("delete.");
    }
}
