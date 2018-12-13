package datalayer.api;

/**
 * Especifica las propieades que debe contener una Entidad
 * @author Victor Manuel Bucio Vargas
 */
public interface IEntity extends IQueryDataTransferCollection
{
    String getTableName();
        
    int getReturnType();
}
