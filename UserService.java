import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private CustomUserRepositoryImpl userRepository;

    public List<User> getUsersByFilters(List<UserFilterInput> filters) {
        return userRepository.findUsersByFilters(filters);
    }
}
