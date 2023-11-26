package softuni.WebFinderserver.model.entities;

import lombok.*;
import softuni.WebFinderserver.model.enums.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;



}
