package datalayer.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public interface ISynchronizableDataAccess<T>
{
   int Create(Connection session, T toAdd) throws SQLException;
   int Create(Connection session, List<T> toAdds) throws SQLException;
   /*List<T> Retrieve(Connection session);
   List<T> Retrieve(Connection session, T filter);*/
   int Update(Connection session, T toModified) throws SQLException;
   int Update(Connection session, List<T> toModifieds) throws SQLException;
   int Delete(Connection session, T toDeleted) throws SQLException;  
   int Delete(Connection session, List<T> toDeleteds) throws SQLException; 

}
