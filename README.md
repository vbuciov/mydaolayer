# mydaolayer

¿Qué es MyDAOlayer?
MyDAOlayer es una liberia OpenSource que comprende un conjunto de clases en los que contenemos una coleccion de algoritmos muy comunes para ejecutar consultas u operaciones de actualización a base de datos relacionales, mediante SQL e Internamente se utiliza la JDBC API, pero nos ayuda a simplificar nuestro codigo para efectuar este tipo de operaciones. 

Nota: Si estas familiarizado con JDBC Template que viene incluido en el framework de spring quiza te resulte familiar utilizar esta libreria. Sin embargo, ten en cuenta que pese a sus similitudes sus enfoques son distintos, pero esto se entiende con mayor facilidad en cuanto comenzamos a utilizar la libreria.

¿Para qué MyDAOlayer?
Cuando utilizamos JDBC API con el patrón de diseño DAO, surge la necesidad de plantear una arquitectura que nos ayude entre otras cosas a reutilizar código a lo largo de todas las clases dedicadas al acceso a los datos y de reducir el código necesario para crear las mismas, sin embargo, conforme la cantidad de clases en las que utilizamos el patrón comienzan a ser numerosas, nuevos problemas comienzan a surgir.

Entre los problemas más comunes que podemos encontrar tenemos:
1. Existen algoritmos entre clases de acceso a los datos que son tan similares, que podrían ser reutilizados ya sea en el mismo objeto o entre multiples objetos de acceso. (Ejemplo: Operaciones de recuperación de información en los que la única diferencia entre una y otra es el campo por el cual se efectuará la búsqueda).
2. Demasiados pasos para llevar acabo hasta la operación más simple en la base de datos o por lo menos buscamos una mayor simpleza para invocar procedimientos almacenados.
3. Necesidad de optimizar el procesamiento simultaneo de grandes volúmenes de información u operaciones por lotes.
4. Cuando optamos por la incoporación de un framework de persistencia, estos nos impiden interactuar con JDBC de forma directa, e incluso en algunos casos hasta la interación directa con sentencias de SQL. Lo que provoca tanto curvas de aprendizaje para entender los mecanismos de interación de la libreria de terceros, como dejar fuera de nuestro control inmediato la posibilidad de optimizar el desempeño de una consulta por nuestra propia cuenta.
5.- Demasiados bloques de Try - Catch a lo largo de nuestro código.
6. Bases para cambiar con facilidad de un motor de base de datos a otro o por lo menos entre esquemas de la misma base de datos.


El enfoque de MyDAOLayer es el de ser una libreria de apoyo hasta el nivel que nosotros consideremos adecuado. Por lo que podemos desde utilizar los objetos propios de JDBC en una operación de base de datos como dejar que MyDAOLayer se encargue de todos los aspectos de una operación.

¿Niveles de APOYO?
A diferencia de otras librerias de persistencia, tenemos multiples objetos pensados para un diferente nivel de interación con la base de datos y de ayuda hacia nosotros.

Nivel 1 - BasicJDBCUpdateSet: Es el nivel más basico, simplemente nos concede un método SubmitUpdate al cual podemos indicar la/las operacion(es) que queremos ejecutar hacia una fuente de datos pero utilizando estrictamente lenguaje SQL para ello, así como delegando completamente el establecimiento de la conexion, manejo de excepciones, etc.

Nivel 2 - BasicJDBCOperationSet: Incluye las operaciones del Nivel 1, además incluye el método submitQuery que nos permite realizar consultas hacia una fuente de datos y convertir al mismo tiempo convertir el resultado en una collecion de objetos de transferencia, sin embargo la implementación de un algoritmo que nos indique como transformar a objeto cada registro en el resultado es necesario en el método onConvertResultRow.

