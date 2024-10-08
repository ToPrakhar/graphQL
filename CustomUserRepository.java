import java.util.List;

public interface CustomUserRepository {
    List<User> findUsersByFilters(List<UserFilterInput> filters);
}
