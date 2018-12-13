package datalayer.utils;

import datalayer.api.IEntity;
import java.util.Date;
import datalayer.api.ISimpleParameterDefinition;
import datalayer.api.ISimpleParameterMetaData;
import java.sql.Types;

/**
 * Representa toda la informacion necesaria para construir un Query hacia
 * una base de datos relacional.
 * @author Victor Manuel Bucio Vargas
 */
public class QueryDataTransfer implements IEntity
{
    private String tableName;
    private final QueryParametersBuilder datas;
    private int returnType;

    public QueryDataTransfer(String name)
    {
        datas = new QueryParametersBuilder();
        tableName = name;
        returnType = Types.INTEGER;
    }

    //--------------------------------------------------------------------
    @Override
    public String getTableName()
    {
        return tableName;
    }

    //--------------------------------------------------------------------
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
        
    //--------------------------------------------------------------------
    @Override
    public int size()
    {
        return datas.size();
    }
    
    //--------------------------------------------------------------------
    @Override
    public ISimpleParameterDefinition remove(int index)
    {
        return datas.remove(index);
    }

    //--------------------------------------------------------------------
    @Override
    public ISimpleParameterMetaData getMetaDataOf(int index)
    {
        return datas.get(index).getMetaData();
    }
    
    //--------------------------------------------------------------------
    @Override
    public ISimpleParameterDefinition getDataDefinitionOf(int index)
    {
        return datas.get(index);
    }

    //--------------------------------------------------------------------
    @Override
    public String getKeyName(int index)
    {
        return datas.get(index).getKeyName();
    }

    //--------------------------------------------------------------------
    @Override
    public Object getValueOf(int index)
    {
        return datas.get(index).getValue();
    }
    
    //--------------------------------------------------------------------
    /*@Override
    public void setValueOf(int i, Object value)
    {
        return.datas.get(i).get
    }*/
    
    //--------------------------------------------------------------------
    @Override
    public int getSQLType (int index)
    {
        return datas.get(index).getSQLType();
    }

    //--------------------------------------------------------------------
    @Override
    public QueryParameter.Operator getOperatorOf(int index)
    {
        return datas.get(index).getApplyOperator();
    }

    //--------------------------------------------------------------------
    @Override
    public boolean isConditional(int i)
    {
        return datas.get(i).isConditional();
    }
    
    //--------------------------------------------------------------------
    @Override
    public int getReturnType()
    {
         return returnType;
    }
    
    //--------------------------------------------------------------------
    /**
     * you should use java.sql.types.
     * @param value
     */
    public void setReturnType(int value)
    {
        returnType = value;
    }
    
    //--------------------------------------------------------------------
    public void clear()
    {
        datas.clear();
    }
    
    //--------------------------------------------------------------------
    /*@Override
    public void setConditional(int i, boolean value)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
             
    //--------------------------------------------------------------------
    public void addString(String param, String value)
    {
        datas.add(param, java.sql.Types.VARCHAR).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addString(String param, String value, int size)
    {
        datas.add(param, java.sql.Types.VARCHAR, size).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addString(String param, String value, int size, boolean isOut)
    {
        datas.add(param, java.sql.Types.VARCHAR, size, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addString(String param, QueryParameter.Operator kind, String value)
    {
        datas.add(param, java.sql.Types.VARCHAR, kind).setValue(value);
    }
    
    //--------------------------------------------------------------------
     /*public QueryParameter addInteger (String name)
    {
        return datas.add(name, java.sql.Types.INTEGER);
    }

    //--------------------------------------------------------------------
    public QueryParameter addInteger (String name, int size)
    {
        return datas.add(name, java.sql.Types.INTEGER, size);
    }*/
    
    //--------------------------------------------------------------------
    public void addInteger (String name, int value)
    {
        datas.add(name, java.sql.Types.INTEGER).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addInteger (String name, int value, int size)
    {
        datas.add(name, java.sql.Types.INTEGER, size).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addInteger(String param, QueryParameter.Operator kind, Integer value)
    {
        datas.add(param, java.sql.Types.INTEGER, kind).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addDouble(String param, double value)
    {
        datas.add(param, java.sql.Types.DOUBLE).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addDouble(String param, double value, boolean isOut)
    {
        datas.add(param, java.sql.Types.DOUBLE, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addDouble(String param, QueryParameter.Operator kind, double value)
    {
        datas.add(param, java.sql.Types.DOUBLE, kind).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addFloat(String param, float value)
    {
        datas.add(param, java.sql.Types.FLOAT).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addFloat(String param, float value, boolean isOut)
    {
        datas.add(param, java.sql.Types.FLOAT, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addFloat(String param, QueryParameter.Operator kind, float value)
    {
        datas.add(param, java.sql.Types.FLOAT, kind).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addChar(String param, char value)
    {
        datas.add(param, java.sql.Types.CHAR).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addChar(String param, char value, boolean isOut)
    {
        datas.add(param, java.sql.Types.CHAR, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addChar(String param, QueryParameter.Operator kind, char value)
    {
        datas.add(param, java.sql.Types.CHAR,kind).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addDate(String param, Date value)
    {
        datas.add(param, java.sql.Types.DATE).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addDate(String param, Date value, boolean isOut)
    {
        datas.add(param, java.sql.Types.DATE, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addDate(String param, QueryParameter.Operator kind, Date value)
    {
        datas.add(param, java.sql.Types.DATE,kind).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addTime(String param, Date value)
    {
        datas.add(param, java.sql.Types.TIME).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addTime(String param, Date value, boolean isOut)
    {
        datas.add(param, java.sql.Types.TIME, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addTime(String param, QueryParameter.Operator kind, Date value)
    {
        datas.add(param, java.sql.Types.TIME, kind).setValue(value);
    }
    
        //--------------------------------------------------------------------
    public void addDateTime(String param, Date value)
    {
        datas.add(param, java.sql.Types.TIMESTAMP).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addDateTime(String param, Date value, boolean isOut)
    {
        datas.add(param, java.sql.Types.TIMESTAMP, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addDateTime(String param, QueryParameter.Operator kind, Date value)
    {
        datas.add(param, java.sql.Types.TIMESTAMP,kind).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addBoolean(String param, boolean value)
    {
        datas.add(param, java.sql.Types.BOOLEAN).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addBoolean(String param, boolean value, boolean isOut)
    {
        datas.add(param, java.sql.Types.BOOLEAN, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addBoolean(String param, QueryParameter.Operator kind, Boolean value)
    {
        datas.add(param, java.sql.Types.BOOLEAN, kind).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addBytes(String param, byte[] value)
    {
        datas.add(param, java.sql.Types.BLOB).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addBytes(String param, byte[] value, boolean isOut)
    {
        datas.add(param, java.sql.Types.BLOB, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addBytes(String param, QueryParameter.Operator kind, byte[] value)
    {
        datas.add(param, java.sql.Types.BLOB, kind).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addShort(String param, short value)
    {
        datas.add(param, java.sql.Types.SMALLINT).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addShort(String param, short value, boolean isOut)
    {
        datas.add(param, java.sql.Types.SMALLINT, DefaultMetaData.NON_VALUE, 
                  isOut?DefaultMetaData.OUT_PARAM:DefaultMetaData.IN_PARAM).setValue(value);
    }

    //--------------------------------------------------------------------
    public void addShort(String param, QueryParameter.Operator kind, short value)
    {
        datas.add(param, java.sql.Types.SMALLINT, kind).setValue(value);
    }
}