Nivel 3 - BatchJDBCOperationSet: Incluye todas las operaciones del Nivel 2, además podremos realizar operaciones por lotes, manejar llaves primarias de auto incremento, utilizar sentecias SQL con parametros y podremos utilizar Objetos de transferencia para pasar dichos parametros. Sin embargo, la implementación de un algoritmo que nos indique como transformar cada objeto de transferencia en un parametro es necesario en el método onConvertTransfer.
Nota: Sin duda este es un mejor punto de partida, si lo que buscas es tipear fuertemente las operaciones en tus objetos de acceso a los datos.

Nivel 4 - BatchJDBCCallableOperationSet: Incluye todas las operaciones del Nivel 3, además podremos realizar operaciones de actualización y recuperación de datos utilizando tanto procedimientos almancenados como funciones.
Sin embargo, la implementación de un algoritmo que nos indique como transformar cada objeto de transferencia en un parametro es necesario en el método onConvertTransfer, así como un algoritmo que nos indique como transformar a objeto cada registro en el resultado es necesario en el método onConvertResultRow.

Nivel 5 - BatchJDBCMappeableOperationSet: Incluye todas las operaciones del Nivel 4, pero esta vez además se incluye soporte para generar la sintaxis adecuada para ejecutar un procedimiento Almacenado o Función a partir de un objeto que implemente la interfaz IEntity.

Nivel 6 - BatchJDBCProducerOperationSet: Incluye todas las operaciones del Nivel 5, pero se incluye soporte para que incluso objetos que no implementen la interfaz IEntity, puedan tener una manera de generar sintaxis adecuada para ejecutar tanto operaciones de CRUD como ejecutar procedimientos almacenados y funciones.
Sin embargo, la implementación de un algoritmo que nos indique como transformar cada objeto de transferencia en un parametro es necesario en el método onConvertTransfer, así como un algoritmo que nos indique como transformar a objeto cada registro en el resultado es necesario en el método onConvertResultRow.

Nivel 7 - BootstrapDataAccess: En forma automatica define todas las operaciones CRUD para un objeto de transferencia.

Ejemplos de cómo utilizar MyDAOLayer.

Para utilizar cualquiera de las clases de las que hablamos, debemos heredar de ellas.

--------------------------------------------------------------------------
---	Utilizando BatchJDBCCallableOperationSet	--------------
--------------------------------------------------------------------------	

public abstract class SingletonDataAccess<C> extends BatchJDBCCallableOperationSet<C> implements IDataAccess<C> {
    
    public SingletonDataAccess()
    {
        super(MySQLDataSource.getInstance());
    }
}


public class PersonaDAO extends SingletonDataAccess<Persona>
{
    //---------------------------------------------------------------------
    @Override
    public int Create(Persona nuevo)
    {
       String query = "INSERT INTO personas (nombres) VALUES('" + nuevo.getNombres() + "')";
       return submitUpdate(query);
    }
    
    //---------------------------------------------------------------------
    @Override
    public int Create(List<Persona> toAdds)
    {
        String query = "INSERT INTO personas (nombres) VALUES(?)";  
        return submitUpdate(query, toAdds);
    }

    //---------------------------------------------------------------------
    @Override
    public int Update(Persona toModified)
    {
        String query = "UPDATE personas SET nombres ='"
                + toModified.getNombres()
                + "',apellidos = '" + toModified.getApellidos()
                + "', nacimiento ='" + toModified.getNacimiento()
                + "' WHERE id =" + toModified.getId();
        return submitUpdate(query);
    }

    //---------------------------------------------------------------------
    @Override
    public int Update(List<Persona> toModifieds)
    {
       //Nota: Por default se sobrentiende que para una colección se utiliza una
       //sola transacción. Por lo que es opcional especificar el tercer parametro en true.
        String query = "UPDATE personas SET nombres = ?, apellidos = ?, nacimiento = ? WHERE id = ?";
        return submitUpdate(query, toModifieds, true); 
        
    }

    //---------------------------------------------------------------------
    @Override
    public int Delete(Persona toDelete)
    {
        String query = "DELETE from personas WHERE id = " + toDelete.getId();
        return submitUpdate(query);
    }

