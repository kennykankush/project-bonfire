package ai.lumidah.bonfire_v1_app_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ai.lumidah.bonfire_v1_app_backend.model.User;
import ai.lumidah.bonfire_v1_app_backend.repository.UserRepository;

@Service
public class UserService {

    
    @Autowired
    private UserRepository userRepository;

    public List<User> allUsers(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public Optional<User> getUser(String username){
        return userRepository.findByUsername(username);
    }

    public Long getUserId(String username){

        Optional<User> Ouser = getUser(username);

        User user = Ouser.get();

        return user.getId();

    }

    public Boolean existByUsername(String username){
        return userRepository.existsByUsername(username);
    } 

    public Boolean existByEmail(String email){
        return userRepository.existsByUsername(email);
    }

    public String getPFP(String username){
        Optional<User> ouser = getUser(username);

        if (ouser.isPresent()){
            User user = ouser.get();
            String url = user.getProfilePictureUrl();

            return url;

        }

        return "https://cdn.discordapp.com/embed/avatars/4.png";
    }

    public List<User> searchUser(String querySearch){
        return userRepository.findTop10ByUsernameStartingWithIgnoreCase(querySearch);

    }

public String getPFPbyId(Long id) {
    Optional<User> ouser = userRepository.findById(id);

    User user = ouser.get();
    String profilePic = user.getProfilePictureUrl();

    return profilePic;

}

public void save(User user){
    userRepository.save(user);
}


}
