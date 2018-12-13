package datalayer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import datalayer.api.IMediatorDataSource;
import datalayer.events.ConnectionStateEvent;
import java.util.Arrays;
import datalayer.api.IBasicTransactionSynchronizeBehavior;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;

/**
 * Proporciona algoritmos basicos para operaciones de actualización de datos para
 * una sentencia a la vez.
 *
 * @author Victor Manuel Bucio Vargas
 */
public abstract class BasicJDBCUpdateSet extends DataSourceOperationSet
{
    protected String[] _idColumnsNames;
    protected int[] _idColumnsIndexes;
    
    protected IBasicTransactionSynchronizeBehavior beforeUpdateBehavior;
    //private IBasicTransactionSynchronizeBehavior onUpdateBehavior;
    protected IBasicTransactionSynchronizeBehavior afterUpdateBehavior;
    
    //----------------------------------------------------------------
    /**
     * Crea una instancia asociada con la fuente de datos especificada.
     * @param mysource fuente de datos.
     */
    public BasicJDBCUpdateSet(IMediatorDataSource mysource)
    {
        super(mysource);
    }
    
     //----------------------------------------------------------------
    /**
     * Crea una instancia asociada con la fuente de datos especificada, además
     * permite especificar los nombres de las columnas consideradas como la 
     * llave primaria para operaciones de auto generacion de ID.
     * @param mysource fuente de datos.
     * @param idColumnsNames Nombres de las columnas involucradas en la PK.
     */
    public BasicJDBCUpdateSet(IMediatorDataSource mysource, String[] idColumnsNames )
    {
        super(mysource);
        _idColumnsNames = idColumnsNames;
    }
    
