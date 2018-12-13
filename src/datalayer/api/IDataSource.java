package datalayer.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Representa un origen de datos, 
 * también es una fabrica de conexiones 
 * hacia el origen de datos que representa.
 * @author Victor Manuel Bucio Vargas
 */
public interface IDataSource
{
   /**
    * <p>
    * Attempts to establish a connection with the data source that this
    * <code>DataSource</code> object represents. The DataSource holds a
    * reference to any connection got throught
    *
    * @return a connection to the data source
    * @exception SQLException if a database access error occurs
    */
   Connection createSession() throws SQLException;

   /**
    *  @Deprecated.- 09-10-2016 - DataAccess can configure the session 
   as it need it.
    * <p>
    * Attempts to establish a connection with the data source that this
    * <code>DataSource</code> object represents. The DataSource holds a
    * reference to any connection got throught
    *
    * @param isAutoCommit
    * @return a connection to the data source
    * @exception SQLException if a database access error occurs
    */
   //Connection createSession(boolean isAutoCommit) throws SQLException;

   /**
    * <p>
    * Este método revisa el estado de una conexión, esta pensando para
    * utilizarse en un bloque catch, También es posible indicar si se desea
    * marcar disponible la conexión tras sido Además gestiona cualquier excepción
    * adicional que se genera al realizar la operación
    *
    * @param session La session que se quiere cerrar.
    * @param disconnect Indica que la conexión también será deconectada.
    * @return un indicador que define el estado de la conexión.
    */
   boolean handleError(Connection session, boolean disconnect);

   
   /**
    * <p>
    * Este método revisa una excepcion dada, para escribir un LOG 
    *
    * @param ex
     * @param source
    * @return Devuelve el mensaje escrito en el LOG.
    */
   String handleExceptionLog(SQLException ex, String source);
   
   
   /**
    * <p>
    * Retrieves the log writer for this <code>DataSource</code> object.
    *
    * <p>
    * The log writer is a character output stream to which all logging and
    * tracing messages for this data source will be printed. This includes
    * messages printed by the methods of this object, messages printed by
    * methods of other objects manufactured by this object, and so on. Messages
    * printed to a data source specific log writer are not printed to the log
    * writer associated with the <code>java.sql.DriverManager</code> class. When
    * a <code>DataSource</code> object is created, the log writer is initially
    * null; in other words, the default is for logging to be disabled.
    *
    * @return the log writer for this data source or null if logging is disabled
    * @exception java.sql.SQLException if a database access error occurs
    * @see #setLogWriter
    * @since 1.4
    */
   //java.io.PrintWriter getLogWriter() throws SQLException;
}
