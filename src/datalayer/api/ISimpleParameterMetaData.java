package datalayer.api;

/**
 * Complementa la información de un parametro.
 * @author Victor Manuel Bucio Vargas
 */
public interface ISimpleParameterMetaData
{
    boolean isAllowNull ();
     
    int getOrdinalIndex();
      
    int getDirection();
        
    int getSize ();
    
    int getPrecision();
    
    int getScale();
}
