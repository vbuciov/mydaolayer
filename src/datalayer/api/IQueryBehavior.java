
package datalayer.api;

import datalayer.events.DataSendToQueryEvent;

/**
 *
 * @author victor
 */
public interface IQueryBehavior<T>
{
     void converTo(DataSendToQueryEvent e, T value);
}