    //---------------------------------------------------------------------
    @Override
    public int Delete(List<Persona> toDeleteds)
    {
        String query = "DELETE from personas WHERE id = ?";
      
        return submitUpdate(query, toDeleteds, true);
    }

    //---------------------------------------------------------------------
    @Override 
    public List<Persona> Retrieve()
    {               
       List<Persona> filters = new ArrayList<>(); 
       Persona nothing = new Persona();
       nothing.setId(-1); //Nada en particular
       nothing.setNombres(""); //Nada en particular
       nothing.setApellidos(""); //Nada en particular
       filters.add(nothing);
       
       nothing = new Persona();
       nothing.setId(-1); //Nada en particular
       nothing.setNombres(""); //Nada en particular
       nothing.setApellidos(""); //Nada en particular
       filters.add(nothing);
      
       String query = "{CALL persona_por_tipo(?, ?, ?, ?, ?, ?)}";
       
       return executeReadStoredProcedure(query, filters, true, 1);
    }
    
    //---------------------------------------------------------------------
    public List<Persona> Retrieve(List<Persona> filters)
    {
        List<Persona> results;
        UUID myUUID = UUID.randomUUID();
        String queryPrepare = "INSERT INTO personas_search(nombres, uuid) VALUES (?, '%s')";
        String querySelect = "SELECT nombres FROM personas " +
                " WHERE nombres IN (SELECT nombres FROM personas_search " +
                                   " WHERE uuid='%s')";
        String queryTerminate = "DELETE FROM personas_search where uuid= '%s'";
        
        submitUpdate(String.format(queryPrepare, myUUID.toString()), filters);
        results = submitQuery(String.format(querySelect,  myUUID.toString()));
        submitUpdate(String.format(queryTerminate, myUUID.toString()));
        
        return results;
    }
    
     //---------------------------------------------------------------------
    @Override
    public List<Persona> Retrieve(Persona filter)
    {
        String queryBase = "SELECT id, nombres, apellidos, nacimiento, es_activo FROM personas ";
        String conditions = "WHERE (%d = -1 OR id = ?) AND ('%s' = '' OR nombres = ?)";
        String query = queryBase + String.format(conditions, 
                                                 filter.getId(), 
                                                 filter.getNombres());
        return submitQuery(query, filter);
    }

    //---------------------------------------------------------------------
    @Override
    protected void onPrepareProcedure(ParameteRegisterEvent e) throws SQLException, UnsupportedOperationException
    {
       if (e.getDML().contains("persona_insert")) 
           e.registerOutParameter(3, Types.INTEGER);
    }
        
    //---------------------------------------------------------------------
    @Override
    public void onConvertTransfer(Persona values, DataSetEvent e) throws SQLException, UnsupportedOperationException
    {   
        if (e.getDML().contains("persona_por_tipo"))
        {
            e.setInt(1, values.getId());
            e.setString(2, values.getNombres());
            e.setString(3, values.getApellidos());
            e.setBoolean(4, true);
            e.setBoolean(5, true);
            e.setBoolean(6, true);
        }
        
        else if (e.getDML().contains("bitacora_insert"))
        {
            e.setString(1, values.getNombres());
        }
        
        else if (e.getDML().contains("SELECT"))
        {
            e.setInt(1, values.getId());
            e.setString(2, values.getNombres());
        }
        
        else  if (e.getDML().contains("INSERT"))
            e.setString(1, values.getNombres());

        else if (e.getDML().contains("UPDATE"))
        {
            e.setString(1, values.getNombres());
            e.setString(2, values.getApellidos());
            e.setDate(3, values.getNacimiento());
            e.setInt(4, values.getId());
        }

        else
            e.setInt(1, values.getId());
    }

