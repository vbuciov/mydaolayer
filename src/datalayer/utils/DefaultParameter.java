package datalayer.utils;

import datalayer.api.ISimpleParameterDefinition;
import datalayer.api.ISimpleParameterMetaData;

/**
 * Es una implementacion sencilla de Parameter Definition 
 * @author Victor Manuel Bucio Vargas
 */
public class DefaultParameter implements ISimpleParameterDefinition
{
    public final static String DEFAULT_VALUE = "default";
    public final static String DEFAULT_NAME = "unknown-parameter";
    
    private ISimpleParameterMetaData metadata;
    private int SQLType;
    private Object value;
    private String key_name;
 
    //--------------------------------------------------------------------
    public DefaultParameter()
    {
        value = DEFAULT_VALUE;
        key_name = DEFAULT_NAME;
        SQLType = java.sql.Types.VARCHAR;
    }

    //--------------------------------------------------------------------
    public DefaultParameter(String name, int SQLType, Object value)
    {
        this.value = value;
        key_name = name;
        this.SQLType = SQLType;
    }

    //--------------------------------------------------------------------
    public void setSQLType(int value)
    {
        this.SQLType = value;
    }

    //--------------------------------------------------------------------
    @Override
    public int getSQLType()
    {
        return this.SQLType;
    }

    //--------------------------------------------------------------------
    public void setValue(Object value)
    {
        this.value = value;
    }

    //--------------------------------------------------------------------
    @Override
    public Object getValue()
    {
        return this.value;
    }

    //--------------------------------------------------------------------
    public void setKeyName(String value)
    {
        key_name = value;
    }

    //--------------------------------------------------------------------
    @Override
    public String getKeyName()
    {
        return key_name;
    }

    //--------------------------------------------------------------------
    @Override
    public ISimpleParameterMetaData getMetaData()
    {
        return metadata;
    }

    //--------------------------------------------------------------------
    public void setMetaData(ISimpleParameterMetaData value)
    {
        metadata = value;

    }
}