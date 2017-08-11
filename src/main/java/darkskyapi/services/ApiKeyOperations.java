package darkskyapi.services;

import darkskyapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by daniel on 11.08.17.
 */
@Service
public class ApiKeyOperations {

    @Autowired
    UserServiceImpl userService;

    public boolean isValid(String apikey){
        User user = userService.findUserByApikey(apikey);
        if (user==null)
            return false;
        else
            return true;
    }
}
