package class2.a204.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsRequestDTO {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<MessageDTO> messages;

}