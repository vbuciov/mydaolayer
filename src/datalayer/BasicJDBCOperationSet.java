package datalayer;

import datalayer.api.IMediatorDataSource;
import datalayer.events.DataGetEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import datalayer.api.IResultSetBehavior;

/**
 * Proporciona algoritmos basicos para recuperación y actualización de datos,  
 * de una consulta a la vez.
 * 
 * @author Victor Manuel Bucio Vargas
 * @param <T> Tipo de objeto a utilizar
 */
public abstract class BasicJDBCOperationSet<T> extends BasicJDBCUpdateSet
{
    private IResultSetBehavior<T> result_behaivor;
    
    //----------------------------------------------------------------
    public BasicJDBCOperationSet(IMediatorDataSource mysource)
    {
        super(mysource);
    }
    
    //----------------------------------------------------------------
    public BasicJDBCOperationSet(IMediatorDataSource mysource, String[] idColumnsNames )
    {
        super(mysource, idColumnsNames);
    }
    
    //----------------------------------------------------------------
    public BasicJDBCOperationSet(IMediatorDataSource mysource, int[] idColumnsIndexes )
    {
        super(mysource, idColumnsIndexes);
    }
    

    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar y convertir en List el resultado de un
     * Query.
     *
     * @param query La sentencia a ejecutar
     * @return Una lista de objetos ;<T>
     */
    public List<T> submitQuery(String query)
    {
        List<T> result = new ArrayList();
        Connection session = null;

        try
        {
            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //TODO: session could handle this
            //1.1.- Si es necesario cambiamos el esquema
            if (eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            Statement request = dataSource.createQueryStatement(session);
            
            ResultSet response = request.executeQuery(query); 
            //2.2.- Recorremos el resultado obtenido.
            do
            {
               if (null == response)
                   response = request.getResultSet();
                fillResult(response, query, result);
                response = null;
            }
            while (request.getMoreResults());           

            //3.- Liberamos los recursos utilizados
            //3.1.- Liberamos la petición utilizada.
            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), query);
            state = result.size() > ZERO ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCOperationSet.class.getName());
        }

        return result;
    }
    
        //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar y convertir en List el resultado de un
     * Query.
     *
     * @param querys Las sentencias a ejecutar
     * @return Una lista de objetos <T>
     */
    public List<T> submitQuery(String[] querys)
    {
        return submitQuery(querys, 1);
    }
   
    //----------------------------------------------------------------
    /**
     * Asegura lo necesario para ejecutar y convertir en List el resultado de un
     * Query.
     *
     * @param querys Las sentencias a ejecutar
     * @param packetSize Cantidad de valores que se enviaran por petición.
     * Nota: Cualquier valor mayor que uno, involucrá a que la fuente de datos sea
     * capaz de efectuar este tipo de operación a través de lotes.
     * @return Una lista de objetos <T>
     */
    public List<T> submitQuery(String[] querys, int packetSize)
    {
        int batchCount = ZERO, batchSuccess = ZERO;
        List<T> result = new ArrayList();
        StringBuilder currentQuery = new StringBuilder();
        StringBuilder finalQuery = new StringBuilder();
        Connection session = null;

        try
        {
            ResultSet response = null;

            //1.- Establecemos conexión con el servidor.
            session = dataSource.createSession();

            //TODO: session could handle this
            //1.1.- Si es necesario cambiamos el esquema
            if ( eschema != EMTPY_STRING)
                dataSource.use(eschema, session);

            //2.- Efectuamos nuestra operación.
            //2.1.- Solicitamos los recursos necesarios para efectuar nuestra operación.
            Statement request = dataSource.createQueryStatement(session);
            
            for (String query : querys)
            {
                if (currentQuery.length() > ZERO)
                    currentQuery.append("; ");
                currentQuery.append(query);

                if (finalQuery.length() > ZERO)
                    finalQuery.append("; ");
                finalQuery.append(query);

                if (packetSize < 2)
                    response = request.executeQuery(query);

                else
                {
                    request.addBatch(query);
                    batchCount++;
   
                    //2.2.1.- Ejecutamos una parcialidad.
                    if (batchCount % packetSize == ZERO)
                    {
                        batchSuccess = calculateSuccessCount(dataSource.executeQueryBatch(request));
                        //batchSuccess = calculateSuccessCount(request.executeBatch());
                        response = request.getResultSet();
                        batchCount = ZERO;
                        currentQuery.delete(ZERO, currentQuery.length());
                    }
                }
                 
                if (null != response)
                {
                    do
                    {
                        if (null == response )
                            response = request.getResultSet();
                        fillResult(response, query, result);
                        response = null;
                    }
                    while (request.getMoreResults());
                }
            }

            //2.2.- Recorremos el resultado obtenido.
            if (batchCount > ZERO)
            {
                batchSuccess = 
                            calculateSuccessCount(dataSource.executeQueryBatch(request));
                //batchSuccess = calculateSuccessCount(request.executeBatch());

                do
                {
                    response = request.getResultSet();
                    fillResult(response, currentQuery.toString(), result);
                }
                while (request.getMoreResults());
            }

            //3.- Liberamos los recursos utilizados
            //3.1.- Liberamos la petición utilizada.
            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), finalQuery.toString());
            state = batchSuccess > ZERO ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCOperationSet.class.getName());
        }

        return result;
    }
    
    //----------------------------------------------------------------
    /**
     * Calcula la cantidad de ejecuciones existosas según el resultado de una
     * ejecución por lotes.
     *
     * @param batchResults
     * @return El número de ejecuciones exitosas.
     */
    final protected int calculateSuccessCount(int[] batchResults)
    {
        int success = ZERO;

        for (int result : batchResults)
            success += result == Statement.SUCCESS_NO_INFO ? 1 : 0;

        return success;
    }
    
    //--------------------------------------------------------------------
    /**
     * Recorre un ResultSet, invocando el evento de transformación hacia 
     * un objeto de transferencia.
     * @param response
     * @param query
     * @param result
     * @throws java.sql.SQLException
     */
    final protected void fillResult(ResultSet response, String query, List<T> result) throws SQLException, UnsupportedOperationException
    {
        DataGetEvent evento = new DataGetEvent(query, response, response.getMetaData());
        
        //2.3.- Recorremos el resultado obtenido.
        while (response.next())
            result.add(onConvertResultRow(evento));
        
        //2.4.- Cerramos los recursos utilizados
        response.close();
    }
    
    //----------------------------------------------------------------
    /**
     * The ResultSet is convert to Transfer Object.
     *
     * @param e
     * @return
     * @throws java.sql.SQLException
     */
    protected T onConvertResultRow(DataGetEvent e) throws SQLException, UnsupportedOperationException
    {
        if (result_behaivor == null)
            throw new UnsupportedOperationException("Convert Result to Transfer Object Not supported yet.");

        else
            return result_behaivor.convertTo(e);
    }
    
    //----------------------------------------------------------------
    /**
     * Obtiene el proveedor de comportamiento de conversión para los querys que
     * involucren un ResultSet.
     *
     * @return
     */
    public IResultSetBehavior<T> getResult_behaivor()
    {
        return result_behaivor;
    }

    //----------------------------------------------------------------
    /**
     * Establece el proveedor de comportamiento de conversión para los querys
     * que involucren un ResultSet.
     *
     * @param value
     */
    public void setResult_behaivor(IResultSetBehavior<T> value)
    {
        this.result_behaivor = value;
    }
}