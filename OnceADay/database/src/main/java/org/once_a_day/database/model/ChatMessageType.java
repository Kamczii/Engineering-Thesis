package org.once_a_day.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.once_a_day.messenger.messenger_common.enums.MessageType;

@Entity
@Table(name = "message_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageType extends AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "message_type_id")
    private Long id;

    @Column(name = "value")
    @Enumerated(EnumType.STRING)
    private MessageType type;

}
