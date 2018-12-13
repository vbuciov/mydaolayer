/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer.api;

import datalayer.events.ConnectionStateEvent;
import java.sql.SQLException;

/**
 *
 * @author victor
 */
public interface IBasicTransactionSynchronizeBehavior
{
    void execute(ConnectionStateEvent e) throws SQLException;
}
