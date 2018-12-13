package datalayer.api;

import datalayer.utils.QueryParameter;



/**
 * Permite la lectura de una lista de objetos de transferencia.
 * @author Victor Manuel Bucio Vargas
 */
public interface IQueryDataTransferCollection extends ISimpleParametersCollection
{         
    QueryParameter.Operator getOperatorOf (int index);
    
    boolean isConditional (int index);    

}
