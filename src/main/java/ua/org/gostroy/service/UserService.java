package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User find(Long id) {
        log.trace("find ...");
        User user = userRepository.findOne(id);
        log.trace("find.");
        return user;
    }

    public List<User> findAll() {
        log.trace("findAll ...");
        List<User>  users = userRepository.findAll();
        log.trace("findAll.");
        return users;
    }

    public Long save(User user) {
        log.trace("save ...");
        userRepository.save(user);
        log.trace("save.");
        return user.getId();
    }

    public void update(User user) {
        log.trace("update ...");
        userRepository.save(user);
        log.trace("update.");
    }

    public void delete(User user) {
        log.trace("delete ...");
        userRepository.delete(user);
        log.trace("delete.");
    }
}
