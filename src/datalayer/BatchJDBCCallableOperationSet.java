package datalayer;

import datalayer.api.IMediatorDataSource;
import datalayer.api.IProcedurePrepareBehavior;
import datalayer.events.ActionStateEvent;
import datalayer.events.ConnectionStateEvent;
import datalayer.events.DataGetEvent;
import datalayer.events.DataSetEvent;
import datalayer.events.ParameteRegisterEvent;
import datalayer.generators.CommonDCLGenerator;
import datalayer.api.IDCLGenerator;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Proporciona algoritmos basicos para recuperación y actualización de datos,
 * atraves de procedimientos almacenados.
 * @author Victor Manuel Bucio Vargas
 * @param <T> Tipo de objeto a utilizar
 */
public abstract class BatchJDBCCallableOperationSet<T> extends BatchJDBCOperationSet<T>
{
    private IProcedurePrepareBehavior procedure_behaivor;
    protected IDCLGenerator dclSintax;
    private boolean generate_callSyntax;
    
    //----------------------------------------------------------------
    public BatchJDBCCallableOperationSet(IMediatorDataSource mysource)
    {
        super(mysource);
        dclSintax = new CommonDCLGenerator();
        generate_callSyntax = true;
    }   
    
    //----------------------------------------------------------------
    public BatchJDBCCallableOperationSet(IMediatorDataSource mysource, String[] idColumnsNames )
    {
        super(mysource, idColumnsNames);
        dclSintax = new CommonDCLGenerator();
        generate_callSyntax = true;
    }
    
    //----------------------------------------------------------------
    public BatchJDBCCallableOperationSet(IMediatorDataSource mysource, int[] idColumnsIndexes )
    {
        super(mysource, idColumnsIndexes);
        dclSintax = new CommonDCLGenerator();
        generate_callSyntax = true;
    }
    
    //--------------------------------------------------------------------
    /**
     * Obsoleto: Porque no se requiere un algoritmo especifico, se puede
     * reutilizar otro existente y solo utilizar la firma del método como
     * una variante del método que ya existe.
     * Ejecuta un procedimiento almacenado .
     *
     * @param storeProcedureName Nombre del procedimiento que será ejecutado.
     * @return indica si fué correcta la operación
     */
    /*final protected int executeStoredProcedure(String storeProcedureName)
    {
        Connection session = null;
        int affected = -1;

        try
        {
            //1.- Conectar
            //1.1- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.2.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            CallableStatement request
                    = dataSource.prepareCall(storeProcedureName, session);

            //2.2.- Evaluamos la existencia de algún resultado, diferente a consulta.
            if (!request.execute())
                affected = request.getUpdateCount();

            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_OPERATION_FINISH, storeProcedureName);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            reportErrorAndWriteLog(session, ex);
        }

        return affected;
    }*/
    
        //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @return indica si fué correcta la operación
     */
    final protected int executeStoredProcedure(String callSintax)
    {
        return executeStoredProcedure(callSintax, null, (Object[])null);
    }
    
