/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer.api;

/**
 * Permite la lectura de una lista de objetos de parametros.
 * @author Victor Manuel Bucio Vargas
 */
public interface ISimpleParametersCollection
{
         /**
     * Returns the number of elements in this list.  If this list contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this list
     */
    int size();
    
    ISimpleParameterDefinition remove (int index);
    
    ISimpleParameterDefinition getDataDefinitionOf(int index);

    ISimpleParameterMetaData getMetaDataOf(int index);
        
    String getKeyName (int index);
    
    Object getValueOf (int index);
    
    int getSQLType (int index);
}
