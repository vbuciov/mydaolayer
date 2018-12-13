package datalayer.utils;

/**
 * Toda la informacion necesaria para crear un Parametro en un query.
 * @author Victor Manuel Bucio Vargas
 */
public class QueryParameter extends DefaultParameter
{
    public enum Operator
    {
        NOTHING, EQUAL, NOT_EQUAL, GREATER_EQUAL, GREATER_THAN, LOWER_EQUAL, LOWER_THAN, LIKE, NOT_LIKE, BETWEEN
    };
   
    private Operator applyOperator;
    //private int returnType;
    
    //--------------------------------------------------------------------
    public QueryParameter()
    {
        super ();
        applyOperator = Operator.NOTHING;
        //returnType = Types.INTEGER;
    }
    
    //--------------------------------------------------------------------
    public QueryParameter(Operator toApply)
    {
        super ();
        applyOperator = toApply;
        //returnType = Types.INTEGER;
    }
    
    //--------------------------------------------------------------------
    public QueryParameter(String name, int SQLType, Object value)
    {
        super (name,SQLType,value);
        applyOperator = Operator.NOTHING;
        //returnType = Types.INTEGER;
    }
    
    //--------------------------------------------------------------------
    public QueryParameter(String name, int SQLType, Object value, Operator toApply)
    {
        super (name,SQLType,value);
        applyOperator = toApply;
        //returnType = Types.INTEGER;
    }

    //--------------------------------------------------------------------
    public Operator getApplyOperator()
    {
        return applyOperator;
    }
    
    //--------------------------------------------------------------------
    public void setApplyOperator(Operator value)
    {
        applyOperator = value;
    }
    
    //--------------------------------------------------------------------
    public boolean isConditional ()
    {
        return applyOperator.ordinal() > Operator.NOTHING.ordinal();
    }
    
    //--------------------------------------------------------------------
    /*public int getReturnType()
    {
        return returnType;
    }*/
    
    //--------------------------------------------------------------------
    /**
     * use java.sql.Types.
     * @param value
     */
    /*public void setReturnType(int value)
    {
        returnType = value;
    }*/
}