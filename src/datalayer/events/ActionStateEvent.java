package datalayer.events;

import java.sql.Connection;

/**
 * Representa el estado en que se encuentra una operación del DataAccess.
 * @author Victor Manuel Bucio Vargas
 */
public class ActionStateEvent<T> extends ConnectionStateEvent
{
    private final T _value;
    //private final List<T> _values;
    
    /*    public ActionStateEvent(Connection session, String DML, T value, List<T> values, int affected)
    {
        super (session, DML, affected);
        _value = value;
        _values = values;
    }
    
    public ActionStateEvent(Connection session, String DML, T value, int affected)
    {
        this (session, DML, value, null, affected);
    }
    
       public ActionStateEvent(Connection session, String DML, List<T> values, int affected)
    {
        this (session, DML, null, values, affected);  
    }
    
    public ActionStateEvent(Connection session, String DML, T value)
    {
        this (session, DML, value, null, 0);     
    }
    
    public ActionStateEvent(Connection session, String DML, List<T> values)
    {
        this (session, DML, null, values, 0);  
    }*/
    
    public ActionStateEvent(Connection session, String DML, T value, int affected)
    {
        super (session, DML, affected);
        _value = value;
    }
    
    public ActionStateEvent(Connection session, String DML, T value)
    {
        this (session, DML, value, 0);     
    }
               
    public T getValue(){return _value; }
    /*public List<T> getValues(){return _values;}
    public boolean isBatchOperation() { return _values != null; }*/
}