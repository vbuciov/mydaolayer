/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer.utils;

/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public class RelationOptions {
    public enum TypeOption {ORDER_BY,  GROUP_BY};
    
    String[] columns;
    TypeOption operation;
    
    public RelationOptions(String[] columns, TypeOption Option)
    {   
        this.columns = columns;
        operation = Option;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] column) {
        this.columns = column;
    }

    public TypeOption getOperation() {
        return operation;
    }

    public void setOperation(TypeOption operation) {
        this.operation = operation;
    }
    
    
}
