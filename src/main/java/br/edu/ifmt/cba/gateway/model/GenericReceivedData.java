package br.edu.ifmt.cba.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "received_data")
public class GenericReceivedData implements IReceivedData {

    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          raw;
    @Column(name = "_from")
    private String        from;
    @Column(name = "_to")
    private String        to;
    private LocalDateTime receivedTime;
    private LocalDateTime sendTime;
    @ElementCollection
    @CollectionTable(name = "received_message", joinColumns = @JoinColumn(name = "received_id"))
    private List<String>  message;

}