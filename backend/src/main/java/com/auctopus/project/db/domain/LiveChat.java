package com.auctopus.project.db.domain;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
public class LiveChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int liveChatSeq;
    private int liveSeq;
    private String userEmail;
    private String message;
    private Timestamp date;
    private MessageType type;
    public enum MessageType {
        ENTER, TALK, BID, EXIT
    }

}
