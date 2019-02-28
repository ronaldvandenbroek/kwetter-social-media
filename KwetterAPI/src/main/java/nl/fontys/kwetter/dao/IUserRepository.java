package nl.fontys.kwetter.dao;

import nl.fontys.kwetter.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IUserRepository extends CrudRepository<User, Long> {

    List<User> findByName(String name);
}