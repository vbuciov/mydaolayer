/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer.events;

import java.sql.Connection;

/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public class ConnectionStateEvent
{
    private final String _DML;
    private final Connection _session;
    private final int _affected;

    public ConnectionStateEvent(Connection session, String DML, int affected)
    {
        _session = session;
        _DML = DML;
        _affected = affected;
    }
    
     public ConnectionStateEvent(Connection session, String DML)
    {
        this (session, DML, 0);
    }

    public String getDML() {return _DML; }
    
    public int getAffected(){ return _affected; }

    public Connection getSession() {return _session; }
}
