package darkskyapi.services;

import darkskyapi.model.User;

/**
 * Created by daniel on 10.08.17.
 */

public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);
    public User findUserByApikey (String apikey);
}
