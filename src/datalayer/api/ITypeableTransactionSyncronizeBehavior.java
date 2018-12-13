package datalayer.api;

import datalayer.events.ActionStateEvent;
import java.sql.SQLException;

/**
 * Representa la metodología para ejecutar una operación
 * externa al tiempo que se realiza la ejecución de otra más
 * en el DataAccess.
 * @author Victor Manuel Bucio Vargas
 */
public interface ITypeableTransactionSyncronizeBehavior<T>
{
    void execute(ActionStateEvent<T> e) throws SQLException;
}
