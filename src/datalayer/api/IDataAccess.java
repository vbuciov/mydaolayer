package datalayer.api;

import java.util.List;

/**
 * Operaciones básicas para un DAO
 * @author Victor Manuel Bucio Vargas
 * @param <T> El objeto de transferencia con el que se interactua.
 */
public interface IDataAccess<T>
{
   int Create(T toAdd);
   int Create(List<T> toAdds);
   List<T> Retrieve();
   List<T> Retrieve(T filter);
   int Update(T toModified);
   int Update(List<T> toModifieds);
   int Delete(T toDeleted);  
   int Delete(List<T> toDeleteds);  
}
