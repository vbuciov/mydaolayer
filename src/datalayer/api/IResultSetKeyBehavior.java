package datalayer.api;

import datalayer.events.DataGetEvent;

/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public interface IResultSetKeyBehavior<T>
{
     void convertTo(DataGetEvent e, T value);
}
