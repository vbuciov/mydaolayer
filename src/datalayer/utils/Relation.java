/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer.utils;

/**
 * Almacena la estructura de una proyección. (Algebra relacional)
 * @author Victor Manuel Bucio Vargas
 */
public class Relation {
    String name;
    String[] columns;
    RelationOptions[] options;
    
    public Relation (String pname)
    {
        name = pname;
    }
    
    public Relation (String name, String[] proyection)
    {
        this.name = name;
        this.columns =  proyection;
    }
    
     public Relation (String name, String[] proyection, RelationOptions.TypeOption Type, String[] columnsOptions )
    {
        this.name = name;
        this.columns =  proyection;
        this.options = new RelationOptions[1];
        this.options[0] = new RelationOptions(columnsOptions, Type);
    }
    
    public Relation (String name, String[] proyection, String[] groupBy, String[] orderBy)
    {
        this.name = name;
        this.columns =  proyection;
        this.options = new RelationOptions[2];
        this.options[0] = new RelationOptions(groupBy, RelationOptions.TypeOption.GROUP_BY);
        this.options[1] = new RelationOptions(orderBy, RelationOptions.TypeOption.ORDER_BY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getProyection() {
        return columns;
    }

    public void setProyection(String... proyection) {
        this.columns = proyection;
    }

    public RelationOptions[] getOptions() {
        return options;
    }

    public void setOptions(RelationOptions[] options) {
        this.options = options;
    }
    
    public void setOrderBy(String... columns)
    {
        if (null == options)
           this.options = new RelationOptions[1];
        this.options[0] = new RelationOptions(columns, RelationOptions.TypeOption.ORDER_BY);
    }
    
    public boolean hasProyection ()
    {
        return columns != null;
    }
    
    public boolean hasOptions ()
    {
        return options != null;
    }
}
