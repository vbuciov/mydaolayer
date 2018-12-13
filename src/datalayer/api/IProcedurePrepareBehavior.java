package datalayer.api;

import datalayer.events.ParameteRegisterEvent;

/**
 * Representa la metodología de configuración
 * de los parametros de salida de un procedimiento.
 * @author Victor Manuel Bucio Vargas
 */
public interface IProcedurePrepareBehavior
{
     void configureTo(ParameteRegisterEvent e);
}