    //---------------------------------------------------------------------
    @Override
    public Persona onConvertResultRow(DataGetEvent e) throws SQLException, UnsupportedOperationException
    {
        Persona actual = new Persona();
        
        for (int i = 1; i <= e.getColumnCount(); i++)
        {
            switch (e.getColumnName(i))
            {
                case "id":
                    actual.setId(e.getInt(i));
                    break;
                    
                case "nombres":
                    actual.setNombres(e.getString(i));
                    break;
                    
                case "apellidos":
                    actual.setApellidos(e.getString(i));
                    break;
                    
                case "nacimiento":
                    actual.setNacimiento(e.getDate(i));
                    break;
            }
        }

        return actual;
    }
}


--------------------------------------------------------------------------
---	Utilizando BootstrapDataAccess	--------------
--------------------------------------------------------------------------	


/**
 * En este trabajo se requiere que todos los dataAccess Compartan conexión al
 * mismo server.
 *
 * @author Victor Manuel Bucio Vargas
 * @param <T> El tipo de dato con el que se realizara el acceso a datos
 */
public abstract class SingletonDataAccess<T extends Entidad> extends BootstrapDataAccess<T>
{

    //--------------------------------------------------------------------
    /**
     * Genera las operaciones basicas CRUD para la tabla especificada.
     * Nota: obtiene el origen de los datos de forma automatica.
     *
     * @param tableName Tabla especificada
     */
    public SingletonDataAccess(String tableName)
    {
        //super (FilesBase.getInstance());
        super(DataSourceManager.getMainDataSourceInstance(), tableName);
    }

    //--------------------------------------------------------------------
    /**
     * Genera las operaciones basicas CRUD para la tabla especificada. 
     * Nota 1: Sin embargo solo para la proyeccion especificada.
     * Nota 2: obtiene el origen de los datos de forma automatica.
     * @param tableName Tabla especificada
     * @param columns Proyeccion.
     */
    public SingletonDataAccess(String tableName, String... columns)
    {
        super(DataSourceManager.getMainDataSourceInstance(), tableName, columns);
    }

    //--------------------------------------------------------------------
    /**
     * Genera las operaciones basicas CRUD para la tabla especificada. 
     * Nota 1: Permite especificar cuales columnas son la llave primaria, sin embargo,
     * no todos los motores de base de datos permiten este manejo por lo que hay
     * que remitirnos a la documentacion del motor en cuestión.
     * Nota 2: obtiene el origen de los datos de forma automatica.
     * @param tableName Tabla especificada
     * @param idColumnsNames Nombres de las columnas llave
     * @param columns Proyeccion.
     */
    public SingletonDataAccess(String tableName, String[] idColumnsNames, String... columns)
    {
        super(DataSourceManager.getMainDataSourceInstance(), tableName, idColumnsNames, columns);
    }

    //--------------------------------------------------------------------
    /**
     * Genera las operaciones basicas CRUD para la tabla especificada. 
     * Nota 1: Permite especificar cuales columnas son la llave primaria por indice, sin
     * embargo, no todos los motores de base de datos permiten este manejo por
     * lo que hay que remitirnos a la documentacion del motor en cuestión.
     * Nota 2: obtiene el origen de los datos de forma automatica.
     * @param tableName Tabla especificada
     * @param idColumnsIndexes indices
     * @param columns Proyeccion.
     */
    public SingletonDataAccess(String tableName, int[] idColumnsIndexes, String... columns)
    {
        super(DataSourceManager.getMainDataSourceInstance(), tableName, idColumnsIndexes, columns);
    }
}



public class BienesDataAccess extends SingletonDataAccess<Bien>
{
    private final String insertProcedure = "bien_insert(?,?,?)";

    //--------------------------------------------------------------------
    public BienesDataAccess()
    {
        super("bienes", "id", "nombre", "es_tipo", "id_categoria");
        setInsertProcedure(insertProcedure);
    }

