package datalayer.utils;

import java.util.ArrayList;
import datalayer.api.ISimpleParameterDefinition;

/**
 * Incluye operaciones que permiten construir una lista de parametros de 
 * tipo fijo.
 * @author Victor Manuel Bucio Vargas
 */
public class QueryParametersBuilder extends ArrayList<QueryParameter> 
{    
   //--------------------------------------------------------------------
   public QueryParameter add(String name, int sqlType, int size, int direction)
   {
      QueryParameter the_new = new QueryParameter();
      DefaultMetaData meta_data = new DefaultMetaData(DefaultMetaData.NON_VALUE, size, direction);
      
      the_new.setMetaData(meta_data);
      the_new.setKeyName(name);
      the_new.setSQLType(sqlType);
      add(the_new);
     
      return the_new;
   }
    
   //--------------------------------------------------------------------
   public QueryParameter add(String name, int sqlType, int size)
   {
      QueryParameter the_new = new QueryParameter();
      DefaultMetaData meta_data = new DefaultMetaData(DefaultMetaData.NON_VALUE, size);
      
      the_new.setMetaData(meta_data);
      the_new.setKeyName(name);
      the_new.setSQLType(sqlType);
      add(the_new);
     
      return the_new;
   }

   //--------------------------------------------------------------------
   public QueryParameter add(String name, int sqlType)
   {
      QueryParameter the_new = new QueryParameter();
      DefaultMetaData meta_data = new DefaultMetaData();
      
      the_new.setMetaData(meta_data);
      the_new.setKeyName(name);
      the_new.setSQLType(sqlType);
      add(the_new);

      return the_new;
   }
   
   //--------------------------------------------------------------------
   public QueryParameter add(String name, int sqlType, QueryParameter.Operator kind)
   {
      QueryParameter the_new = new QueryParameter();
      DefaultMetaData meta_data = new DefaultMetaData();
      
      the_new.setMetaData(meta_data);
      the_new.setKeyName(name);
      the_new.setSQLType(sqlType);
      the_new.setApplyOperator(kind);
      add(the_new);
     
      return the_new;
   }
  

   //--------------------------------------------------------------------
   public QueryParameter add(int column_index, int sqlType, int size)
   {
      QueryParameter the_new = new QueryParameter();
      DefaultMetaData meta_data = new DefaultMetaData(column_index, size);
      
      the_new.setMetaData(meta_data);
      the_new.setSQLType(sqlType);
      add(the_new);
      
      return the_new;
   }

   //--------------------------------------------------------------------
   public QueryParameter add(int column_index, int sqlType)
   {
      QueryParameter the_new = new QueryParameter();
      DefaultMetaData meta_data = new DefaultMetaData(column_index);
      
      the_new.setMetaData(meta_data);
      the_new.setSQLType(sqlType);
      add(the_new);

      return the_new;
   }
   

   //--------------------------------------------------------------------
   public boolean contains(String name)
   {
      boolean find = false;
      int i = 0;
      ISimpleParameterDefinition current;

      while (!find && i < size())
      {
         current = get(i++);
         find = current.getKeyName().equals(name);
      }

      return find;
   }

   //--------------------------------------------------------------------
   public int indexOf(String name)
   {
      boolean find = false;
      int i = 0;
      ISimpleParameterDefinition current;

      while (!find && i < size())
      {
         current = get(i++);
         find = current.getKeyName().equals(name);
      }

      return --i;
   }

   //--------------------------------------------------------------------
   public boolean contains(int column_index)
   {
      boolean find = false;
      int i = 0;
      ISimpleParameterDefinition current;

      while (!find && i < size())
      {
         current = get(i++);
         find = current.getMetaData().getOrdinalIndex() == column_index;
      }

      return find;
   }

   //--------------------------------------------------------------------
   public int indexOf(int column_index)
   {
      boolean find = false;
      int i = 0;
      ISimpleParameterDefinition current;

      while (!find && i < size())
      {
         current = get(i++);
         find = current.getMetaData().getOrdinalIndex() == column_index;
      }

      return --i;
   }
}