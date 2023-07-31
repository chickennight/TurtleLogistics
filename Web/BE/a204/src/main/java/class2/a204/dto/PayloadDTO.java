package class2.a204.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PayloadDTO {
    private String topic;

    private String message;
}