    //---------------------------------------------------------------------
    /**
     * InsertProcedure is using an executeUpdateStoredProcedure
     *
     * @throws java.sql.SQLException
     */
    @Override
    protected void onConvertTransfer(Bien values, DataSetEvent e) throws SQLException, UnsupportedOperationException
    {
        if (e.getDML() == insertProcedure)
        {
            e.setString(1, values.getNombre(), 45);//"vnombre"
            e.setInt(2, values.getEsTipo());//"ves_tipo"
            e.setInt(3, values.getIdCategoria());//"vid_categoria"
        }
    }

    //--------------------------------------------------------------------    
    @Override
    protected void onConvertKeyResult(DataGetEvent e, Bien value) throws SQLException, UnsupportedOperationException
    {
        if (e.getDML() == insertProcedure)
        {
            for (int i = 1; i <= e.getColumnCount(); i++)
            {
                switch (e.getColumnName(i))
                {
                    case "id":
                        value.setId(e.getInt(i));
                        break;
                    case "nombre":
                        value.setNombre(e.getString(i));
                        break;
                    case "es_tipo":
                        value.setEsTipo(e.getInt(i));
                        break;
                    case "id_categoria":
                        value.setIdCategoria(e.getInt(i));
                        break;
                }
            }
            /*else
            if (e.getColumnCount() > 0)
                value.setId(e.getInt(1));*/
        }
    }


    //--------------------------------------------------------------------
    /**
     * Este método sirve para convertir un objeto a valores de transferencia.
     *
     * @param values Es el objeto base.
     */
    @Override
    public void onSendValues(DataSendToQueryEvent e, Bien values)
    {
        if (values.getId() != Bien.EMPTY_INT)
        {

        if (e.isCreateOperation())
            e.addInteger("id", values.getId());

        else if (e.isUpdateOperation())
        {
            e.addInteger("id", values.getId());
            e.addInteger("id", QueryParameter.Operator.EQUAL, values.getId_Viejo());
        }

        else
            e.addInteger("id", QueryParameter.Operator.EQUAL, values.getId_Viejo());
        }

        if (!values.getNombre().equals(Bien.EMPTY_STRING))
        {
            if (e.isCreateOperation() || e.isUpdateOperation())
                e.addString("nombre", values.getNombre(), 45);

            else if (!values.isSearchOnlyByPrimaryKey()) //Busca por la llave primaria unicamente.
                e.addString("nombre", QueryParameter.Operator.EQUAL, values.getNombre());
        }

        if (values.getEsTipo() != Bien.EMPTY_INT)
        {
            if (e.isCreateOperation() || e.isUpdateOperation())
                e.addInteger("es_tipo", values.getEsTipo());

            else if (!values.isSearchOnlyByPrimaryKey()) //Busca por la llave primaria unicamente.
                e.addInteger("es_tipo", QueryParameter.Operator.EQUAL, values.getEsTipo());
        }

        if (values.getIdCategoria() != Bien.EMPTY_INT)
        {
            if (e.isCreateOperation() || e.isUpdateOperation())
                e.addInteger("id_categoria", values.getIdCategoria());

            else if (!values.isSearchOnlyByPrimaryKey()) //Busca por la llave primaria unicamente.
                e.addInteger("id_categoria", QueryParameter.Operator.EQUAL, values.getIdCategoria());
        }
    }
    
    //--------------------------------------------------------------------
    @Override
    public Bien onConvertResultRow(DataGetEvent e) throws SQLException, UnsupportedOperationException
    {
        Bien current = new Bien();

        for (int i = 1; i <= e.getColumnCount(); i++)
        {
            switch (e.getColumnName(i))
            {
                case "id":
                    current.setId(e.getInt(i));
                    break;
                case "nombre":
                    current.setNombre(e.getString(i));
                    break;
                case "es_tipo":
                    current.setEsTipo(e.getInt(i));
                    break;
                case "id_categoria":
                    current.setIdCategoria(e.getInt(i));
                    break;
            }
        }

        return current;

    }
