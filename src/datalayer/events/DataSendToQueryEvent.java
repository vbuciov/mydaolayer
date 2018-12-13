package datalayer.events;

import datalayer.utils.QueryDataTransfer;

/*
 * Información sobre el evento que requiere crear un Query basado en los valores
 * proporcionados y que además se utilizarán en un objeto PreparedStatement.
 * @author Victor Manuel Bucio Vargas
 */
public class DataSendToQueryEvent extends QueryDataTransfer
{
    /* El envio de parametros complejos es solo para operaciones de CRUD, PROCEDURE, FUNCTION*/
    public enum Type { CREATE, RETRIEVE, UPDATE, DELETE }

    private Type Operation;

    //--------------------------------------------------------------
    public DataSendToQueryEvent(String name)
    {
        super(name);
    }

    //--------------------------------------------------------------
    public DataSendToQueryEvent(Type CurrentOperation)
    {
        super("");
        Operation = CurrentOperation;
    }
    
    //--------------------------------------------------------------
    public DataSendToQueryEvent(String name, Type CurrentOperation)
    {
        super(name);
        Operation = CurrentOperation;
    }

    //--------------------------------------------------------------
    public boolean isCreateOperation()
    {
        return Operation == Type.CREATE;
    }

    //--------------------------------------------------------------
    public boolean isRetrieveOperation()
    {
        return Operation == Type.RETRIEVE;
    }

    //--------------------------------------------------------------
    public boolean isUpdateOperation()
    {
        return Operation == Type.UPDATE;
    }

    //--------------------------------------------------------------
    public boolean isDeleteOperation()
    {
        return Operation == Type.DELETE;
    }

    //--------------------------------------------------------------
    public boolean isRequieredValues()
    {
        return isCreateOperation() || isUpdateOperation() ;
    }

    //--------------------------------------------------------------
    public boolean isRequieredConditions()
    {
        return !isCreateOperation();
    }
    
    //--------------------------------------------------------------
    public boolean isRequieredValuesAndConditions()
    {
        return  isUpdateOperation();
    }
    
    //--------------------------------------------------------------
    public Type getOperation()
    {
        return Operation;
    }
}