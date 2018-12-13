package datalayer.api;

import datalayer.utils.DefaultParameter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Este datasource encapsula los algoritmos particulares de cada
 * origen de datos para llevar cada operación en concreto.
 * @author Victor Manuel Bucio Vargas
 */
public interface IMediatorDataSource extends IDataSource
{
   /**
    * <p>
    * Realiza una operación de prueba para iniciar comunicación con el servidor
    *
    * @return un indicador que define el estado de la conexión.
    */

    boolean Connect ();

   /**
    * <p>
    * Revisa el estado de la connection proporcionada, y gestiona cualquier
    * excepción adicional que se genera como resultado de esta revisión.
    *
    * @param session La session que se quiere evaluar.
    * @return un indicador que define el estado de la conexión.
    */
   boolean isConnect(Connection session);

   /**
    * <p>
    * Este método marca como disponible una connection, que previamente había
    * sido obtenida a través de esta fabrica. Además gestiona cualquier
    * excepción adicional que se genera como resultado de utilizar dicha
    * Connection.
    *
    * @param session La session que se quiere cerrar.
    * @throws java.sql.SQLException
    */
   void disconnect(Connection session) throws SQLException;
   
   /**
    * <p>
    * Permite utilizar una misma sessión para acceder a otro Eschema o
    * BaseDatos. y gestiona cualquier excepción adicional que se genera como
    * resultado de intentar realizar dicha session.
    *
    * @param schema
    * @param session La session que se quiere cerrar.
    * @return un indicador que define el estado de la conexión.
    * @throws java.sql.SQLException
    */
   boolean use(String schema, Connection session) throws SQLException;
   
    /**
    * <p>
    * Permite comprobar si en la conexión se esta utilizando el esquema dado.
    *
    * @param schema
    * @param session La session que se quiere cerrar.
    * @return un indicador que define el estado de la conexión.
    * @throws java.sql.SQLException
    */
   //boolean isUse(String schema, Connection session) throws SQLException;
   
   /**
    * <p>
    * Permite consultar la información de un procedimiento almacenado. 
    * y gestiona cualquier excepción adicional que se genera como
    * resultado de intentar realizar dicha session.
    *
    * @param procedure
    * @param session La session que se quiere cerrar.
    * @return un indicador que define el estado de la conexión.
    * @throws java.sql.SQLException
    */
   List<DefaultParameter> getSchemaInformation(String procedure, Connection session) throws SQLException;

   /**
    * <p>
    * Crea un objeto Statement, al cual mantendrá una referencia para asegurar
    * su relación con la sessión actual.
    *
    * @param session
    * @return
    * @throws java.sql.SQLException
    */
   Statement createStatement(Connection session) throws SQLException;
   
   /**
    * <p>
    * Crea un objeto Statement, al cual mantendrá una referencia para asegurar
    * su relación con la sessión actual.Además configura las propiedades
    * asociadas con la recuperación de información.
    *
    * @param session
    * @return
    * @throws java.sql.SQLException
    */
   Statement createQueryStatement(Connection session) throws SQLException;

   /**
    * <p>
    * Crea un objeto PreparedStatement, al cual mantendrá una referencia para
    * asegurar su relación con la sessión actual.
    *
    * @param DML
    * @param session
    * @return
    * @throws java.sql.SQLException
    */
   PreparedStatement prepareStatement(String DML, Connection session) throws SQLException;
      
   /**
    * <p>
    * Crea un objeto PreparedStatement, al cual mantendrá una referencia para
    * asegurar su relación con la sessión actual.
    *
    * @param DML
    * @param session
     * @param getAutoId
    * @return
    * @throws java.sql.SQLException
    */
   PreparedStatement prepareStatement(String DML, Connection session, boolean getAutoId) throws SQLException;
   
   
   /**
    * <p>
    * Crea un objeto PreparedStatement, al cual mantendrá una referencia para
    * asegurar su relación con la sessión actual.
    *
    * @param DML
    * @param session
     * @param idColumnsNames
    * @return
    * @throws java.sql.SQLException
    */
   PreparedStatement prepareStatement(String DML, Connection session, String[] idColumnsNames) throws SQLException;
   
      /**
    * <p>
    * Crea un objeto PreparedStatement, al cual mantendrá una referencia para
    * asegurar su relación con la sessión actual. Además configura las propiedades
    * asociadas con la recuperación de información.
    *
    * @param DML
    * @param session
    * @return
    * @throws java.sql.SQLException
    */
   PreparedStatement prepareQueryStatement(String DML, Connection session) throws SQLException;
      
   /**
    * <p>
    * Crea un objeto PreparedStatement, al cual mantendrá una referencia para
    * asegurar su relación con la sessión actual.
    *
    * @param DML
    * @param session
     * @param idColumnsIndexes
    * @return
    * @throws java.sql.SQLException
    */
   PreparedStatement prepareStatement(String DML, Connection session, int[] idColumnsIndexes) throws SQLException;
   
   /**
    * <p>
    * Crea un objeto CallableStatement, al cual mantendrá una referencia para
    * asegurar su relación con la sessión actual.
    *
    * @param DML
    * @param session
    * @return
    * @throws java.sql.SQLException
    */
   CallableStatement prepareCall(String DML, Connection session) throws SQLException;
   
      /**
    * <p>
    * Crea un objeto CallableStatement, al cual mantendrá una referencia para
    * asegurar su relación con la sessión actual.
    *
    * @param DML
    * @param session
    * @return
    * @throws java.sql.SQLException
    */
   CallableStatement prepareQueryCall(String DML, Connection session) throws SQLException;
   
   /**
    * <p>
    * Actua para encapsular la metodología de ejecución de un lote de Querys.
    * Esta funcionalidad es poco común en los diferentes DBMS y pocos le dan 
    * soporte.
    *
     * @param request
     * @return 
     * @throws java.sql.SQLException 
    */
   public int[] executeQueryBatch (Statement request) throws SQLException;
}