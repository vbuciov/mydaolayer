package datalayer;

import datalayer.api.IMediatorDataSource;
import datalayer.api.IResultSetKeyBehavior;
import datalayer.events.ActionStateEvent;
import datalayer.events.DataSetEvent;
import datalayer.api.ITransferBehavior;
import datalayer.api.ITypeableTransactionSyncronizeBehavior;
import datalayer.events.ConnectionStateEvent;
import datalayer.events.DataGetEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Proporciona algoritmos basicos para recuperación y actualización de datos; 
 * Utilizando un PreparedStatement por lo que existe optimizacion para 
 * procesamiento por lotes y protección en parametros contra SQL Injection.
 * @author Victor Manuel Bucio Vargas
 * @param <T> Tipo de objeto a utilizar
 */
public abstract class BatchJDBCOperationSet<T> extends BasicJDBCOperationSet<T> 
{
    private IResultSetKeyBehavior<T> resultkey_behaivor;
    private ITransferBehavior<T> transfer_behaivor;
    protected ITypeableTransactionSyncronizeBehavior<T> navigationPropertyUpdateBehavior;

    //----------------------------------------------------------------
    public BatchJDBCOperationSet(IMediatorDataSource mysource)
    {
        super(mysource);
    }
    
    //----------------------------------------------------------------
    public BatchJDBCOperationSet(IMediatorDataSource mysource, String[] idColumnsNames )
    {
        super(mysource, idColumnsNames);
    }
    
