package datalayer.utils;

import datalayer.api.ISimpleParameterMetaData;

/**
 * 
 * @author Victor Manuel Bucio Vargas
 */
public class DefaultMetaData implements ISimpleParameterMetaData
{
   public final static int NOTHING_PARAM = -1;
   public final static int OUT_PARAM = 0;
   public final static int IN_PARAM = 1;
   public final static int OUT_IN_PARAM = 2;
   public final static int NON_VALUE = -1;

    
   private boolean allow_null;
   private int index, direction, size, scale, precision;

   public DefaultMetaData()
   {
      allow_null = true;
      index = NON_VALUE;
      direction = IN_PARAM;
      size = NON_VALUE;
      scale = NON_VALUE;
      precision = NON_VALUE;
   }
   
    public DefaultMetaData(int index)
   {
      allow_null = true;
      this.index = index;
      direction = IN_PARAM;
      size = NON_VALUE;
      scale = NON_VALUE;
      precision = NON_VALUE;
   }
    
   public DefaultMetaData(int index, int size)
   {
      allow_null = true;
      this.index = index;
      direction = IN_PARAM;
      this.size = size;
      scale = NON_VALUE;
      precision = NON_VALUE;
   }
      
   public DefaultMetaData(int index, int size, int direction)
   {
      allow_null = true;
      this.index = index;
      this.direction = direction;
      this.size = size;
      scale = NON_VALUE;
      precision = NON_VALUE;
   }

   @Override
   public boolean isAllowNull()
   {
      return allow_null;
   }

   public void setAllowNull(boolean value)
   {
      allow_null = value;
   }

   @Override
   public int getOrdinalIndex()
   {
      return index;
   }

   public void setOrdinalIndex(int value)
   {
      index = value;
   }

   @Override
   public int getDirection()
   {
      return direction;
   }

   public void setDirection(int value)
   {
      direction = value;
   }

   @Override
   public int getSize()
   {
      return size;
   }

   public void setSize(int value)
   {
      size = value;
   }

    @Override
    public int getPrecision()
    {
        return precision;
    }
    
    public void setPrecision(int value)
    {
        precision = value;
    }

    @Override
    public int getScale()
    {
        return scale;
    }
    
    public void setScale (int value)
    {
        scale = value;
    }
    
    public void setAsOutParam()
    {
        direction = OUT_PARAM;
    }
    
    public boolean isOutParam()
    {
        return direction == OUT_PARAM;
    }
    
    public void setAsInParam()
    {
        direction = IN_PARAM;
    }
    
    public boolean isInParam(){
        return direction == IN_PARAM;
    }
    
    public void setAsOutInParam()
    {
        direction= OUT_IN_PARAM;
    }
    
    public boolean isOutInParam()
    {
        return direction == OUT_IN_PARAM;
    }
    
    public void setAsNothingParam()
    {
        direction = NOTHING_PARAM;
    }
    
    public boolean isNothingParam()
    {
        return direction == NOTHING_PARAM;
    }
}