        //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param recollector
     * @return indica si fué correcta la operación
     */
    final protected int executeStoredProcedure(String callSintax, Map<String, Object> recollector)
    {
        return executeStoredProcedure(callSintax, recollector, (Object[])null);
    }

    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return indica si fué correcta la operación
     */
    final protected int executeStoredProcedure(String callSintax, Object... values)
    {
        return executeStoredProcedure(callSintax, (Map<String, Object>)null, values);
    }
    
    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado suministrando los valores proporcionados.
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param recollector
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * @return
     */
    final protected int executeStoredProcedure(String callSintax,
                                           Map<String, Object> recollector,
                                           Object... values)
    {
        Connection session = null;
        int affected = -1;

        try
        {
            //1.- Conectar
            //1.1- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.2.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            affected = doStoredProcedure (session, callSintax, recollector, values);
            
            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_OPERATION_FINISH, callSintax);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }

        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCCallableOperationSet.class.getName() );
        }

        return affected;
    }
    
    //--------------------------------------------------------------------
    /*
    * Ejecuta el procedimiento almacenado.
    * @param callSintax Nombre del procedimiento que será ejecutado.
    * @param values Especified the IN parameters ONLY, you can't use OUT
    */    
    protected int doStoredProcedure (Connection session,
                                     String callSintax,
                                     Map<String, Object> recollector,
                                     Object... values) throws SQLException
    {
        int affected = 0;
        ParameterMetaData parameterInfo = null;

        //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
        CallableStatement request
                = dataSource.prepareCall(generate_callSyntax?dclSintax.formatCallProcedure(callSintax): 
                                         callSintax, session);

        parameterInfo = request.getParameterMetaData();

        if (recollector != null)
            onPrepareProcedure(new ParameteRegisterEvent(callSintax,
                                                         request,
                                                         parameterInfo));
        if (values != null)
            autoSetParameters(request, values);

        //2.2.- Evaluamos la existencia de algún resultado, diferente a consulta.
        if (!request.execute())
            affected = request.getUpdateCount();
        //TODO:What should we do when there's a ResultSet?

        //2.3.- Add the Out Put parameters results
        if (recollector != null)
            fillOutParameterHolder(recollector, request, parameterInfo);

        request.close();

        return affected;
    }
        
    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param recollector
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(String callSintax,
                                                       Map<String, Object> recollector,
                                                       Object... values)
    {
        Connection session = null;
        List<T> result = new ArrayList<>();

        try
        {
            //1.- Conectar
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.2.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            result = doReadStoredProcedure(session, callSintax, recollector, result, values);

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), callSintax);
            state = result.size() > 0 ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCCallableOperationSet.class.getName());
        }

        return result;
    }
    
    //--------------------------------------------------------------------
    /*
    * Ejecuta el procedimiento almacenado.
    */    
    protected List<T> doReadStoredProcedure (Connection session,
                                     String callSintax,
                                     Map<String, Object> recollector,
                                     List<T> result,
                                     Object... values) throws SQLException
    {
        ParameterMetaData parameterInfo;

        //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
        CallableStatement request
                = dataSource.prepareQueryCall(callSintax, session);

        parameterInfo = request.getParameterMetaData();

        if (recollector != null)
            onPrepareProcedure(new ParameteRegisterEvent(callSintax,
                                                         request,
                                                         parameterInfo));
        if (values != null)
            autoSetParameters(request, values);

        //2.2.-Efectuamos operación y Evaluamos la existencia de algún resultado.
        if (request.execute())
            fillResultTraversingMultipleResultSet(request, eschema, result);

        //2.3.- Add the Out Put parameters results
        if (recollector != null)
            fillOutParameterHolder(recollector, request, parameterInfo);

        request.close();
        
        return result;
    }
        
    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(String callSintax)
    {
        return executeReadStoredProcedure(callSintax, null, (Object[]) null);
    }

    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(String callSintax, Object... values)
    {
        return executeReadStoredProcedure(callSintax, null, values);
    }

    //--------------------------------------------------------------------
    /**
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param recollector
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * @param anyResultSetIsAutoID
     * @return
     */
    final protected int executeUpdateStoredProcedure(String callSintax,
                                                     Map<String, Object> recollector,
                                                     T values,
                                                     boolean anyResultSetIsAutoID)
    {
        Connection session = null;
        int affected = -1;

        try
        {
            //1.- Conectar
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.2.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            affected = doUpdateStoredProcedure(session, callSintax, 
                                               recollector, values, 
                                               anyResultSetIsAutoID);
            
            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_OPERATION_FINISH, callSintax);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCCallableOperationSet.class.getName());
        }

        return affected;
    }
    
    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return indica si fué correcta la operación
     */
    final protected int executeUpdateStoredProcedure(String callSintax, T values)
    {
        return executeUpdateStoredProcedure(callSintax, null, values, false);
    }
    
    //--------------------------------------------------------------------
    /**
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param recollector
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * @return
     */
    final protected int executeUpdateStoredProcedure(String callSintax,
                                                     Map<String, Object> recollector,
                                                     T values)
    {
        return executeUpdateStoredProcedure(callSintax, recollector, values, false);
    }
    
        //--------------------------------------------------------------------
    /**
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * @param anyResultSetIsAutoID
     * @return
     */
    final protected int executeUpdateStoredProcedure(String callSintax,
                                                     T values,
                                                     boolean anyResultSetIsAutoID )
    {
        return executeUpdateStoredProcedure(callSintax, null, values, anyResultSetIsAutoID);
    }
    
    //--------------------------------------------------------------------
    /**
     * 
     * @param session
     * @param callSintax
     * @param recollector
     * @param values
     * @param anyResultSetIsAutoID
     * @return 
     * @throws java.sql.SQLException 
     */
    public int doUpdateStoredProcedure (Connection session, 
                                        String callSintax,
                                        Map<String, Object> recollector,
                                        T values, 
                                        boolean anyResultSetIsAutoID) throws SQLException
    {
        ParameterMetaData parameterInfo = null;
        int affected = -1;

        //2.1.- Si se solicito Antes la preparción de una operación externa.
        if(beforeUpdateBehavior != null)
            beforeUpdateBehavior.execute(new ConnectionStateEvent(session, callSintax));
            
        //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
        CallableStatement request
            = dataSource.prepareCall(generate_callSyntax?dclSintax.formatCallProcedure(callSintax): 
                                     callSintax, session);

        parameterInfo = request.getParameterMetaData();

        if (recollector != null)
            onPrepareProcedure(new ParameteRegisterEvent(callSintax,
                                                     request,
                                                     parameterInfo));

        if (values != null)
            onConvertTransfer(values, new DataSetEvent(callSintax,
                                             request,
                                             parameterInfo));

        //2.3.- Evaluamos la existencia de algún resultado que no sea conjunto.
        //affected = request.executeUpdate(); //Never returns a value distinct to -1
        // If there's no a resultSet even when the user asked for collect AutoID
        if (!request.execute())
            affected = request.getUpdateCount();
        else if (anyResultSetIsAutoID)
            affected = 1;
        
        //2.3.1- Collect any ResultSet as AutoID
        if (anyResultSetIsAutoID)
            fillValueWithAutoIDTraversingMultipleResultSet(session, request, callSintax, values);
        
        else if (null != navigationPropertyUpdateBehavior )
            navigationPropertyUpdateBehavior.execute( new ActionStateEvent<>(session, callSintax, values));
        
        //2.4.- Add the Out Put parameters results
        if (recollector != null)
            fillOutParameterHolder(recollector, request, parameterInfo);

        //2.5.- Liberamos los recursos utilizados
        request.close();
        
        //2.6.- Si se solicito después la preparación de una operación externa.
         if (afterUpdateBehavior != null)
            afterUpdateBehavior.execute(new ConnectionStateEvent(session, callSintax, affected));

        return affected;
    }

    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @param isAtomic All values must be correct for commit Transaction.
     * @param packet_size
     * @param anyResultSetIsAutoID Indica si la existencia de un ResultSet tras 
     * la ejecución del procedimiento será tomado como la llave primaria.
     * Nota: Algunos DBMS no soportan acumular ResultSet que no provengan del
     * mismo llamado, por lo que procura hacer cada llamado de forma independiente
     * mediante el uso de packet size.
     * @return Una lista de objetos ;<T>
     */
    final protected int executeUpdateStoredProcedure(String callSintax, List<T> values, 
                                                     boolean isAtomic, int packet_size,
                                                     boolean anyResultSetIsAutoID)
    {
        int affected = -1, countAffected = 0, batchCount = 0, start = 0;
        Connection session = null;

        try
        {
            //1.- Conectar
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.2.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //1.3.- Iniciamos la transacción.
            session.setAutoCommit(!isAtomic);

            //2.- Efectuamos nuestra operación.
            //2.1.- Si se solicito Antes la preparación de una operación externa.
            if(beforeUpdateBehavior != null)
                beforeUpdateBehavior.execute(new ConnectionStateEvent(session, callSintax));
        
            //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            CallableStatement request = dataSource.prepareCall(generate_callSyntax?dclSintax.formatCallProcedure(callSintax): 
                                         callSintax, session);

            for (int i = 0; i < values.size(); i++)
            {
                onConvertTransfer(values.get(i), new DataSetEvent(callSintax, request, 
                        request.getParameterMetaData()));
                
                //2.2.1-Efectuamos operación y Evaluamos la existencia de algún resultado.
                if (packet_size < 2)
                {
                    //countAffected += request.executeUpdate();
                    if (!request.execute())
                        countAffected += request.getUpdateCount();
                    else if (anyResultSetIsAutoID)
                        countAffected++;
                    
                    if (anyResultSetIsAutoID)
                        fillValueWithAutoIDTraversingMultipleResultSet(session, request, callSintax, values.get(i));
                    
                    else if (null != navigationPropertyUpdateBehavior )
                        navigationPropertyUpdateBehavior.execute( new ActionStateEvent<>(session, callSintax, values.get(i)));
                }
               
                else
                {
                    request.addBatch();
                    batchCount++;

                    if (batchCount % packet_size == 0)
                    {
                        countAffected += calculateAffectedCount(request.executeBatch());
                        start += batchCount;
                        batchCount = 0;
                        if (anyResultSetIsAutoID)
                            fillValuesWithAutoIDTraversingMultipleResultSet(session, request, callSintax, values, start);
                        
                        else if (null != navigationPropertyUpdateBehavior )
                            synchronizeNavigationValues (session, callSintax, values, start, batchCount);
                    }
                }
            }
            
            //2.3.- Ejecutamos la operación.
            if (batchCount > 0)
            {
                countAffected += calculateAffectedCount(request.executeBatch());            
                if (anyResultSetIsAutoID)
                    fillValuesWithAutoIDTraversingMultipleResultSet(session, request, callSintax, values, start);
                
                else if (null != navigationPropertyUpdateBehavior )
                    synchronizeNavigationValues (session, callSintax, values, start, batchCount);
            }
                     
            //2.4.- Liberamos los recursos utilizados
            request.close();
            
            //2.5.- Si se solicito Después la preparación de una operación externa.
            if (afterUpdateBehavior != null)
                afterUpdateBehavior.execute(new ConnectionStateEvent(session, callSintax, affected));

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            affected = countAffected;
            message = String.format(MSJ_RETRIEVE, affected, callSintax);
            state = affected > 0 ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCCallableOperationSet.class.getName());
        }
       
        return affected;
    }
    
    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return Una lista de objetos ;<T>
     */
    final protected int executeUpdateStoredProcedure(String callSintax, List<T> values)
    {
        return executeUpdateStoredProcedure(callSintax, values, true, DEFAULT_BATCH, false);
    }
    
        //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @param isAtomic All values must be correct for commit Transaction.
     * @return Una lista de objetos ;<T>
     */
    final protected int executeUpdateStoredProcedure(String callSintax, List<T> values, 
                                                     boolean isAtomic)

    {
        return executeUpdateStoredProcedure(callSintax, values, isAtomic, DEFAULT_BATCH, false);
    }
    
        //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @param isAtomic All values must be correct for commit Transaction.
     * @param packet_size
     * @return Una lista de objetos ;<T>
     */
    final protected int executeUpdateStoredProcedure(String callSintax, List<T> values, 
                                                     boolean isAtomic, int packet_size)
    {
        return executeUpdateStoredProcedure(callSintax, values, isAtomic, packet_size, false);
    }

    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param recollector
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(String callSintax,
                                                       Map<String, Object> recollector,
                                                       T values)
    {
        Connection session = null;
        ParameterMetaData parameterInfo;
        List<T> result = new ArrayList<>();

        try
        {
            //1.- Conectar
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.2.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            CallableStatement request
                    = dataSource.prepareQueryCall(callSintax, session);

            parameterInfo = request.getParameterMetaData();

            if (recollector != null)
                onPrepareProcedure(new ParameteRegisterEvent(callSintax,
                                                             request,
                                                             parameterInfo));

            if (values != null)
                onConvertTransfer(values, new DataSetEvent(callSintax,
                                                     request,
                                                     parameterInfo));

            //2.2.-Efectuamos operación y Evaluamos la existencia de algún resultado.
            if (request.execute())
                fillResultTraversingMultipleResultSet(request, callSintax, result);

            //2.3.- Add the Out Put parameters results
            if (recollector != null)
                fillOutParameterHolder(recollector, request, parameterInfo);

            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), callSintax);
            state = result.size() > 0 ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCCallableOperationSet.class.getName());
        }

        return result;
    }
    
    
    
    
    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(String callSintax, T values)
    {
        return executeReadStoredProcedure(callSintax, null, values);
    }

    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @param isAtomic All values must be correct for commit Transaction.
     * @param packet_size
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(String callSintax, 
                                                       List<T> values, 
                                                       boolean isAtomic, 
                                                       int packet_size)
    {
        int batchCount = 0, batchSuccess = 0;
        ResultSet response = null;
        Connection session = null;
        List<T> result = new ArrayList<>();

        try
        {
            //1.- Conectar
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //1.2.- Iniciamos la transacción.
            session.setAutoCommit(!isAtomic);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            CallableStatement request = dataSource.prepareQueryCall(callSintax, session);
            for (int i = 0; i < values.size(); i++)
            {
                onConvertTransfer(values.get(i), new DataSetEvent(callSintax, 
                        request, request.getParameterMetaData()));

                //2.2.-Efectuamos operación y Evaluamos la existencia de algún resultado.
                if (packet_size < 2)
                    response = request.executeQuery();
               
                else
                {
                    request.addBatch();
                    batchCount++;
   
                    //2.2.1.- Ejecutamos una parcialidad.
                    if (batchCount % packet_size == 0)
                    {
                        batchSuccess += calculateSuccessCount(dataSource.executeQueryBatch(request));
                        response = request.getResultSet();
                        batchCount = 0;
                    }
                }

                //2.2.-Efectuamos operación y Evaluamos la existencia de algún resultado.
                if (null != response)
                {
                    do
                    {
                        if (null == response)
                            response = request.getResultSet();
                        fillResult(response, callSintax, result);
                        response = null;
                    }
                    while (request.getMoreResults());
                }

            }
            //2.3.- Comprobamos cualquier remanente por ejecutar.
            if (batchCount > 0)
            {
                batchSuccess += calculateSuccessCount(dataSource.executeQueryBatch(request));
                
                do
                {
                    response = request.getResultSet();
                    fillResult(response, callSintax, result);
                }
                while (request.getMoreResults());
            }

            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), callSintax);
            state = result.size() > 0 ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCCallableOperationSet.class.getName());
        }

        return result;
    }

    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(String callSintax, List<T> values)
    {
        return executeReadStoredProcedure(callSintax, values, true, 1);
    }
    
    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param callSintax Nombre del procedimiento que será ejecutado.
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @param isAtomic All values must be correct for commit Transaction.
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(String callSintax, List<T> values, 
                                                   boolean isAtomic)
    {
        return executeReadStoredProcedure(callSintax, values, isAtomic, 1);
    }   
    
        //--------------------------------------------------------------------
    /**
     * Ejecuta una función y nos devuelve el resultado de la misma.
     *
     * @param callSintax
     * @param returnType You should use java.sql.Types
     * @param values
     * @return
     */
    protected Object executeFunction(String callSintax,
                                     int returnType,
                                     Object... values)
    {
        Object result = null;
        Connection session = null;
        ParameterMetaData parameterInfo;

        try
        {
            //1.- Conectar
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //1.2.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            CallableStatement request
                    = dataSource.prepareCall(generate_callSyntax?dclSintax.formatCallProcedure(callSintax): 
                                         callSintax, session);

            parameterInfo = request.getParameterMetaData();

            //2.- Realizar operacion         
            onPrepareProcedure(new ParameteRegisterEvent(callSintax,
                                                             request,
                                                             parameterInfo));   
            //2.1.- Configure the return type.
            request.registerOutParameter(1, returnType);
                
            //2.2.- Autoset Parameters.
            if (values != null)
                autoSetParameters(request, values);
            
            //2.3.- Execute and retrieve the returned values.
            if (!request.execute())
                result = request.getObject(1);

            //2.4.- This method doesn't support a ResultSet as return type.
            if (result instanceof ResultSet)
            {
                ResultSet rows = (ResultSet) result;
                rows.close();
                result = null;
            }

            //3.- Desconectar
            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format("%s %s", MSJ_OPERATION_FINISH, callSintax);
            state = result != null ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCCallableOperationSet.class.getName());
        }
        return result;
    }
        
    //--------------------------------------------------------------------
    private void autoSetParameters(CallableStatement request, Object[] values)throws SQLException
    {
        for (int i = 0; i < values.length; i++)
            autoSetParameter(request, i + 1, values[i]);
    }
    
    //--------------------------------------------------------------------
    private void autoSetParameter(CallableStatement request, int parameter, Object value) throws SQLException
    {
        if (value.getClass() == Integer.class)
            request.setInt(parameter, (int) value);

        else if (value.getClass() == String.class)
            request.setString(parameter, (String) value);

        else if (value.getClass() == java.util.Date.class)
            request.setDate(parameter, new Date(((java.util.Date) value).getTime()));

        else if (value.getClass() == byte[].class)
            request.setBytes(parameter, (byte[]) value);

        else if (value.getClass() == Double.class)
            request.setDouble(parameter, (Double) value);

        else if (value.getClass() == Float.class)
            request.setFloat(parameter, (Float) value);

        else if (value.getClass() == Boolean.class)
            request.setBoolean(parameter, (Boolean) value);

        else if (value.getClass() == Short.class)
            request.setShort(parameter, (Short) value);
    }
    
    //--------------------------------------------------------------------
    private void fillValueWithAutoIDTraversingMultipleResultSet(Connection session, CallableStatement request, String query, T value) throws SQLException
    {
        ResultSet response;
        do
        {
            response = request.getResultSet();

            if (response!= null)
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
        }while (request.getMoreResults());
    }
    
    //--------------------------------------------------------------------
    private void fillValuesWithAutoIDTraversingMultipleResultSet(Connection session, CallableStatement request, String query, List<T> values, int start) throws SQLException
    {
        ResultSet response;

        do
        {
            response = request.getResultSet();
            
            if (response != null)
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
        }while (request.getMoreResults());
    }

    //--------------------------------------------------------------------    
    final protected void fillResultTraversingMultipleResultSet(CallableStatement request, String query, List<T> result) throws SQLException
    {
        ResultSet response;
        do
        {
            response = request.getResultSet();
            fillResult(response, query, result);
        }
        while (request.getMoreResults());
    }
    
    //--------------------------------------------------------------------
    private void fillOutParameterHolder (Map<String, Object> recollector, 
                                         CallableStatement values,
                                         ParameterMetaData parameterInfo
                                         ) throws SQLException{
        for (int i = 1; i <= parameterInfo.getParameterCount(); i++)
        {
            if (parameterInfo.getParameterMode(i) == ParameterMetaData.parameterModeOut)
                recollector.put(String.valueOf(i), values.getObject(i));
        }
    }
    
    //----------------------------------------------------------------
    /**
     * When the Query is previous Done, you only especify the parameteres.
     *
     * @param e
     * @throws java.sql.SQLException
     */
    protected void onPrepareProcedure(ParameteRegisterEvent e) throws SQLException, UnsupportedOperationException
    {
        if (procedure_behaivor == null)
            throw new UnsupportedOperationException("Procedure Parameter Register Not supported yet.");

        else
            procedure_behaivor.configureTo(e);
    }
    
    //----------------------------------------------------------------
    /**
     * Obtiene el proveedor de comportamiento de conversión para los llamados a
     * procedimientos almacenados.
     *
     * @return
     */
    public IProcedurePrepareBehavior getProcedure_behaivor()
    {
        return procedure_behaivor;
    }

    //----------------------------------------------------------------
    /**
     * Establece el proveedor de comportamiento de conversión para los llamados
     * a procedimientos almacenados
     *
     * @param value
     */
    public void setProcedure_behaivor(IProcedurePrepareBehavior value)
    {
        this.procedure_behaivor = value;
    }
    
    //----------------------------------------------------------------
    /**
     * Obtiene el generador de Sintaxis DCL.
     *
     * @return
     */
    public IDCLGenerator getDclSintax()
    {
        return dclSintax;
    }

    //----------------------------------------------------------------
    /**
     * Establece el generador de Sintaxis DCL.
     *
     * @param value
     */
    public void setDclSintax(IDCLGenerator value)
    {
        this.dclSintax = value;
    }

     //----------------------------------------------------------------
    /**
     * Indica si la call syntax se genera automaticamente.
     *
     * @return
     */
    public boolean isGenerate_callSyntax()
    {
        return generate_callSyntax;
    }

    //----------------------------------------------------------------
    /**
     * Establece si la call syntax se genera automaticamente.
     *
     * @param value
     */
    public void setGenerate_callSyntax(boolean generate_callSyntax)
    {
        this.generate_callSyntax = generate_callSyntax;
    }
}