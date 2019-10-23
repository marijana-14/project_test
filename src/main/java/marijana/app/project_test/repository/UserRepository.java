package marijana.app.project_test.repository;

import marijana.app.project_test.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

   Optional<User> findByUsername(String username);
   Optional<User> findByEmail(String email);
}
