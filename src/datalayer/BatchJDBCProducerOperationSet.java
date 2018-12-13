package datalayer;

import static datalayer.BasicJDBCUpdateSet.NOT_RECIEVED;
import static datalayer.BasicJDBCUpdateSet.SUCCESS_OPERATION;
import datalayer.api.IDMLGenerator;
import datalayer.api.IMediatorDataSource;
import datalayer.api.IQueryBehavior;
import datalayer.events.ActionStateEvent;
import datalayer.events.ConnectionStateEvent;
import datalayer.events.DataSendToQueryEvent;
import datalayer.generators.CommonDMLGenerator;
import datalayer.utils.BatchOperationCache;
import datalayer.utils.ProductRelation;
import datalayer.utils.Relation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provee en forma automatica de operaciones basicas CRUD y crea los querys
 * necesarios, para llevar acabo dichas operaciones.
 *
 * @author Victor Manuel Bucio Vargas
 * @param <T> Tipo de objeto a utilizar
 */
public abstract class BatchJDBCProducerOperationSet<T> extends BatchJDBCMappeableOperationSet<T> 
{
    protected IDMLGenerator dmlSintax;
    private IQueryBehavior<T> query_behaivor;    
    private final String _tableName;
    
    //----------------------------------------------------------------
    public BatchJDBCProducerOperationSet(IMediatorDataSource mysource, String tableName )
    {
        super(mysource);
        dmlSintax = new CommonDMLGenerator();
        _tableName = tableName;
    }
    
    
    //----------------------------------------------------------------
    public BatchJDBCProducerOperationSet(IMediatorDataSource mysource, String[] idColumnsNames, String tableName)
    {
        super(mysource, idColumnsNames);
        dmlSintax = new CommonDMLGenerator();
        _tableName = tableName;
    }
    
