package br.edu.ifmt.cba.gateway.service;

import javax.persistence.EntityManager;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class ReceivedDataService {

    private EntityManager manager;

    public ReceivedDataService(EntityManager manager) {
        this.manager = manager;
    }



}
