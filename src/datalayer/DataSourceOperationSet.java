package datalayer;

import datalayer.api.IDataSource;
import datalayer.api.IMediatorDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Contiene todas las operaciones necesarias para interactuar con un IDataSource.
 * @author Victor Manuel Bucio Vargas
 */
public abstract class DataSourceOperationSet
{
    public static int DEFAULT_BATCH = 1000;
    public static int ZERO = 0;
    public static int WAITING = 0;
    public static int NOT_AFFECTED = 1;
    public static int NOT_RECIEVED = 2;
    public static int SUCCESS_OPERATION = 3;
    public static int DATASOURCE_ERROR = 4;
    public static int SESSION_ERROR = 5;

    protected final String EMTPY_STRING = "";
    protected final String MSJ_AFECTED = "%d elemento(s) afectado(s) en %s";
    protected final String MSJ_OUTPARAMETER = "%d parametro(s) obtenido(s) al termino de ejecución de %s";
    protected final String MSJ_RETRIEVE = "%d elemento(s) recuperado(s) de %s";
    protected final String MSJ_OPERATION_FINISH = "operación completada correctamente";

    protected String eschema, message;
    protected int state;
    protected IMediatorDataSource dataSource;

    //----------------------------------------------------------------
    public DataSourceOperationSet(IMediatorDataSource mysource)
    {
        dataSource = mysource;
        eschema = EMTPY_STRING;
        state = WAITING;
    }    
    
    //----------------------------------------------------------------
    /**
     * Reporta el error proporcionado en la Exceptión pero también se cálcula en
     * base al estado de la session.
     * @param session Session en la que se genero el error.
     * @param ex Excepcion ocurrida.
     * @param source Clase de origen en la que se ha generado el error.
     */
    protected void handleErrorAndWriteLog(Connection session, SQLException ex, String source)
    {
        state = dataSource.handleError(session, true) ? DATASOURCE_ERROR : SESSION_ERROR;
        message = dataSource.handleExceptionLog(ex, source + " - " + eschema);
    }

    //----------------------------------------------------------------
    /**
     * Calcula la cantidad de registros affectados según el resultado de una
     * ejecución por lotes.
     *
     * @param batchResults
     * @return El número de registros afectados.
     */
    protected int calculateAffectedCount(int[] batchResults)
    {
        int affected = ZERO;

        for (int result : batchResults)
        {
            affected += result > ZERO ? result : ZERO;
        }

        return affected;
    }

            
   //----------------------------------------------------------------
    /**
     * Obtiene el último mensaje generado por la operación realizada.
     *
     * @return
     */
    public String getMessage()
    {
        return message;
    }
    
    //----------------------------------------------------------------
    /**
     * Establece el equema por default en el cual se efectuarán las operaciones.
     v*
     * @param value
     */
    public void setEschema(String value)
    {
        this.eschema = value;
    }
    
    //----------------------------------------------------------------
    /**
     * Obtiene el esquema por default en el cual se efectuarán las operaciones.
     *
     * @return
     */
    public String getEschema()
    {
        return eschema;
    }

    //----------------------------------------------------------------
    /**
     * Obtiene el estado en el que se encuentra el DataAccess tras efectuar una
     * operación.
     *
     * @return
     */
    public int getState()
    {
        return state;
    }

    //----------------------------------------------------------------
    /**
     * Obtiene el DataSource utilizado para efectuar las operaciones.
     *
     * @return
     */
    public IDataSource getDataSource()
    {
        return dataSource;
    }

    //----------------------------------------------------------------
    /**
     * Establece el DataSource utilizado para efectuar las operaciones. que
     * involucren un ResultSet.
     *
     * @param database
     */
    public void setDataSource(IMediatorDataSource database)
    {
        if (database == null)
            throw new IllegalArgumentException("it is Not possible pass a null DataSource");

        this.dataSource = database;
    }
}
