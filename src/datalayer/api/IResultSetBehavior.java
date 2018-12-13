package datalayer.api;

import datalayer.events.DataGetEvent;

/**
 * Representa la metodología de conversión
 * de un registro en el resultado a un objeto de transferencia.
 * @author Victor Manuel Bucio Vargas
 * @param <T> TransferObject
 */
public interface IResultSetBehavior<T>
{
    T convertTo(DataGetEvent e);
}
