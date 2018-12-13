package datalayer.api;

import datalayer.events.DataSetEvent;

/**
 * Representa la metodología de conversión
 * de un objeto de transferencia a parámetros.
 * @author Victor Manuel Bucio Vargas
 * @param <T> TransferObject
 */
public interface ITransferBehavior<T>
{
    void converTo( T values, DataSetEvent e);
}
