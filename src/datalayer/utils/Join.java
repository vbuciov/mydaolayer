/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer.utils;

/**
 * Indica al manejador la realización de una operación de Join.
 * @author Victor Manuel Bucio Vargas
 */
public class Join 
{
    public enum joinType {NATURAL, NATURAL_LEFT_OUTER, NATURAL_RIGHT_OUTER, LEFT_OUTER, RIGHT_OUTER, INNER, CROSS, NOTHING};
    
    String storeObject;
    String onConditional;
    joinType option;
    
    /**
     * CROSS JOIN
    * @param pstoreObject
     */
     public Join (String pstoreObject)
    {
        storeObject = pstoreObject;
        onConditional = "";
        option = joinType.CROSS;
    }
    
     /**
      * INNER JOIN
    * @param pstoreObject
    * @param pOnConditional
      */
    public Join (String pstoreObject, String pOnConditional)
    {
        storeObject = pstoreObject;
        onConditional = pOnConditional;
        option = joinType.INNER;
    }
    
    /**
     * Especify JOIN
    * @param pstoreObject
    * @param pOnConditional
    * @param pOption
     */
    public Join (String pstoreObject, String pOnConditional, joinType pOption)
    {
        storeObject = pstoreObject;
        onConditional = pOnConditional;
        option = pOption;
    }
    
    public String getStoreObject() {
        return storeObject;
    }

    public void setStoreObject(String storeObject) {
        this.storeObject = storeObject;
    }

    public String getOnConditional() {
        return onConditional;
    }

    public void setOnConditional(String onConditional) {
        this.onConditional = onConditional;
    }

    public joinType getOption() {
        return option;
    }

    public void setOption(joinType option) {
        this.option = option;
    }
}