    //----------------------------------------------------------------
    public BatchJDBCOperationSet(IMediatorDataSource mysource, int[] idColumnsIndexes )
    {
        super(mysource, idColumnsIndexes);
    }
    
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la SQL injection .
     *
     * @param query El query a preparar
     * @param value Los valores que deben ser introducidos
     * @param getAutoID
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String query, T value, boolean getAutoID)
    {
        int affected = -1;
        Connection session = null;

        try
        {
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            affected = doUpdate (session, query, value, getAutoID);         

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_AFECTED, affected, query);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCOperationSet.class.getName());
        }

        return affected;
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la SQL injection .
     *
     * @param query El query a preparar
     * @param value Los valores que deben ser introducidos
     * @return Un valor que representa el número de registros afectados.
     */    
    public int submitUpdate(String query, T value)
    {
        return submitUpdate(query, value, false);
    }
           
    //--------------------------------------------------------------------
    /**
     * Solo ejecuta el query roporcionado en la session indicada.
     * @param session
     * @param query
     * @param value
     * @param getAutoID
     * @return 
     * @throws java.sql.SQLException 
     */
    public int doUpdate (Connection session, String query, 
                         T value, boolean getAutoID) throws SQLException
    {
        int affected;

        //2.1.- Si se solicto Antes la preparación de una operación externa.
        if(beforeUpdateBehavior != null)
            beforeUpdateBehavior.execute(new ConnectionStateEvent(session, query));
        
        //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
        PreparedStatement request; 
        
        if (!getAutoID)
            request = dataSource.prepareStatement(query, session, getAutoID);
        else
            request = howCreateForAutoID(query, dataSource, session);

        //2.3.- Se solicita el establecimiento de parametros.
        onConvertTransfer(value, new DataSetEvent(query, request, 
            request.getParameterMetaData()));

        //2.4.- Enviamos la petición al servidor.
        affected = request.executeUpdate();
        
        //2.4.1- Leemos los valores de llaves primarias generados.
        if (getAutoID)
            fillValueWithAutoID(session, request.getGeneratedKeys(), query, value);
        
        else if (null != navigationPropertyUpdateBehavior )
            navigationPropertyUpdateBehavior.execute( new ActionStateEvent<>(session, query, value));
        
        //2.5.- Liberamos la petición utilizada.
        request.close();
        
        //2.6.- Si se solicito Después la preparación de una operación externa.
        if (afterUpdateBehavior != null)
            afterUpdateBehavior.execute(new ConnectionStateEvent(session, query, affected));
        
        return affected;
    }
    
    //--------------------------------------------------------------------
    /**
     * Solo ejecuta el query roporcionado en la session indicada.
     * @param session
     * @param query
     * @param value
     * @return 
     * @throws java.sql.SQLException 
     */
    public int doUpdate (Connection session, String query, 
                         T value) throws SQLException
    {
        return doUpdate(session, query, value, false);
    }
  
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param query El query a preparar
     * @param values Los valores que deben ser introducidos
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String query, List<T> values)
    {
        return submitUpdate(query, values, true, DEFAULT_BATCH, false);
    }
    
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param query El query a preparar
     * @param values Los valores que deben ser introducidos
     * @param isAtomic
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String query, List<T> values, boolean isAtomic)
    {
        return submitUpdate(query, values, isAtomic, DEFAULT_BATCH, false);
    }
    
        //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param query El query a preparar
     * @param values Los valores que deben ser introducidos
     * @param isAtomic
     * @param getAutoID
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String query, List<T> values, boolean isAtomic, boolean getAutoID)
    {
        return submitUpdate(query, values, isAtomic, DEFAULT_BATCH, getAutoID);
    }
 
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param query El query a preparar
     * @param values Los valores que deben ser introducidos
     * @param isAtomic
     * @param packetSize
     * @param getAutoID
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String query, List<T> values, boolean isAtomic, 
                            int packetSize, boolean getAutoID)
    {
        int affected = -1;
        Connection session = null;

        try
        {
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.2.- Iniciamos la transacción.
            session.setAutoCommit(!isAtomic);

            //2.- Efectuamos nuestra operación.
            affected = doUpdate (session, query, values, packetSize, getAutoID);
            
            //3.- Liberamos los recursos utilizados
            //3.1- Liberamos los recursos utilizados
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_AFECTED, affected, query);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCOperationSet.class.getName());
        }

        return affected;
    }
    
        //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param query El query a preparar
     * @param values Los valores que deben ser introducidos
     * @param isAtomic
     * @param packetSize
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String query, List<T> values, boolean isAtomic, int packetSize)
    {
        return submitUpdate(query, values, isAtomic, packetSize, false);
    }

    //--------------------------------------------------------------------
    /**
     * Solo ejecuta el query roporcionado en la session indicada.
     * @param session Transaction-Connection en donde se realiza la operacion
     * @param query Lo que se quiere ejecutar
     * @param values Los valores que se desean introducir en los parametros.
     * @param packetSize Indica la cantidad maxima de elementos por cada lote.
     * @param getAutoID
     * @return Devuelve la cantidad de elementos afectados por la operación.
     * @throws java.sql.SQLException
     */    
    public int doUpdate(Connection session, String query, List<T> values, 
                        int packetSize, boolean getAutoID) throws SQLException
    {
        int  batchAffected = 0, batchCount = 0, start=0;
        PreparedStatement request; 
        
        //2.1.- Si se solicito Antes la preparación de una operación externa.
        if(null != beforeUpdateBehavior)
            beforeUpdateBehavior.execute(new ConnectionStateEvent(session, query));
        
        //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
        if (!getAutoID)
            request = dataSource.prepareStatement(query, session, getAutoID);
        else
            request = howCreateForAutoID(query, dataSource, session);
        
        //2.3.- Se solicita el establecimiento de parametros.
        for (T value : values)
        {
            onConvertTransfer(value, new DataSetEvent(query, 
                    request, request.getParameterMetaData()));
            request.addBatch();
            batchCount++;

            // 2.2.1.- Se alcanzo el tamaño máximo esperado 
            if (batchCount % packetSize == 0)
            {
                batchAffected += calculateAffectedCount(request.executeBatch());
                if (getAutoID)
                    fillValueWithAutoID(session, request.getGeneratedKeys(), query, values,start);
                
                else if (null != navigationPropertyUpdateBehavior )
                    synchronizeNavigationValues (session, query, values, start, batchCount);
                    
                start += batchCount;
                batchCount = 0;                
            }
        }
        
        //2.4.- Ejecutamos cualquier valor pendiente en nuestro lote.
        if (batchCount > 0)
        {
            batchAffected += calculateAffectedCount(request.executeBatch());
            if (getAutoID)
                fillValueWithAutoID(session, request.getGeneratedKeys(), query, values, start);
            
            else if (null != navigationPropertyUpdateBehavior )
                synchronizeNavigationValues (session, query, values, start, batchCount);
        }

        //2.5.- Cerramos la petición empleada
        request.close();
        
        //2.6.- Si se solicitó Después la preparación de una operación externa.
        if (null != afterUpdateBehavior)
            afterUpdateBehavior.execute(new ConnectionStateEvent(session, query, batchAffected));
     
        return batchAffected;
    }
    
       //--------------------------------------------------------------------
    /**
     * Solo ejecuta el query roporcionado en la session indicada.
     * @param session Transaction-Connection en donde se realiza la operacion
     * @param query Lo que se quiere ejecutar
     * @param values Los valores que se desean introducir en los parametros.
     * @param packetSize Indica la cantidad maxima de elementos por cada lote.
     * @return Devuelve la cantidad de elementos afectados por la operación.
     * @throws java.sql.SQLException
     */    
    public int doUpdate(Connection session, String query, List<T> values, 
                        int packetSize) throws SQLException
    {
        return doUpdate(session, query, values, packetSize, false);
    }


    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar y convertir en List el resultado de un
     * Query.
     *
     * @param query La sentencia a ejecutar
     * @param filter
     * @return Una lista de objetos ;<T>
     */
    public List<T> submitQuery(String query, T filter)
    {
        List<T> result = new ArrayList();
        Connection session = null;

        try
        {
            ResultSet response;

            //1.- Conectar la session con el servidor
            //1.1- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            PreparedStatement request = dataSource.prepareQueryStatement(query, session);

            onConvertTransfer(filter, new DataSetEvent(query, request, request.getParameterMetaData()));

            response = request.executeQuery();
            fillResult(response, query, result);
            //response.close(); fill method closes the response
            request.close();

            //3.2.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), query);
            state = result.size() > 0 ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCOperationSet.class.getName());
        }

        return result;
    }
    
     //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param query El query a preparar
     * @param filters
     * @return Un valor que representa el número de registros afectados.
     */
    public List<T> submitQuery(String query, List<T> filters)
    {
        return submitQuery(query, filters, 1);
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param query El query a preparar
     * @param filters
     * @param packet_size
     * @return Un valor que representa el número de registros afectados.
     */
    public List<T> submitQuery(String query, List<T> filters, int packet_size)
    {
        int batchCount = 0, batchSuccess = 0;
        List<T> result = new ArrayList();
        ResultSet response = null;
        Connection session = null;
     
        try
        {
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            PreparedStatement request = dataSource.prepareQueryStatement(query, session);
            
            for (T filter : filters)
            {
                onConvertTransfer(filter, new DataSetEvent(query, request, 
                        request.getParameterMetaData()));
                
                if (packet_size < 2)
                    response = request.executeQuery();
               
                else
                {
                    request.addBatch();
                    batchCount++;
   
                    //2.2.1.- Ejecutamos una parcialidad.
                    if (batchCount % packet_size == 0)
                    {
                        batchSuccess = 
                            calculateSuccessCount(dataSource.executeQueryBatch(request));
                        response = request.getResultSet();
                        batchCount = 0;
                        //currentQuery.delete(0, currentQuery.length());
                    }
                }

                //2.2.-Efectuamos operación y Evaluamos la existencia de algún resultado.
                if (null != response)
                {
                    do
                    {
                        if (null == response)
                            response = request.getResultSet();
                        fillResult(response, query, result);
                        //response.close(); fill method closes the response
                        response = null;
                    }
                    while (request.getMoreResults());
                }
            }
            
            //2.3.- Comprobamos cualquier remanente por ejecutar.
            if (batchCount > 0)
            {
                batchSuccess = 
                    calculateSuccessCount(dataSource.executeQueryBatch(request));
                do
                {
                 response = request.getResultSet();
                 fillResult(response, query, result); 
                 //response.close(); fill method closes the response
                }
                while (request.getMoreResults());
            }

            //3.- Liberamos los recursos utilizados
            //3.1.- Cerramos la petición empleada
            request.close();

            //3.2.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            message = String.format(MSJ_AFECTED, batchSuccess, query);
            state = result.size() > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCOperationSet.class.getName());
        }

        return result;
    }
    
        //--------------------------------------------------------------------
    /**
     * This factory method calculate how to create a Preparedstatment based on 
     * which information we know about the primary key.
     */
    final protected PreparedStatement howCreateForAutoID(String query, IMediatorDataSource source, Connection session) throws SQLException
    {
         if (_idColumnsNames != null)
            return source.prepareStatement(query, session, _idColumnsNames);
        
        else if (_idColumnsIndexes != null)
            return source.prepareStatement(query, session, _idColumnsIndexes);
        
        else
            return source.prepareStatement(query, session, true);
    }
    
    //--------------------------------------------------------------------
    final protected void synchronizeNavigationValues (Connection session, String query, List<T> values, int start, int batchCount) throws SQLException
    {
        for (int i = 0; i < batchCount; i++)
                navigationPropertyUpdateBehavior.execute( new ActionStateEvent<>(session, query, values.get(start++)));
    }

    //--------------------------------------------------------------------
    final protected void fillValueWithAutoID(Connection session, ResultSet response, String query, T value) throws SQLException
    {
        DataGetEvent evento = new DataGetEvent(query, response, response.getMetaData());

        while (response.next())
        {
            onConvertKeyResult(evento, value);
            if (null != navigationPropertyUpdateBehavior )
                navigationPropertyUpdateBehavior.execute( new ActionStateEvent<>(session, query, value));
        }
        
        response.close();
    }
   
    
     //--------------------------------------------------------------------
    final protected void fillValueWithAutoID(Connection session, ResultSet response, String query, List<T> values, int start) throws SQLException
    {
        DataGetEvent evento = new DataGetEvent(query, response, response.getMetaData());
        
        while (response.next())
        {
            onConvertKeyResult(evento, values.get(start));
            if (null != navigationPropertyUpdateBehavior )
                navigationPropertyUpdateBehavior.execute( new ActionStateEvent<>(session, query, values.get(start)));
            start++;
        }
        
        response.close();
    }
    
    //----------------------------------------------------------------
    /**
     * The ResultSet with AutoIDs will be converted to Transfer Object.
     * Note: This event executes once per each element.
     * @param e
     * @param value
     * @throws java.sql.SQLException
     */
    protected void onConvertKeyResult(DataGetEvent e, T value) throws SQLException, UnsupportedOperationException
    {
        if (resultkey_behaivor == null)
            throw new UnsupportedOperationException("Convert Key Result to Transfer Object, Not supported yet.");

        else
            resultkey_behaivor.convertTo(e, value);
    }

    //----------------------------------------------------------------
    /**
     * When the Query is previous Done, you only especify the parameteres.
     * Note: This event executes once per each element.
     * @param values
     * @param e
     * @throws java.sql.SQLException
     */
    protected void onConvertTransfer(T values, DataSetEvent e) throws SQLException, UnsupportedOperationException
    {
        if (transfer_behaivor == null)
            throw new UnsupportedOperationException("Convert Transfer to Parameters values, Not supported yet.");

        else
            transfer_behaivor.converTo(values, e);
    }
    

    //----------------------------------------------------------------
    /**
     * Obtiene el proveedor de comportamiento de conversión para los objetos
     * hacia valores de parametro de un Statement
     *
     * @return
     */
    public ITransferBehavior<T> getTransfer_behaivor()
    {
        return transfer_behaivor;
    }

    //----------------------------------------------------------------
    /**
     * Establece el proveedor de comportamiento de conversión para los objetos
     * hacia valores de parametro de un Statement
     *
     * @param value
     */
    public void setTransfer_behaivor(ITransferBehavior<T> value)
    {
        this.transfer_behaivor = value;
    }

    //----------------------------------------------------------------
    public IResultSetKeyBehavior<T> getResultkey_behaivor()
    {
        return resultkey_behaivor;
    }

    //----------------------------------------------------------------
    public void setResultkey_behaivor(IResultSetKeyBehavior<T> resultkey_behaivor)
    {
        this.resultkey_behaivor = resultkey_behaivor;
    }

    //----------------------------------------------------------------
    public ITypeableTransactionSyncronizeBehavior<T> getNavigationPropertyUpdateBehavior()
    {
        return navigationPropertyUpdateBehavior;
    }

    //----------------------------------------------------------------
    public void setNavigationPropertyUpdateBehavior(ITypeableTransactionSyncronizeBehavior<T> navigationPropertyUpdateBehavior)
    {
        this.navigationPropertyUpdateBehavior = navigationPropertyUpdateBehavior;
    }
    
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param query El query a preparar
     * @param value Los valores que deben ser introducidos
     * @return Un valor que representa el número de registros afectados.
     */
    /*public int submitUpdateGetAutoKey(String query, T value)
    {
        int affected = -1;
        Connection session = null;

        try
        {
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            //2.1.- Si se solicito Antes la preparación de una operación externa.
            if(TypeableBeforeUpdateBehavior != null)
                TypeableBeforeUpdateBehavior.execute(new ActionStateEvent<T>(session, query, value));
            
            //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            PreparedStatement request = dataSource.prepareStatement(query, session, true);

            //2.3.- Se solicita el establecimiento de parametros.
            onConvertTransfer(new DataSetEvent<>(query, request, request.getParameterMetaData(), value));
            affected = request.executeUpdate();

            //2.4.- Buscamos las llaves generadas 
            //TODO: Call onConvertKeys is more or like ConvertResult
            ResultSet response = request.getGeneratedKeys();
            if (response.next())
                affected = response.getInt(1);
            response.close();
            
            //2.5.- Si se solicito Después la preparació de una operación externa.
            if (TypeableAfterUpdateBehavior != null)
               TypeableAfterUpdateBehavior.execute(new ActionStateEvent<T>(session, query, value, affected));

            //3.- Liberamos los recursos utilizados.
            //3.1- Liberamos la petición utilizada
            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_AFECTED, affected, query);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, PreparedDataAccess.class.getName());
        }

        return affected;
    }*/
}