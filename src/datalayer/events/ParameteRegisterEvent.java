package datalayer.events;

import datalayer.wrappers.ParameterCollectionRegister;
import java.sql.CallableStatement;
import java.sql.ParameterMetaData;

/**
 * Información sobre el evento que requiere registrar parametros de salida
 * en un objeto CallableStatement.
 * @author Victor Manuel Bucio Vargas
 */
public class ParameteRegisterEvent extends ParameterCollectionRegister
{
    private final String _CALL;
    
    public ParameteRegisterEvent(String theCALL, CallableStatement toDecorate, 
                                                 ParameterMetaData the_parametersInfo)
    {
        super(toDecorate, the_parametersInfo);
        _CALL = theCALL;
    }
    
    /**
     * Obtiene la sentencia DML que desencadeno la ejecución de este evento.
     * @return 
     */
    public String getDML()
    {
        return _CALL;
    }
}