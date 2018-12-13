package datalayer.events;

import datalayer.wrappers.DataRecordReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * Información sobre el evento que requiere leer valores
 * en un objeto ResultSet.
 * @author Victor Manuel Bucio Vargas
 */
public class DataGetEvent extends DataRecordReader
{   
   private final String _DML;

   public DataGetEvent(String DML, ResultSet toDectorate, ResultSetMetaData the_columns)
   {
      super(toDectorate, the_columns);
      _DML = DML;
   }
   
   /**
    * Obtiene la sentencia DML que desencadeno la ejecución de este evento.
     * @return 
    */
   public String getDML()
   {
      return _DML;
   }
}