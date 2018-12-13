package datalayer.utils;

/**
 * Representa un producto entre relaciones CARTESIANO o JOINS. (Algebra relacional)
 * @author Victor Manuel Bucio Vargas
 */
public class ProductRelation extends Relation {
    
    Join[] anothers;
    
    //--------------------------------------------------------------------
    /**
     * Realiza el producto cartesiano de las 2 relaciones.
     * @param storeObjectA
     * @param StoreObjectB
     */
    public ProductRelation (String storeObjectA, String StoreObjectB)
    {
        super (storeObjectA);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB);
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el producto cartesiano de las 2 relaciones, y nos permite especificar la proyección.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Proyection
     */
    public ProductRelation (String storeObjectA, String StoreObjectB, String[] Proyection)
    {
        super (storeObjectA, Proyection);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB);
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el producto cartesiano de las 2 relaciones, y nos permite especificar la proyección.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Proyection
     * @param Type 
     * @param columnsOptions 
     */
    public ProductRelation (String storeObjectA, String StoreObjectB, String[] Proyection, RelationOptions.TypeOption Type, String[] columnsOptions)
    {
        super (storeObjectA, Proyection, Type, columnsOptions);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB);
    }
    
    //--------------------------------------------------------------------
     /**
     * Realiza el producto cartesiano de las 2 relaciones, y nos permite especificar la proyección.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Proyection
     * @param groupby
     * @param orderby
     */
    public ProductRelation (String storeObjectA, String StoreObjectB, String[] Proyection, String[] groupby, String[] orderby)
    {
        super (storeObjectA, Proyection, groupby, orderby);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB);
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de las 2 relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Option
     */
    public ProductRelation (String storeObjectA, String StoreObjectB, String Conditional)
    {
        super (storeObjectA);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB, Conditional);
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de las 2 relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Option
     * @param Proyection
     */
    public ProductRelation (String storeObjectA, String StoreObjectB, String Conditional, String[] Proyection)
    {
        super (storeObjectA, Proyection);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB, Conditional);
    }
    
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de las 2 relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Option
     * @param Proyection
     */
    public ProductRelation (String storeObjectA, String StoreObjectB, String Conditional, String[] Proyection, RelationOptions.TypeOption Type, String[] columnsOptions)
    {
        super (storeObjectA, Proyection, Type, columnsOptions);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB, Conditional);
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de las 2 relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Option
     * @param Proyection
     */
    public ProductRelation (String storeObjectA, String StoreObjectB, String Conditional, String[] Proyection, String[] groupby, String[] orderby)
    {
        super (storeObjectA, Proyection, groupby, orderby);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB, Conditional);
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de las 2 relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Option
     */
    public ProductRelation (String storeObjectA, Join.joinType Option, String StoreObjectB, String Conditional)
    {
        super (storeObjectA);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB, Conditional, Option);
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de las 2 relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     * @param Option
     */
    public ProductRelation (String storeObjectA, Join.joinType Option, String StoreObjectB, String Conditional, String[] Proyection)
    {
        super (storeObjectA, Proyection);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB, Conditional, Option);
    }
    
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de las 2 relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     * @param Option
     */
    public ProductRelation (String storeObjectA, Join.joinType Option, String StoreObjectB, String Conditional, String[] Proyection, RelationOptions.TypeOption Type, String[] columnsOptions)
    {
        super (storeObjectA, Proyection, Type, columnsOptions);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB, Conditional, Option);
    }
    
    
        //--------------------------------------------------------------------
    /**
     * Realiza el Join de las 2 relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     * @param Option
     */
    public ProductRelation (String storeObjectA, Join.joinType Option, String StoreObjectB, String Conditional, String[] Proyection, String[] groupby, String[] orderby)
    {
        super (storeObjectA, Proyection, groupby, orderby);
        anothers = new Join[1];
        anothers[0] = new Join(StoreObjectB, Conditional, Option);
    }
    

     //--------------------------------------------------------------------
    /**
     * Realiza el Join de relación con el producto de relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     */
    public ProductRelation (String storeObjectA, ProductRelation StoreObjectB, String Conditional)
    {
        super (storeObjectA);
        Join[] temp = StoreObjectB.getAnothers();
        anothers = new Join[temp.length + 1];
        anothers[0] = new Join(StoreObjectB.getName(), Conditional);
        for (int i = 0; i<temp.length; i++)
            anothers[i + 1] = temp[i];
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de relación con el producto de relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     */
    public ProductRelation (String storeObjectA, ProductRelation StoreObjectB, String Conditional, String[] Proyection)
    {
        super (storeObjectA, Proyection);
        Join[] temp = StoreObjectB.getAnothers();
        anothers = new Join[temp.length + 1];
        anothers[0] = new Join(StoreObjectB.getName(), Conditional);
        for (int i = 0; i<temp.length; i++)
            anothers[i + 1] = temp[i];
    }
    
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de relación con el producto de relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     */
    public ProductRelation (String storeObjectA, ProductRelation StoreObjectB, String Conditional, String[] Proyection, RelationOptions.TypeOption Type, String[] columnsOptions)
    {
        super (storeObjectA, Proyection, Type, columnsOptions);
        Join[] temp = StoreObjectB.getAnothers();
        anothers = new Join[temp.length + 1];
        anothers[0] = new Join(StoreObjectB.getName(), Conditional);
        for (int i = 0; i<temp.length; i++)
            anothers[i + 1] = temp[i];
    }
    
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de relación con el producto de relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     */
    public ProductRelation (String storeObjectA, ProductRelation StoreObjectB, String Conditional, String[] Proyection, String[] groupby, String[] orderby)
    {
        super (storeObjectA, Proyection, groupby, orderby);
        Join[] temp = StoreObjectB.getAnothers();
        anothers = new Join[temp.length + 1];
        anothers[0] = new Join(StoreObjectB.getName(), Conditional);
        for (int i = 0; i<temp.length; i++)
            anothers[i + 1] = temp[i];
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de relación con el producto de relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Option
     */
    public ProductRelation (String storeObjectA, Join.joinType Option, ProductRelation StoreObjectB, String Conditional)
    {
        super (storeObjectA);
        Join[] temp = StoreObjectB.getAnothers();
        anothers = new Join[temp.length + 1];
        anothers[0] = new Join(StoreObjectB.getName(), Conditional, Option);
        for (int i = 0; i<temp.length; i++)
            anothers[i + 1] = temp[i];
    }
    
    
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de relación con el producto de relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     * @param Option
     */
    public ProductRelation (String storeObjectA, Join.joinType Option, ProductRelation StoreObjectB, String Conditional, String[] Proyection)
    {
        super (storeObjectA, Proyection);
        Join[] temp = StoreObjectB.getAnothers();
        anothers = new Join[temp.length + 1];
        anothers[0] = new Join(StoreObjectB.getName(), Conditional, Option);
        for (int i = 0; i<temp.length; i++)
            anothers[i + 1] = temp[i];
    }
        
    //--------------------------------------------------------------------
    /**
     * Realiza el Join de relación con el producto de relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     * @param Option
     */
    public ProductRelation (String storeObjectA, Join.joinType Option, ProductRelation StoreObjectB, String Conditional, String[] Proyection, RelationOptions.TypeOption Type, String[] columnsOptions)
    {
        super (storeObjectA, Proyection, Type, columnsOptions);
        Join[] temp = StoreObjectB.getAnothers();
        anothers = new Join[temp.length + 1];
        anothers[0] = new Join(StoreObjectB.getName(), Conditional, Option);
        for (int i = 0; i<temp.length; i++)
            anothers[i + 1] = temp[i];
    }
    
    
        //--------------------------------------------------------------------
    /**
     * Realiza el Join de relación con el producto de relaciones según la condición.
     * @param storeObjectA
     * @param StoreObjectB
     * @param Conditional
     * @param Proyection
     * @param Option
     */
    public ProductRelation (String storeObjectA, Join.joinType Option, ProductRelation StoreObjectB, String Conditional, String[] Proyection, String[] groupby, String[] orderby)
    {
        super (storeObjectA, Proyection, groupby, orderby);
        Join[] temp = StoreObjectB.getAnothers();
        anothers = new Join[temp.length + 1];
        anothers[0] = new Join(StoreObjectB.getName(), Conditional, Option);
        for (int i = 0; i<temp.length; i++)
            anothers[i + 1] = temp[i];
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el producto con todas las relaciones según la condición de cada una.
     * @param name
     * @param anothersRelations
     */
    public ProductRelation (String name, Join[] anothersRelations)
    {
        super (name);
        anothers = anothersRelations;
    }
    
    //--------------------------------------------------------------------
    /**
     * Realiza el producto con todas las relaciones según la condición de cada una.
     * @param name
     * @param anothersRelations
     * @param proyection
     */
    public ProductRelation (String name, Join[] anothersRelations, String[] proyection)
    {
        super (name, proyection);
        anothers = anothersRelations;
    }

        //--------------------------------------------------------------------
    /**
     * Realiza el producto con todas las relaciones según la condición de cada una.
     * @param name
     * @param anothersRelations
     * @param proyection
     */
    public ProductRelation (String name, Join[] anothersRelations, String[] proyection, RelationOptions.TypeOption Type, String[] columnsOptions)
    {
        super (name, proyection, Type, columnsOptions);
        anothers = anothersRelations;
    }
    
    
    //--------------------------------------------------------------------
    /**
     * Obtiene todas las relaciones con las que se esta efectuando el producto
     * @return
     */
    public Join[] getAnothers() {
        return anothers;
    }
    
    //--------------------------------------------------------------------
    public String getJoinName()
    {
        StringBuilder stores = new StringBuilder();
        stores.append(getName());
        for (Join another : anothers)
        {
            if (stores.length() > 0)
            {
                if (another.getOption() == Join.joinType.RIGHT_OUTER)
                    stores.append(" X] ");

                else if (another.getOption() == Join.joinType.LEFT_OUTER)
                    stores.append(" [X ");

                else if (another.getOption() == Join.joinType.CROSS)
                    stores.append(" X ");

                else if (another.getOption() == Join.joinType.NATURAL)
                    stores.append(" [X] ");

                else if (another.getOption() == Join.joinType.INNER)
                    stores.append(" |X| ");
            }
            stores.append(another.getStoreObject());
        }

        return stores.toString();
    }
}