    //----------------------------------------------------------------
    public BatchJDBCProducerOperationSet(IMediatorDataSource mysource, int[] idColumnsIndexes, String tableName )
    {
        super(mysource, idColumnsIndexes);
        dmlSintax = new CommonDMLGenerator();
        _tableName = tableName;
    }
    
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la SQL injection .
     *
     * @param kindQuery
     * @param value Los valores que deben ser introducidos
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(DataSendToQueryEvent.Type kindQuery,
                             T value)
    {
        return submitUpdate(kindQuery, value, false);
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la SQL injection .
     *
     * @param kindQuery
     * @param value Los valores que deben ser introducidos
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(DataSendToQueryEvent.Type kindQuery,
                             T value, 
                             boolean getAutoID)
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
            affected = doUpdate(session, kindQuery, value, getAutoID);

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_AFECTED, affected, _tableName);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCProducerOperationSet.class.getName());
        }

        return affected;
    }

    //--------------------------------------------------------------------
    /**
     * GENERA y EJECUTA un QUERY en la session indicada.
     * 
     * @param session
     * @param kindQuery
     * @param value
     * @param getAutoID
     * @return
     * @throws java.sql.SQLException
     */
    public int doUpdate(Connection session,
                        DataSendToQueryEvent.Type kindQuery,
                        T value, boolean getAutoID) throws SQLException
    {
        int affected = -1;

        DataSendToQueryEvent event = new DataSendToQueryEvent(_tableName, kindQuery);
        onSendValues(event, value);

        if (event.size() > 0)
        {
            String query;

            query = createQuery(event);

            //2.1.- Si se solicto Antes la preparación de una operación externa.        
            if(beforeUpdateBehavior != null)
                beforeUpdateBehavior.execute(new ConnectionStateEvent(session, query));
            
            PreparedStatement request;

            //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            if (!getAutoID)
                request = dataSource.prepareStatement(query, session, getAutoID);
            else
                request = howCreateForAutoID(query, dataSource, session);

            //2.3.- Se solicita el establecimiento de parametros.
            onConvertEntity(request, request.getParameterMetaData(), event);

            //2.4.- Enviamos la petición al servidor.
            affected = request.executeUpdate();
            
            if (getAutoID)
                fillValueWithAutoID(session, request.getGeneratedKeys(), query, value);
            
            else if (null != navigationPropertyUpdateBehavior )
                navigationPropertyUpdateBehavior.execute( new ActionStateEvent<>(session, query, value));
       
            //2.5.- Liberamos la petición utilizada.
            request.close();
            
            //2.6.- Si se solicito después la preparación de una operación externa.
            if (afterUpdateBehavior != null)
                afterUpdateBehavior.execute(new ConnectionStateEvent(session, query, affected));
        }

        return affected;
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param kindQuery
     * @param values Los valores que deben ser introducidos
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(DataSendToQueryEvent.Type kindQuery, List<T> values)
    {
        return submitUpdate(kindQuery, values, true, DEFAULT_BATCH, false);
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param kindQuery
     * @param values Los valores que deben ser introducidos
     * @param isAtomic
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(DataSendToQueryEvent.Type kindQuery,
                            List<T> values, boolean isAtomic)
    {
        return submitUpdate(kindQuery, values, isAtomic, DEFAULT_BATCH, false);
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param kindQuery
     * @param values Los valores que deben ser introducidos
     * @param isAtomic
     * @param getAutoID
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(DataSendToQueryEvent.Type kindQuery,
                            List<T> values, boolean isAtomic, boolean getAutoID)
    {
        return submitUpdate(kindQuery, values, isAtomic, DEFAULT_BATCH, getAutoID);
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param kindQuery
     * @param values Los valores que deben ser introducidos
     * @param isAtomic
     * @param packetSize
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(DataSendToQueryEvent.Type kindQuery,
                            List<T> values, boolean isAtomic, int packetSize)
    {
        return submitUpdate(kindQuery, values, isAtomic, packetSize, false);
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param kindQuery
     * @param values Los valores que deben ser introducidos
     * @param isAtomic
     * @param packetSize
     * @param getAutoID
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(DataSendToQueryEvent.Type kindQuery,
                            List<T> values, boolean isAtomic, int packetSize, 
                            boolean getAutoID)
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
            affected = doUpdate(session, kindQuery, values, packetSize, getAutoID);

            //3.- Liberamos los recursos utilizados
            //3.1- Liberamos los recursos utilizados
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_AFECTED, affected, _tableName);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCProducerOperationSet.class.getName());
        }

        return affected;
    }

    //--------------------------------------------------------------------
    /**
     * GENERA y EJECUTA un QUERY en la session indicada.
     *
     * @param session
     * @param kindQuery
     * @param values
     * @param packetSize
     * @param getAutoID
     * @return
     * @throws java.sql.SQLException
     */
    public int doUpdate(Connection session,
                        DataSendToQueryEvent.Type kindQuery, 
                        List<T> values, 
                        int packetSize, boolean getAutoID) throws SQLException
    {
        int batchAffected = 0;
        BatchOperationCache current;
        PreparedStatement request;
        //Query optimization resources.
        Map<String, BatchOperationCache> cache = new HashMap<>();
        DataSendToQueryEvent event = new DataSendToQueryEvent(_tableName, kindQuery);
        String query;

        //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
        for (T value : values)
        {
            onSendValues(event, value);

            if (event.size() > 0)
            {
                query = createQuery(event);

                //2.1.- Si se solicito Antes la preparción de una operación externa.
                if(null != beforeUpdateBehavior)
                    beforeUpdateBehavior.execute(new ConnectionStateEvent(session, query));
                
                current = cache.get(query);
                if (current == null)
                {
                    if (!getAutoID)
                        request =  dataSource.prepareStatement(query, session);
                    else
                        request = howCreateForAutoID(query, dataSource, session);
                    current = new BatchOperationCache(request);
                    cache.put(query,current);
                }
                else
                    request = current.getRequest();

                onConvertEntity(request, request.getParameterMetaData(), event);
                request.addBatch();
                //event.clear(); deprecated onConvertEntity removes Elements
                current.increaseBatchCountby1();

                // 2.2.1.- Se alcanzo el tamaño máximo esperado 
                if (current.getBatchCount() % packetSize == 0)
                {
                    batchAffected += calculateAffectedCount(request.executeBatch());
                    if (getAutoID)
                        fillValueWithAutoID(session, request.getGeneratedKeys(), _tableName, values, current.getStart());
                    else if (null != navigationPropertyUpdateBehavior )
                        synchronizeNavigationValues (session, query, values, current.getStart(), current.getBatchCount());
                    current.increaseStartBy(current.getBatchCount());
                    current.resetBatchCount();
                }
            }
        }
        //2.3.- Ejecutamos cualquier valor pendiente en nuestro lote.
        for (Map.Entry<String, BatchOperationCache> entry : cache.entrySet())
        {
            current = entry.getValue();
            request = current.getRequest();
          
            if (current.getBatchCount() > 0)
            {
                batchAffected += calculateAffectedCount(request.executeBatch());
                if (getAutoID)
                   fillValueWithAutoID(session, request.getGeneratedKeys(), entry.getKey(), values, current.getStart());
                
                else if (null != navigationPropertyUpdateBehavior )
                   synchronizeNavigationValues (session, entry.getKey(), values, current.getStart(), current.getBatchCount());         
            }
            
            request.close();
        }
        cache.clear();
        //event.clear();       
        
        //2.6.- Si se solicito después la preparación de una operación externa.
        if (null != afterUpdateBehavior)
            afterUpdateBehavior.execute(new ConnectionStateEvent(session, _tableName, batchAffected));

        return batchAffected;
    }
    
        //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar y convertir en List el resultado de un
     * Query.
     *
     * @param storeObject
     * @param filter
     * @return Una lista de objetos ;<T>
     */
    public List<T> submitQuery(Relation storeObject)
    {
        List<T> result = new ArrayList();
        Connection session = null;
        
        try
        {
            ResultSet response;

            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            String query = storeObject instanceof ProductRelation? 
                           dmlSintax.formatSelect((ProductRelation)storeObject):
                           dmlSintax.formatSelect(storeObject);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            Statement request = dataSource.createQueryStatement(session);
            response = request.executeQuery(query);
            fillResult(response, query, result);           
            //response.close(); fill method closes the response
            request.close();
            //event.clear();

            //3.2.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), _tableName);
            state = result.size() > 0 ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCProducerOperationSet.class.getName());
        }

        return result;
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar y convertir en List el resultado de un
     * Query.
     *
     * @param storeObject
     * @param filter
     * @return Una lista de objetos ;<T>
     */
    public List<T> submitQuery(Relation storeObject, T filter)
    {
        List<T> result = new ArrayList();
        Connection session = null;
        DataSendToQueryEvent event = new DataSendToQueryEvent(_tableName, DataSendToQueryEvent.Type.RETRIEVE);

        try
        {
            ResultSet response;

            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            onSendValues(event, filter);

            if (event.size() > 0)
            {
                String query;

                query = storeObject instanceof ProductRelation?
                        dmlSintax.formatSelect((ProductRelation)storeObject, event):
                        dmlSintax.formatSelect(storeObject, event);

                //2.- Efectuamos nuestra operación.
                //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
                PreparedStatement request = dataSource.prepareQueryStatement(query, session);

                onConvertEntity(request, request.getParameterMetaData(), event);

                response = request.executeQuery();
                fillResult(response, query, result);           
                //response.close(); fill method closes the response
                request.close();
            }
            //event.clear();

            //3.2.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), _tableName);
            state = result.size() > 0 ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCProducerOperationSet.class.getName());
        }

        return result;
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param storeObject
     * @param filters
     * @return Un valor que representa el número de registros afectados.
     */
    public List<T> submitQuery(Relation storeObject, List<T> filters)
    {
        return submitQuery(storeObject, filters, 1);
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param storeObject
     * @param filters
     * @param packet_size
     * @return Un valor que representa el número de registros afectados.
     */
    public List<T> submitQuery(Relation storeObject, List<T> filters, int packet_size)
    {
        List<T> result = new ArrayList();
        Connection session = null;
        int batchSuccess = 0;
        
        Map<String, BatchOperationCache> cache = new HashMap<>();
        BatchOperationCache current;
        ResultSet response = null; 
        PreparedStatement request;
        DataSendToQueryEvent event = new DataSendToQueryEvent(_tableName, DataSendToQueryEvent.Type.RETRIEVE);
        String query;

        try
        {
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            for (T filter : filters)
            {
                onSendValues(event, filter);

                if (event.size() > 0)
                {
                    query = storeObject instanceof ProductRelation? 
                            dmlSintax.formatSelect((ProductRelation)storeObject, event):
                            dmlSintax.formatSelect(storeObject, event);
                    current = cache.get(query);

                    if (current == null)
                    {
                        request = dataSource.prepareQueryStatement(query, session);
                        
                        current = new BatchOperationCache(request);
                        cache.put(query, current);
                    }
                    else
                        request = current.getRequest();

                    onConvertEntity(request, request.getParameterMetaData(), event);

                    if (packet_size < 2)
                        response = request.executeQuery();

                    else
                    {
                        request.addBatch();
                        //event.clear();
                        current.increaseBatchCountby1();
                        
                        //2.2.1.- Ejecutamos una parcialidad.
                        if (current.getBatchCount() % packet_size == 0)
                        {
                            batchSuccess = calculateSuccessCount(dataSource.
                                                executeQueryBatch(request));
                            response = request.getResultSet();
                            current.resetBatchCount(); 
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
                    //event.clear();
                }
            }

            for (Map.Entry<String, BatchOperationCache> entry : cache.entrySet())
            {
                current = entry.getValue();
                request = current.getRequest();

                //2.3.- Comprobamos cualquier remanente por ejecutar.
                if (current.getBatchCount() > 0)
                {
                    batchSuccess = calculateSuccessCount(dataSource.executeQueryBatch(request));

                    do
                    {
                        response = request.getResultSet();
                        fillResult(response, entry.getKey(), result);
                        //response.close(); fill method closes the response
                    }
                    while (request.getMoreResults());                    
                }
                
                //3.- Liberamos los recursos utilizados
                //3.1.- Cerramos la petición empleada
                request.close();
            }
            cache.clear();
            //event.clear();

            //3.2.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            message = String.format(MSJ_RETRIEVE, result.size(), _tableName);
            state = result.size() > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCProducerOperationSet.class.getName());
        }

        return result;
    }
    
    //----------------------------------------------------------------
    /**
     * When the Query is previous Done, you only especify the parameteres.
     *
     * @param e
     * @param value
     * @throws java.sql.SQLException
     */
    protected void onSendValues(DataSendToQueryEvent e, T value) throws SQLException, UnsupportedOperationException
    {
        if (query_behaivor == null)
            throw new UnsupportedOperationException("Send Values, Not supported yet.");

        else
            query_behaivor.converTo(e, value);
    }
    
    //----------------------------------------------------------------
    private String createQuery(DataSendToQueryEvent event)
    {
        switch (event.getOperation())
        {
            case CREATE:
                return dmlSintax.formatInsert(event.getTableName(), event);

            case UPDATE:
                return dmlSintax.formatUpdate(event.getTableName(), event);

            case DELETE:
                return dmlSintax.formatDelete(event.getTableName(), event);
        }

        return "";
    }

    //----------------------------------------------------------------
    /**
     * Obtiene el generador de Sintaxis DML.
     *
     * @return
     */
    public IDMLGenerator getDmlSintax()
    {
        return dmlSintax;
    }

    //----------------------------------------------------------------
    /**
     * Establece el generador de Sintaxis DML.
     *
     * @param value
     */
    public void setDmlSintax(IDMLGenerator value)
    {
        this.dmlSintax = value;
    }

    //----------------------------------------------------------------
    public IQueryBehavior<T> getQuery_behaivor()
    {
        return query_behaivor;
    }

    //----------------------------------------------------------------
    public void setQuery_behaivor(IQueryBehavior<T> query_behaivor)
    {
        this.query_behaivor = query_behaivor;
    }

    //----------------------------------------------------------------
    public String getTable()
    {
        return _tableName;
    }
    
    
    
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param kindQuery
     * @param value Los valores que deben ser introducidos
     * @return Un valor que representa el número de registros afectados.
     */
    /*public int submitUpdateGetAutoKey(DataSendToQueryEvent.Type kindQuery,
                                      T value)
    {
        return submitUpdateGetAutoKey(kindQuery, value, false);
    }*/

    //----------------------------------------------------------------
    /**
     * GENERA y EJECUTA un QUERY en la session indicada. Este método utiliza un
     * preparedStatement por lo que evita la injection SQL.
     *
     * @param kindQuery
     * @param value Los valores que deben ser introducidos
     * @param syncTransaction
     * @return Un valor que representa el número de registros afectados.
     */
    /*public int submitUpdateGetAutoKey(DataSendToQueryEvent.Type kindQuery,
                                      T value, boolean syncTransaction)
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
            DataSendToQueryEvent event = new DataSendToQueryEvent(table, kindQuery);
            onSendValues(event, value);

            if (event.size() > 0)
            {
                String query;

                query = createQuery(event);

                //2.1.- Si se solicito Antes la preparación de una operación externa.
                if (TypeableBeforeUpdateBehavior != null)
                    TypeableBeforeUpdateBehavior.execute(new ActionStateEvent<T>( session, table, value));

                //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
                PreparedStatement request = dataSource.prepareStatement(query, session, true);

                //2.3.- Se solicita el establecimiento de parametros.
                onConvertEntity(request, request.getParameterMetaData(), event);
                affected = request.executeUpdate();

                //2.4.- Buscamos las llaves generadas
                ResultSet response = request.getGeneratedKeys();
                if (response.next())
                    affected = response.getInt(1);
                response.close();

                //2.5.- Si se solicito Después la preparació de una operación externa.
                if (TypeableAfterUpdateBehavior != null)
                    TypeableAfterUpdateBehavior.execute(new ActionStateEvent<T>( session, table, value, affected));

                //3.- Liberamos los recursos utilizados.
                //3.1- Liberamos la petición utilizada
                request.close();

                //3.- Liberamos la conexión con el servidor
                dataSource.disconnect(session);

                //4.- Generamos una respuesta.
                message = String.format(MSJ_AFECTED, affected, table);
                state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
            }

        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, ProducerDataAccess.class.getName());
        }

        return affected;
    }*/
}