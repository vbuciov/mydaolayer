/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer.utils;

/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public class UnionRelation extends Relation {
    
    Relation[] anothers;
    
    public UnionRelation (String name, Relation[] anothersRelations)
    {
        super(name);
        anothers = anothersRelations;
    }
    
    public UnionRelation (String name, String[] proyection, Relation[] anothersRelations)
    {
        super(name, proyection);
        anothers = anothersRelations;
    }

    public Relation[] getAnothers() {
        return anothers;
    }

    public void setAnothers(Relation[] anothers) {
        this.anothers = anothers;
    }
}
