package darkskyapi.services;

/**
 * Created by daniel on 10.08.17.
 */

import java.util.Arrays;
import java.util.HashSet;

import darkskyapi.model.Role;
import darkskyapi.model.User;
import darkskyapi.repository.RoleRepository;
import darkskyapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setApiKey(createUserKey(user));
        userRepository.save(user);
    }

    @Override
    public User findUserByApikey(String apikey) {
        return userRepository.findByApikey(apikey);
    }

    public String createUserKey(User user){
        int x = user.getEmail().hashCode();
        return new Integer(x).toString();
    }

}
