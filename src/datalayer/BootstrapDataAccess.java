package datalayer;

import static datalayer.BasicJDBCUpdateSet.DEFAULT_BATCH;
import datalayer.api.IDataAccess;
import datalayer.api.IMediatorDataSource;
import datalayer.api.ISynchronizableDataAccess;
import datalayer.events.DataSendToQueryEvent;
import datalayer.utils.Relation;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Victor Manuel Bucio Vargas
 * @param <T>
 */
public abstract class BootstrapDataAccess<T> extends BatchJDBCProducerOperationSet<T> implements IDataAccess<T>, ISynchronizableDataAccess<T>
{
    protected boolean withAutoID;
    private final Relation basic;
    private final String[] projection;
    public String insertProcedure, updateProcedure, deleteProcedure;
     
     //----------------------------------------------------------------
    public BootstrapDataAccess(IMediatorDataSource mysource, String tableName)
    {
        super(mysource, tableName);
        withAutoID = true;        
        projection = null;
        basic = new Relation(tableName);     
    }

    //----------------------------------------------------------------
    public BootstrapDataAccess(IMediatorDataSource mysource, String tableName, String... columns)
    {
        super(mysource, tableName);
        projection = columns;
        basic = new Relation(tableName, projection);
        withAutoID = true;
    }
    
    //----------------------------------------------------------------
    public BootstrapDataAccess(IMediatorDataSource mysource, String tableName, String[] idColumnsNames, String... columns )
    {
        super(mysource, idColumnsNames, tableName);
        projection = columns;
        basic = new Relation(tableName, projection);        
        withAutoID = true;
    }
    
    //----------------------------------------------------------------
    public BootstrapDataAccess(IMediatorDataSource mysource, String tableName, int[] idColumnsIndexes, String... columns )
    {
        super(mysource, idColumnsIndexes, tableName);
        projection = columns;
        basic = new Relation(tableName, projection);
        withAutoID = true;
    }

    //---------------------------------------------------------------------
    @Override
    public int Create(T values)
    {
        return null!=insertProcedure?  
               executeUpdateStoredProcedure(insertProcedure, values, withAutoID): 
               submitUpdate(DataSendToQueryEvent.Type.CREATE, values, withAutoID);
    }
    
    //---------------------------------------------------------------------
    @Override
    public int Create(Connection session, T values) throws SQLException
    {
        return doUpdate(session, DataSendToQueryEvent.Type.CREATE,
                            values, withAutoID);
    }

    //---------------------------------------------------------------------
    @Override
    public int Create(List<T> values)
    {
        return null!=insertProcedure?  
               executeUpdateStoredProcedure(insertProcedure, 
                                            values, 
                                            true, 
                                            1, 
                                            withAutoID):
               submitUpdate(DataSendToQueryEvent.Type.CREATE,
                            values, 
                            true, 
                            withAutoID);
    }
    
        //---------------------------------------------------------------------
    @Override
    public int Create(Connection session, List<T> values) throws SQLException
    {
        return doUpdate(session, DataSendToQueryEvent.Type.CREATE,
                            values, 
                            DEFAULT_BATCH, 
                            withAutoID);
    }

    //---------------------------------------------------------------------
    @Override
    public int Update(T values)
    {
        return null != updateProcedure ?
               executeUpdateStoredProcedure(updateProcedure, values, withAutoID):
               submitUpdate(DataSendToQueryEvent.Type.UPDATE, values);
    }
    
    //---------------------------------------------------------------------
    @Override
    public int Update(Connection session, T values) throws SQLException
    {
        return doUpdate(session, DataSendToQueryEvent.Type.UPDATE,
                            values, false);
    }

    //---------------------------------------------------------------------
    @Override
    public int Update(List<T> values)
    {
        return null!=updateProcedure?  
               executeUpdateStoredProcedure(updateProcedure, values, true, 1, withAutoID):
               submitUpdate(DataSendToQueryEvent.Type.UPDATE, values);
    }
    
    //---------------------------------------------------------------------
    @Override
    public int Update(Connection session, List<T> values) throws SQLException
    {
        return doUpdate(session, 
                        DataSendToQueryEvent.Type.UPDATE,
                        values,
                        DEFAULT_BATCH,
                        false);
    }

    //---------------------------------------------------------------------
    @Override
    public int Delete(T values)
    {
        return null != deleteProcedure ?
               executeUpdateStoredProcedure(updateProcedure, values, withAutoID):
               submitUpdate(DataSendToQueryEvent.Type.DELETE,
                            values);
    }
    
        //---------------------------------------------------------------------
    @Override
    public int Delete(Connection session, T values) throws SQLException
    {
        return doUpdate(session, 
                        DataSendToQueryEvent.Type.DELETE,
                        values, 
                        false);
    }

