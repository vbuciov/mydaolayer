package datalayer.events;

import datalayer.wrappers.ParameterCollectionWriter;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;

/**
 * Información sobre el evento que requiere introducir valores
 * en un objeto PreparedStatement.
 * @author Victor Manuel Bucio Vargas
 */
public class DataSetEvent extends ParameterCollectionWriter
{

    private final String _DML;
    //private T _values;

    public DataSetEvent(String theDML, PreparedStatement toDectorate, 
                                       ParameterMetaData the_parametersInfo/*, 
                                       T values*/)
    {
        super(toDectorate, the_parametersInfo);
        //_values = values;
        _DML = theDML;
    }

    /**
     * Obtiene los valores que se proporcionaron para la ejecución de este
     * evento.
     * @return 
     */
    //public T getValues(){ return _values; }

    /**
     * Obtiene la sentencia DML que desencadeno la ejecución de este evento.
     * @return 
     */
    public String getDML() { return _DML; }

    /**
     * Establece los valores para la ejecución del evento.
     * @param the_values
     */
    //public void setValues(T the_values) { _values = the_values; }
}