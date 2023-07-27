package class2.a204.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageDTO {
    String to;
    String content;
}
