package br.edu.ifmt.cba.gateway.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
@Entity
@Data
@Table(name = "received_data")
public class ReceivedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "_from")
    private String from;
    @Column(name = "_to")
    private String to;
    private LocalDate receivedTime;
    private LocalDate sendTime;
    @ElementCollection
    @CollectionTable(name = "received_message", joinColumns = @JoinColumn(name = "received_id"))
    private List<String> message;

    public ReceivedData(String from, String to, LocalDate sendTime,
                        LocalDate receivedTime, List<String> message) {
        this.from = from;
        this.to = to;
        this.sendTime = sendTime;
        this.receivedTime = receivedTime;
        this.message = message;
    }

}