    //----------------------------------------------------------------
    /**
     * Crea una instancia asociada con la fuente de datos especificada, además
     * permite especificar los indices de las columnas consideradas como la 
     * llave primaria para operaciones de auto generacion de ID.
     * @param mysource fuente de datos.
     * @param idColumnsIndexes indicds de las columnas involucradas en la PK.
     */
    public BasicJDBCUpdateSet(IMediatorDataSource mysource, int[] idColumnsIndexes )
    {
        super(mysource);
        _idColumnsIndexes = idColumnsIndexes;
    }
    
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query.
     *
     * @param query El query a ejecutar
     * @param keyHolder Contenedor de los valores resultantes.
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String query, Map<String, Object> keyHolder)
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
            
            //2.- Realizar la operación
            affected = doUpdate (session, query, keyHolder);
            
            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_AFECTED, affected, query);
            state = affected > ZERO ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BasicJDBCUpdateSet.class.getName() );
        }

        return affected;
    }

     //----------------------------------------------------------------
    /**
     * Solo ejecuta la sentecia proporcionada en la session indicada.
     * @param query
     * @return 
     */
    public int submitUpdate(String query)
    {
        return submitUpdate (query, null);
    }

    //----------------------------------------------------------------
    /**
     * Solo ejecuta la sentecia proporcionada en la session indicada.
     * @param session
     * @param query
     * @param keyHolder
     * @return 
     * @throws java.sql.SQLException 
     */
    public int doUpdate(Connection session, String query, Map<String, Object> keyHolder) throws SQLException
    {
        int affected;
       
        //2.- Efectuamos nuestra operación.
        //2.1.- Si se solicito Antes la preparación de una operación externa.
        if(beforeUpdateBehavior != null)
            beforeUpdateBehavior.execute(new ConnectionStateEvent(session, query));

        //2.2.- Solicitamos los recursos necesarios para efectuar nuestra operación.
        Statement request = dataSource.createStatement(session);

        //2.3.- Enviamos petición al servidor.
        //When a KeyHolder is passed as argument we need to specify
        //how execute the statement in order to retrieve autoID values from de DBMS.
        //It can be either passing idColumnsNames, idColumnsIndex or just getAutoGenerateKeys flag.
        if (keyHolder != null)
        {
            affected = howExecuteForAutoID(request, query);
            fillKeyHolder(request.getGeneratedKeys(), query, keyHolder, 0);
        }
        
        else
            affected = request.executeUpdate(query);
            
        //2.4.- Liberamos los recursos utilizados
        request.close();
        
        //2.5.- Si se solicitó Después la preparación de una operación externa.
        if (afterUpdateBehavior != null)
            afterUpdateBehavior.execute(new ConnectionStateEvent(session, query, affected));
            
        return affected;
    }

    //----------------------------------------------------------------
    /**
     * Solo ejecuta la sentecia proporcionada en la session indicada.
     * @param session
     * @param query
     * @return 
     * @throws java.sql.SQLException 
     */    
    public int doUpdate(Connection session, String query) throws SQLException
    {
        return doUpdate(session, query, null);
    }

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query.
     *
     * @param querys Los querys que se quieren ejecutar
     * @param packetSize
     * @param keyHolder
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String[] querys, int packetSize,
                         Map<String, Object> keyHolder)
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
            affected = doUpdate (session, querys, packetSize, keyHolder);
            
            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_AFECTED, affected, Arrays.toString(querys));
            state = affected > ZERO ? SUCCESS_OPERATION : NOT_AFFECTED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BasicJDBCUpdateSet.class.getName() );
        }

        return affected;
    }
    
        //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query.
     *
     * @param querys Los querys que se quieren ejecutar
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String... querys)
    {
        return submitUpdate(querys, DEFAULT_BATCH, null);
    }
    
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar el query.
     *
     * @param querys Los querys que se quieren ejecutar
     * @param packetSize
     * @return Un valor que representa el número de registros afectados.
     */
    public int submitUpdate(String[] querys, int packetSize)
    {
        return submitUpdate(querys, packetSize, null);
    }
    
    //----------------------------------------------------------------
    /**
     * Solo ejecuta las sentecias proporcionadas en la session indicada.
     * @param session
     * @param queries Los querys que se quieren ejecutar
     * @param packetSize
     * @param keyHolder
     * @return Un valor que representa el número de registros afectados.
     * @throws java.sql.SQLException
     */
    public int doUpdate (Connection session, 
                         String[] queries, 
                         int packetSize,
                         Map<String, Object> keyHolder) throws SQLException
    {
        int batchCount = ZERO, batchAffected = ZERO, start = 0;
        String allqueries = Arrays.toString(queries);
        //StringBuilder acceptedQuery = new StringBuilder();
        
        //2.2.1.- Se se solicito Antes la preparación de una operación externa.
        if(beforeUpdateBehavior != null)
            beforeUpdateBehavior.execute(new ConnectionStateEvent(session, allqueries));
                
        //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
        Statement request = dataSource.createStatement(session);

        //2.2.- Construimos la petición que será enviada al servidor.
        for (String query : queries)
        {            
            request.addBatch(query);
            /*if (acceptedQuery.length() > 0);
                acceptedQuery.append("; ");
            acceptedQuery.append(query);*/
            batchCount++;

            //2.3.1- Enviamos una parcialidad al servidor.
            if (batchCount % packetSize == ZERO)
            {
                batchAffected += calculateAffectedCount(request.executeBatch());
                if (null!= keyHolder)
                    fillKeyHolder(request.getGeneratedKeys(), queries, keyHolder, start);
                start += batchCount;
                batchCount = ZERO;                    
                //acceptedQuery.delete(0, acceptedQuery.length());
            }
        }

        //2.4.- Enviamos el restante como petición al servidor.
        if (batchCount > ZERO)
        {
            batchAffected += calculateAffectedCount(request.executeBatch());
            if (null!= keyHolder)
              fillKeyHolder(request.getGeneratedKeys(), queries, keyHolder, start);
        }
        
        //2.5.- Liberamos los recursos utilizados
        request.close();
        
        //2.6.- Si se solicito Después la preparación de una operación externa.
        if (afterUpdateBehavior!= null)
            afterUpdateBehavior.execute(new ConnectionStateEvent(session, allqueries));
            
        return batchAffected;
    }
    
    //----------------------------------------------------------------   
    /**
     * This factory method calculate how to execute a statment based on 
     * which information we know about the primary key.
     */
    private int howExecuteForAutoID(Statement request, String query) throws SQLException
    {
        int affected;
        
        if (_idColumnsNames != null)
            affected = request.executeUpdate(query, _idColumnsNames);
        
        else if (_idColumnsIndexes != null)
            affected = request.executeUpdate(query, _idColumnsIndexes);
        
        else
            affected = request.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        
        return affected;
    }
    
    //----------------------------------------------------------------
    /**
     * LLena el keyHolder acorde a los valores contenidos en el ResultSet.
     * En caso de haber multiples columnas y renglones estos se numeran como
     * Key = Colunmna + Renglon
     */
    private void fillKeyHolder(ResultSet response, String query, Map<String, Object> keyHolder, int row) throws SQLException, UnsupportedOperationException
    {
        ResultSetMetaData e = response.getMetaData();
               
        //2.3.- Recorremos el resultado obtenido.
        while (response.next())
        {
            keyHolder.put("QUERY" + row, query);

            for (int i = 1; i <= e.getColumnCount(); i++)
               keyHolder.put(e.getColumnName(i) + row, response.getObject(i));
            row++;
        }
        
        //2.4.- Cerramos los recursos utilizados
        response.close();
    }
    
        //----------------------------------------------------------------
    /**
     * LLena el keyHolder acorde a los valores contenidos en el ResultSet.
     * En caso de haber multiples columnas y renglones estos se numeran como
     * Key = Colunmna + Renglon
     */
    private void fillKeyHolder(ResultSet response, String[] queries, Map<String, Object> keyHolder, int row) throws SQLException, UnsupportedOperationException
    {
        ResultSetMetaData e = response.getMetaData();
                
        //2.3.- Recorremos el resultado obtenido.
        while (response.next())
        {
            keyHolder.put("QUERY" + row, queries[row]);
            
            for (int i = 1; i <= e.getColumnCount(); i++)
               keyHolder.put(e.getColumnName(i) + row, response.getObject(i));
            row++;
        }
        
        //2.4.- Cerramos los recursos utilizados
        response.close();
    }


    //----------------------------------------------------------------
    /**
     * Obtiene información sobre la llave primaria utilizada.
     *
     * @return
     */
    public String[] getIdColumnsNames()
    {
        return _idColumnsNames;
    }

    //----------------------------------------------------------------
    /**
     * Establece información sobre la llave primaria utilizada para efectuar 
     * las operaciones que involucren recuperar campos de auto incremento.
     * Nota: Este método es excluyente a setIdColumnsIndexes
     * @param values Indices de la llave primaria.
     */
    public void setIdColumnsNames(String[] values)
    {
        if (values == null)
            throw new IllegalArgumentException("Not is possible pass a null IdColumnsNames");
        
        if (_idColumnsIndexes!= null)
            _idColumnsIndexes = null;
          
        this._idColumnsNames = values;
    }

    //----------------------------------------------------------------
    /**
     * Obtiene información sobre la llave primaria utilizada.
     *
     * @return
     */
    public int[] getIdColumnsIndexes()
    {
        return _idColumnsIndexes;
    }

    //----------------------------------------------------------------
    /**
     * Establece información sobre la llave primaria utilizada para efectuar 
     * las operaciones que involucren recuperar campos de auto incremento.
     * Nota: Este método es excluyente a setIdColumnsNames
     * @param values Indices de la llave primaria.
     */
    public void setIdColumnsIndexes(int[] values)
    {
        if (values == null)
            throw new IllegalArgumentException("Not is possible pass a null idColumnsIndexes");
        
        if (_idColumnsNames!= null)
            _idColumnsNames = null;
        
        this._idColumnsIndexes = values;
    }
        
    //----------------------------------------------------------------
    /**
     * Obtiene el proveedor de comportamiento para los llamados a
     * eventos antes de actualización.
     * @return 
     */
    public IBasicTransactionSynchronizeBehavior getBasicTransactionBeforeUpdateBehavior()
    {
        return beforeUpdateBehavior;
    }

    //----------------------------------------------------------------
    /**
     * Establece el proveedor de comportamiento para los llamados a
     * eventos antes de actualización.
     * @param value
     */
    public void setBasicTransactionBeforeUpdateBehavior(IBasicTransactionSynchronizeBehavior value)
    {
        this.beforeUpdateBehavior = value;
    }

    //----------------------------------------------------------------
    /**
     * Obtiene el proveedor de comportamiento para los llamados a
     * eventos después de actualización.
     * @return 
     */
    public IBasicTransactionSynchronizeBehavior getBasicTransactionAfterUpdateBehavior()
    {
        return afterUpdateBehavior;
    }

    //----------------------------------------------------------------
    /**
     * Establece el proveedor de comportamiento para los llamados a
     * eventos después de actualización.
     * @param value
     */
    public void setBasicTransactionAfterUpdateBehavior(IBasicTransactionSynchronizeBehavior value)
    {
        this.afterUpdateBehavior = value;
    }
}