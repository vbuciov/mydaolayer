package datalayer.api;

/**
 * Añade a un par de valores Llave y Valor, informacion acerca
 * del tipo de dato y otras reglas de integridad
 * @author Victor Manuel Bucio Vargas
 */
public interface ISimpleParameterDefinition extends IHashRecord
{     
    int getSQLType ();
    ISimpleParameterMetaData getMetaData();
}
