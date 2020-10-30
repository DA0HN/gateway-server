package br.edu.ifmt.cba.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author daohn on 29/10/2020
 * @project gateway_server
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "estatisticas_ar_condicionado")
public class AirConditionerData implements IReceivedData {

    @Id
    private Long   timestamp;
    @Column(name = "_from")
    private String from;
    @Column(name = "_to")
    private String to;

    private Long elapsedTime;

    private Double temperature;
    private Double humidity;
    private Double power;
    private Double current; // current -> IRMS
    private Double energyConsumption; // -> kwhTotal_Acc
}
