package class2.a204.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    private String to;
    private String content;

    public MessageDTO(String phone) {
        this.to = phone;
    }
}
