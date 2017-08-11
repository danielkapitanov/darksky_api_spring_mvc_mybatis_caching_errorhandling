package darkskyapi.repository;

/**
 * Created by daniel on 10.08.17.
 */
import darkskyapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByApikey (String apikey);
}
