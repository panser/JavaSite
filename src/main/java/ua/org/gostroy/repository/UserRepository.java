package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.org.gostroy.model.User;

/**
 * Created by panser on 5/20/14.
 */
@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByEmail(String email);
//    User findByLogin(String email);
    User findByLogin(String login);
    User findByRegUrI(String regUrI);
}