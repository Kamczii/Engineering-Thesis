package org.once_a_day.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage extends AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "content")
    String content;

    @Column(name = "created_time")
    @Builder.Default
    LocalDateTime createdTime = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "attachment_id")
    FileDetails attachment;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    ChatMessageType type;

    @Column(name = "seen")
    @Builder.Default
    boolean seen = false;

    @Column(name = "sent_time")
    LocalDateTime sentTime;
}
