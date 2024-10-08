import lombok.Data;

@Data
public class UserFilterInput {
    private String key;
    private String value;
    private String startDate;
    private String endDate;
    private FilterOperation operation;
}