    //---------------------------------------------------------------------
    @Override
    public int Delete(List<T> values)
    {
        return null!=deleteProcedure?  
               executeUpdateStoredProcedure(deleteProcedure, values, true, 1, withAutoID):
               submitUpdate(DataSendToQueryEvent.Type.DELETE,
                            values);
    }
    
        //---------------------------------------------------------------------
    @Override
    public int Delete(Connection session, List<T> values) throws SQLException
    {
        return doUpdate(session, 
                        DataSendToQueryEvent.Type.DELETE,
                        values, 
                        DEFAULT_BATCH,
                        false);
    }

    //--------------------------------------------------------------------
    @Override
    /**
     * Recupera una Lista de elementos.
     * @return Elementos existentes en la tabla asociada con este DataAccess.
     */
    public List<T> Retrieve()
    {
        return submitQuery(dmlSintax.formatSelect(basic));
    }

    //--------------------------------------------------------------------
        /**
     * Recupera una Lista de elementos que cumplan los criterios especificados
     * por el paramtro Filter.
     * @param Filter
     * @return Elementos que cumplen los criterios proporcionados y existentes
     * en la tabla asociada con este DataAccess.
     */
    @Override
    public List<T> Retrieve(T Filter)
    {
        return submitQuery(basic, Filter);
    }

    //--------------------------------------------------------------------
    /**
     * Recupera una Lista de elementos que cumplan las collección de filtros 
     * provistos por el paramtro Filters.
     * Nota: En realidad se ejecuta una operación por cada elemento existente
     * en Filters, pero todos los resultados son integrados en un único resultado.
     * @param Filters
     * @return Elementos que cumplen los criterios proporcionados y existentes
     * en la tabla asociada con este DataAccess.
     */
    public List<T> Retrieve(List<T> Filters)
    {
        return submitQuery(basic, Filters);
    }
    
    //----------------------------------------------------------------
    /**
     * Obtiene un valor que indica si se esta utilizando el método 
     * onConvertKeyResult para evaluar si cualquier Resultado proveniente 
     * de la ejecución de un procedimiento almacedao u operación de INSERT
     * debe ser tratado como la llave primaria.
     */
    public boolean isWithAutoID()
    {
        return withAutoID;
    }

    //----------------------------------------------------------------
    /**
     * Establece un valor que indica si el método onConvertKeyResult debe
     * ser ejecutado para evaluar cualquier Resultado proveniente de la 
     * ejecución de procedimiento almacenado u operación de INSERT debe
     * ser tratado como la llave primaria.
     * @param value
     */
    public void setWithAutoID(boolean value)
    {
        this.withAutoID = value;
    }

    //----------------------------------------------------------------
    /**
     * Obtiene las columnas que se utilizan para realizar una proyeccion
     * en operaciones de recuperación de información.
     * @return 
     */
    public String[] getColumns()
    {
        return projection;
    }
    
    //----------------------------------------------------------------
    /**
     * Establece las columnas por las cuales se realizar el ordenamiento en
     * operaciones de recuperación de información.
     */
    public void setBasicOrderBy(String... options)
    {
        basic.setOrderBy(options);
    }
    
    //---------------------------------------------------------------------
    /**
     * Obtiene el nombre del método utilizado para realizar operaciones de 
     * INSERT en caso de haber sido especificado.
     */
    public String getInsertProcedure()
    {
        return insertProcedure;
    }

    //---------------------------------------------------------------------
    /**
     * Establece e indica que las operaciones de INSERT deben utilizar
     * un procedimiento almacenado con el nombre especificado en lugar
     * de un sentencia de DML.
     */
    public void setInsertProcedure(String value)
    {
        this.insertProcedure = value;
    }

    //---------------------------------------------------------------------
    /**
     * Obtiene el nombre del método utilizado para realizar operaciones de 
     * UPDATE en caso de haber sido especificado.
     */
    public String getUpdateProcedure()
    {
        return updateProcedure;
    }

    //---------------------------------------------------------------------
    /**
     * Establece e indica que las operaciones de UPDATE deben utilizar
     * un procedimiento almacenado con el nombre especificado en lugar
     * de un sentencia de DML.
     */
    public void setUpdateProcedure(String value)
    {
        this.updateProcedure = value;
    }

    //---------------------------------------------------------------------
    /**
     * Obtiene el nombre del método utilizado para realizar operaciones de 
     * DELETE en caso de haber sido especificado.
     */
    public String getDeleteProcedure()
    {
        return deleteProcedure;
    }

    //---------------------------------------------------------------------
    /**
     * Establece e indica que las operaciones de DELETE deben utilizar
     * un procedimiento almacenado con el nombre especificado en lugar
     * de un sentencia de DML.
     */
    public void setDeleteProcedure(String value)
    {
        this.deleteProcedure = value;
    }
}