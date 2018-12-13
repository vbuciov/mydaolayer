package datalayer;

import datalayer.api.IEntity;
import datalayer.api.IMediatorDataSource;
import datalayer.api.ISimpleParameterDefinition;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Proporciona algoritmos basicos para recuperación y actualización de datos,
 * pero traduciendo los objetos de IEntity a Query.
 * @author Victor Manuel Bucio Vargas
 */
public abstract class BatchJDBCMappeableOperationSet<T> extends BatchJDBCCallableOperationSet<T>
{
    //----------------------------------------------------------------
    public BatchJDBCMappeableOperationSet(IMediatorDataSource mysource)
    {
        super(mysource);
    }
    
    //----------------------------------------------------------------
    public BatchJDBCMappeableOperationSet(IMediatorDataSource mysource, String[] idColumnsNames )
    {
        super(mysource, idColumnsNames);
    }
    
    //----------------------------------------------------------------
    public BatchJDBCMappeableOperationSet(IMediatorDataSource mysource, int[] idColumnsIndexes )
    {
        super(mysource, idColumnsIndexes);
    }
    
    //--------------------------------------------------------------------
    /**
     * Ejecuta un procedimiento almacenado .
     *
     * @param values Especified the IN parameters ONLY, you can't use OUT
     * parameters
     * @return Una lista de objetos ;<T>
     */
    final protected List<T> executeReadStoredProcedure(IEntity values)
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
                    = dataSource.prepareQueryCall(dclSintax.formatCallProcedure(values.getTableName(), values), session);

            parameterInfo = request.getParameterMetaData();

            /*TODO: Configure Out Put Parameters WITH IEntity Object.
              if (recollector != null)
                onPrepareProcedure(new ParameteRegisterEvent(values.getTableName(),
                                                             request,
                                                             parameterInfo));*/

            onConvertEntity(request, parameterInfo, values);

            //2.2.-Efectuamos operación y Evaluamos la existencia de algún resultado.
            if (request.execute())
                fillResultTraversingMultipleResultSet(request, values.getTableName(), result);

            //2.3.- Add the Out Put parameters results
            /* TODO: Recollect Out Put Parameters WITH IEntity Object.
               if (recollector != null)
                fillOutParameterHolder(recollector, request, parameterInfo);*/

            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format(MSJ_RETRIEVE, result.size(), values.getTableName());
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
     * Ejecuta una función y nos devuelve el resultado de la misma.
     *
     * @param values
     * @return
     */
    protected Object executeFunction(IEntity values)
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
                    = dataSource.prepareCall(dclSintax.formatCallFunction(values.getTableName(), values), session);

            parameterInfo = request.getParameterMetaData();

            //2.- Realizar operacion         
            if (values.size() > 0)
            {
                request.registerOutParameter(1, values.getReturnType());
                
                onConvertEntity(request, parameterInfo, values);
         
                // Execute and retrieve the returned values
                if (!request.execute())
                    result = request.getObject(1);

                if (result instanceof ResultSet)
                {
                    ResultSet rows = (ResultSet) result;
                    rows.close();
                    result = null;
                }
            }

            //3.- Desconectar
            request.close();

            //3.- Liberamos la conexión con el servidor
            dataSource.disconnect(session);

            //4.- Generamos una respuesta.
            message = String.format("%s %s", MSJ_OPERATION_FINISH, values.getTableName());
            state = result != null ? SUCCESS_OPERATION : NOT_RECIEVED;
        }
        catch (SQLException ex)
        {
            handleErrorAndWriteLog(session, ex, BatchJDBCProducerOperationSet.class.getName());
        }
        return result;
    }
    
                    //----------------------------------------------------------------
    final protected void onConvertEntity(PreparedStatement parameters,
                                 ParameterMetaData parametersMetaData,
                                 IEntity values) throws SQLException
    {
        int parameterIndex = 1, i = 0;
        ISimpleParameterDefinition value;
         
        while (i < values.size())
        {
            if (!values.isConditional(i))
            {
                value = values.remove(i);
                setParameter(parameters, parameterIndex++, value.getValue(), value.getSQLType());
            }
            else
              i++;
        }
        
        i = 0; 
        while (i < values.size())
        {
          if (values.isConditional(i))
          {
            value = values.remove(0);
            setParameter(parameters, parameterIndex++, value.getValue(), value.getSQLType());
          }
          else
              i++;
        } 
    }
    
        //----------------------------------------------------------------
    private void setParameter(PreparedStatement parameters, int parameterIndex, Object value, int kind) throws SQLException
    {
        switch (kind)
        {
            case Types.VARCHAR:
                if (value != null)
                    parameters.setString(parameterIndex, (String) value);
                else
                    parameters.setNull(parameterIndex, Types.VARCHAR);
                break;

            case Types.INTEGER:
                if (value != null)
                    parameters.setInt(parameterIndex, (Integer) value);
                else
                    parameters.setNull(parameterIndex, Types.INTEGER);
                break;

            case Types.BLOB:
                if (value != null)
                    parameters.setBytes(parameterIndex, (byte[]) value);
                else
                    parameters.setNull(parameterIndex, Types.BLOB);
                break;

            case Types.BOOLEAN:
                if (value != null)
                    parameters.setBoolean(parameterIndex, (Boolean) value);
                else
                    parameters.setNull(parameterIndex, Types.BOOLEAN);
                break;

            case Types.CHAR:
                if (value != null)
                    parameters.setString(parameterIndex, ((Character) value).toString());
                else
                    parameters.setNull(parameterIndex, Types.CHAR);
                break;

            case Types.DATE:
                if (value != null)
                {
                    if (value instanceof Date)
                        parameters.setDate(parameterIndex,(Date)value);
                    else
                        parameters.setDate(parameterIndex, new Date(((java.util.Date) value).getTime()));
                }
                else
                    parameters.setNull(parameterIndex, Types.DATE);
                break;

            case Types.TIME:
                if (value != null)
                {
                    if (value instanceof Time)
                        parameters.setTime(parameterIndex, (Time)value);
                    else
                        parameters.setTime(parameterIndex, new Time(((java.util.Date) value).getTime()));
                }
                else
                    parameters.setNull(parameterIndex, Types.TIME);
                break;

            case Types.TIMESTAMP:
                if (value != null)
                {
                    if (value instanceof Timestamp)
                        parameters.setTimestamp(parameterIndex, (Timestamp)value);
                    else
                        parameters.setTimestamp(parameterIndex, new Timestamp(((java.util.Date) value).getTime()));
                }
                else
                    parameters.setNull(parameterIndex, Types.TIMESTAMP);
                break;

            case Types.DOUBLE:
                if (value != null)
                    parameters.setDouble(parameterIndex, (Double) value);
                else
                    parameters.setNull(parameterIndex, Types.DOUBLE);
                break;

            case Types.FLOAT:
                if (value != null)
                    parameters.setFloat(parameterIndex, (Float) value);
                else
                    parameters.setNull(parameterIndex, Types.FLOAT);
                break;

            case Types.SMALLINT:
                if (value != null)
                    parameters.setShort(parameterIndex, (Short) value);
                else
                    parameters.setNull(parameterIndex, Types.SMALLINT);
                break;
        }
    }
}