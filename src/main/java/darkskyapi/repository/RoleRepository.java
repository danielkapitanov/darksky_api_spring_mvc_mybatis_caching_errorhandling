package darkskyapi.repository;

/**
 * Created by daniel on 10.08.17.
 */
import darkskyapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findByRole(String role);

}
