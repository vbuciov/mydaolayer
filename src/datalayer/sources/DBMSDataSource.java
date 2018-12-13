package datalayer.sources;

import datalayer.generators.CommonDCLGenerator;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import datalayer.api.IMediatorDataSource;
import datalayer.api.ISQLExceptionFormatter;

/**
 * Gestiona sessiones, ayuda a manejar errores y liberar recursos asociados. Es
 * una fabrica (Un patron de diseño de POO)
 *
 * @author Victor Manuel Bucio Vargas
 */
public abstract class DBMSDataSource implements IMediatorDataSource
{
    public static int DEFAULT_MAXROW = 0;
    public static int DEFAULT_FETCHSIZE = 0;
    public static int DEFAULT_MAXFIELD = 0;
    private final int DEFAULT_TIMEOUT = 0; //30 Es el default

    private List<Connection> _avalaibles, _useds;
    private List<Statement> StatementPool, otherStatements;
    //private List<String> lastDML; Deprecated
    private String formatString, user_name, password, validation, lastErrorMessage;
    private CommonDCLGenerator dclSintax;
    private ISQLExceptionFormatter formatter;
    
    private int maxRow, maxFetch, maxField;

    public DBMSDataSource(String driver, String connection, String user, String pass, String validationQuery)
    {
        try
        {
            Class.forName(driver);
            //lastDML = new ArrayList<>();
            StatementPool = new ArrayList<>();
            otherStatements = new ArrayList<>();
            _avalaibles = new ArrayList<>();
            _useds = new ArrayList<>();
            formatString = connection;
            user_name = user;
            password = pass;
            validation = validationQuery;
            dclSintax = new CommonDCLGenerator();
        }
        catch (ClassNotFoundException ex)
        {
            lastErrorMessage = ex.getMessage();
            Logger.getLogger(DBMSDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //---------------------------------------------------------------------
    /**
     * Realiza una operación de prueba para iniciar comunicación con el servidor.
     * Nota: El tiempo promedio de la primera conexión son 1.5 segundos.
     */
    @Override
    public boolean Connect ()
    {
        Statement request;
        Connection session;
        boolean affected = false;

        if (null != validation && !validation.isEmpty())
        {
             try
             {
                /*long start, end, res;
                start = System.currentTimeMillis();*/
                session = createSession();
                /*end = System.currentTimeMillis();
                res = end - start;
                System.out.println("Segundos En primera conexion: "+(res/1000.0) % 60 );*/
                //start = System.currentTimeMillis();
                request = session.createStatement();
                affected = request.execute(validation);
                request.close();
                /*end = System.currentTimeMillis();
                res = end - start;
                System.out.println("Segundos En validacion de conexion: "+(res/1000.0) % 60 );*/
                disconnect(session);
             }
             catch (SQLException ex)
             {
                 Logger.getLogger(DBMSDataSource.class.getName()).log(Level.SEVERE, null, ex);
             }
        }

        return affected;
    }

    //---------------------------------------------------------------------
    /**
     * Evalua si la conexion esta activa y gestiona las excepciones
     * relacionadas.
     */
    @Override
    public boolean isConnect(Connection session)
    {
        try
        {
            return session != null && !session.isClosed();
        }
        catch (SQLException ex)
        {
            lastErrorMessage = handleExceptionLog(ex, DBMSDataSource.class.getName());
            return false;
        }
    }

    //---------------------------------------------------------------------
    /**
     * Realiza las acciones necesarias para liberar una session. También
     * devuelve la session utilizada a su estado original.
     */
    @Override
    public void disconnect(Connection session) throws SQLException
    {
        boolean isStillAlive = false;

        isStillAlive = session != null && !session.isClosed();

        if (isStillAlive && !session.getAutoCommit())
        {
            session.commit();
            session.setAutoCommit(true);
        }

        _avalaibles.add(session);
        cleanPool(StatementPool, session);
        cleanPool(otherStatements, session);
    }

    //---------------------------------------------------------------------
    /**
     * Realiza las acciones necesarias ante la ocurrencia de un error. También
     * devuelve la session utilizada a su estado original.
     */
    @Override
    public boolean handleError(Connection session, boolean disconnect)
    {
        boolean isStillAlive = false;

        try
        {
            isStillAlive = session != null && !session.isClosed();

            if (isStillAlive && !session.getAutoCommit())
            {
                session.rollback();
                session.setAutoCommit(true);
            }

            if (disconnect)
            {
                _avalaibles.add(session);
                cleanPool(StatementPool, session);
                cleanPool(otherStatements, session);
            }
        }
        catch (SQLException ex)
        {
            lastErrorMessage = handleExceptionLog(ex, DBMSDataSource.class.getName());
        }

        return isStillAlive;
    }

    //---------------------------------------------------------------------
    @Override
    public String handleExceptionLog(SQLException ex, String source)
    {
        String message = getNestedMessages(ex);
        Logger.getLogger(String.format("%s %s", source, message)).log(Level.SEVERE, null, ex);
        return message;
    }

    //----------------------------------------------------------------
    /**
     * Obtiene todos los mensajes incluidos en las excepciones anidadas en una
     * sola instancia de String.
     */
    private String getNestedMessages(SQLException toHandledValue)
    {
        StringBuilder details = new StringBuilder();
        SQLException current = toHandledValue;

        while (current != null)
        {
            if (details.length() > 0)
                details.append('\n');
            if (formatter != null)
                details.append(formatter.getFriendlyMessage(current));
            else
                details.append(current.getMessage());
            current = current.getNextException();
        }

        return details.toString();
    }

    //---------------------------------------------------------------------
    private void cleanPool(List<Statement> pool, Connection session) throws SQLException
    {
        int i = 0;
        Statement worked;

        while (i < pool.size())
        {
            worked = pool.get(i);

            if (worked.isClosed() || worked.getConnection() == session)
            {
                if (!worked.isClosed())
                    worked.close();

                pool.remove(i);
            }
            else
                i++;
        }
    }

    //---------------------------------------------------------------------
    /**
     * Realiza un cambio de esquema basado en un Comando hacia la base de datos.
     *
     * @param value
     */
    @Override
    public boolean use(String value, Connection session) throws SQLException
    {
        Statement request;
        boolean affected = false;

        if (!value.isEmpty())
        {
            request = session.createStatement();
            affected = !request.execute(dclSintax.formatNotifyDatabase(value));
            request.close();
        }

        return affected;
    }

    //---------------------------------------------------------------------
    /**
     * Crea y relaciona una nueva session con este origen de datos.
     *
     * @throws java.sql.SQLException
     */
    @Override
    public Connection createSession() throws SQLException
    {
        //return createSession(true);
        Connection the_created;

        if (_avalaibles.size() > 0)
            the_created = _avalaibles.remove(_avalaibles.size() - 1); //Pop

        else
            the_created = DriverManager.getConnection(formatString, user_name, password);

        // the_created.setAutoCommit(isAutoCommit);
        _useds.add(the_created);

        return the_created;

    }

    //---------------------------------------------------------------------
    /**
     * Crea y relaciona un comando con este origen de datos.
     *
     * @throws java.sql.SQLException
     */
    @Override
    public Statement createStatement(Connection session) throws SQLException
    {
        Statement comando;

        comando = session.createStatement();
        comando.setQueryTimeout(DEFAULT_TIMEOUT);

        otherStatements.add(comando);

        return comando;
    }
    
    //---------------------------------------------------------------------
    /**
     * Crea y relaciona un comando con este origen de datos.
     *
     * @throws java.sql.SQLException
     */
    @Override
    public Statement createQueryStatement(Connection session) throws SQLException
    {
        Statement comando;

        comando = session.createStatement();
        comando.setQueryTimeout(DEFAULT_TIMEOUT);
        configureQueryStatement(comando);

        otherStatements.add(comando);

        return comando;
    }

    //-------------------------------------------------------------------
    /**
     * Crea y relaciona un comando preparado con este origen de datos
     *
     * @param DML
     * @param session
     * @return
     * @throws java.sql.SQLException
     */
    @Override
    public PreparedStatement prepareStatement(String DML, Connection session) throws SQLException
    {
        return prepareStatement(DML, session, false);
    }

    //-------------------------------------------------------------------
    /**
     * Crea y relaciona un comando con este origen de datos que además se
     * configura para devolver un campo de auto incremento.
     *
     * @param DML
     * @param session
     * @param getAutoID
     * @return
     * @throws java.sql.SQLException
     */
    @Override
    public PreparedStatement prepareStatement(String DML, Connection session, boolean getAutoID) throws SQLException
    {
        PreparedStatement BufferRequest;
        /*boolean seEncontro = false;
      int i = 0;

      while (!seEncontro && i < StatementPool.size())
      {
         seEncontro = StatementPool.get(i) instanceof PreparedStatement
                      && StatementPool.get(i).getConnection() == session
                      && i < lastDML.size() && lastDML.get(i).equals(DML);

         if (!seEncontro)
            i++;
      }

      if (StatementPool.size() > 0 && seEncontro)
      {
         BufferRequest = (PreparedStatement) StatementPool.get(i);
      }
      else
      {
         BufferRequest = getAutoID? session.prepareStatement(DML, Statement.RETURN_GENERATED_KEYS): 
                                    session.prepareStatement(DML);
         
         BufferRequest.setQueryTimeout(timeout);

         StatementPool.add(BufferRequest);
         lastDML.add(DML);
      }*/
        BufferRequest = getAutoID
                ? session.prepareStatement(DML, Statement.RETURN_GENERATED_KEYS)
                : session.prepareStatement(DML);
        BufferRequest.setQueryTimeout(DEFAULT_TIMEOUT);
        StatementPool.add(BufferRequest);

        return BufferRequest;
    }

    //---------------------------------------------------------------------
    @Override
    public PreparedStatement prepareStatement(String DML, Connection session, String[] idColumnsNames) throws SQLException
    {
        PreparedStatement BufferRequest;
        BufferRequest = session.prepareStatement(DML, idColumnsNames);
        BufferRequest.setQueryTimeout(DEFAULT_TIMEOUT);
        StatementPool.add(BufferRequest);

        return BufferRequest;
    }

    //---------------------------------------------------------------------
    @Override
    public PreparedStatement prepareStatement(String DML, Connection session, int[] idColumnsIndexes) throws SQLException
    {
        PreparedStatement BufferRequest;
        BufferRequest = session.prepareStatement(DML, idColumnsIndexes);
        BufferRequest.setQueryTimeout(DEFAULT_TIMEOUT);
        StatementPool.add(BufferRequest);

        return BufferRequest;
    }
    
    
        //-------------------------------------------------------------------
    /**
     * Crea y relaciona un comando con este origen de datos que además se
     * configura para devolver un campo de auto incremento.
     *
     * @param DML
     * @param session
     * @return
     * @throws java.sql.SQLException
     */
    @Override
    public PreparedStatement prepareQueryStatement(String DML, Connection session) throws SQLException
    {
        PreparedStatement BufferRequest;
        BufferRequest = session.prepareStatement(DML);
        BufferRequest.setQueryTimeout(DEFAULT_TIMEOUT);
        configureQueryStatement(BufferRequest);
        StatementPool.add(BufferRequest);

        return BufferRequest;
    }
    
    //--------------------------------------------------------------------
    /**
     * Crea y relaciona un comando de procedimientos con este origen de datos
     *
     * @param DML
     * @param session
     * @return
     * @throws java.sql.SQLException
     */
    @Override
    public CallableStatement prepareCall(String DML, Connection session) throws SQLException
    {
        CallableStatement BufferRequest;
        /*boolean seEncontro = false;
      int i = 0;

      while (!seEncontro && i < StatementPool.size())
      {
         seEncontro = StatementPool.get(i) instanceof CallableStatement
                      && StatementPool.get(i).getConnection() == session
                      && i < lastDML.size()
                      && lastDML.get(i).equals(DML);

         if (!seEncontro)
            i++;
      }

      if (StatementPool.size() > 0 && seEncontro)
      {
         BufferRequest = (CallableStatement) StatementPool.get(i);
      }

      else
      {
         BufferRequest = session.prepareCall(DML);
         BufferRequest.setQueryTimeout(timeout);

         StatementPool.add(BufferRequest);
         lastDML.add(DML);
      }*/

        BufferRequest = session.prepareCall(DML);
        BufferRequest.setQueryTimeout(DEFAULT_TIMEOUT);
        StatementPool.add(BufferRequest);

        return BufferRequest;
    }
    
    
        //--------------------------------------------------------------------
    /**
     * Crea y relaciona un comando de procedimientos con este origen de datos
     *
     * @param DML
     * @param session
     * @return
     * @throws java.sql.SQLException
     */
    @Override
    public CallableStatement prepareQueryCall(String DML, Connection session) throws SQLException
    {
        CallableStatement BufferRequest;

        BufferRequest = session.prepareCall(DML);
        BufferRequest.setQueryTimeout(DEFAULT_TIMEOUT);
        configureQueryStatement(BufferRequest);
        StatementPool.add(BufferRequest);

        return BufferRequest;
    }

    //---------------------------------------------------------------------
    @Override
    public int[] executeQueryBatch(Statement request) throws SQLException
    {
        return request.executeBatch();
    }
    
    //--------------------------------------------------------------------
    public void configureQueryStatement(Statement toConfigure) throws SQLException
    {
       if (maxRow > DEFAULT_MAXROW)
           toConfigure.setMaxRows(maxRow);            
       
       if (maxFetch > DEFAULT_FETCHSIZE)
           toConfigure.setMaxFieldSize(maxFetch);
       
       if (maxField > DEFAULT_MAXFIELD)
           toConfigure.setMaxFieldSize(maxField);
    }

    //---------------------------------------------------------------------
    public ISQLExceptionFormatter getFormatter()
    {
        return formatter;
    }

    //---------------------------------------------------------------------
    public void setFormatter(ISQLExceptionFormatter formatter)
    {
        this.formatter = formatter;
    }

    //--------------------------------------------------------------------
    public String getLastErrorMessage()
    {
        return lastErrorMessage;
    }
    
    //--------------------------------------------------------------------
        public int getMaxRow()
    {
        return maxRow;
    }

    //--------------------------------------------------------------------
    public void setMaxRow(int maxRow)
    {
        this.maxRow = maxRow;
    }

    //--------------------------------------------------------------------
    public int getMaxFetch()
    {
        return maxFetch;
    }

    //--------------------------------------------------------------------
    public void setMaxFetch(int maxFetch)
    {
        this.maxFetch = maxFetch;
    }

    //--------------------------------------------------------------------
    public int getMaxField()
    {
        return maxField;
    }

    //--------------------------------------------------------------------
    public void setMaxField(int maxField)
    {
        this.maxField = maxField;
    }
    
    //---------------------------------------------------------------------
    /*
   Deprecated.- 09-10-2016 - DataAccess can configure the session 
   as it need it.
   @Override
   public Connection createSession(boolean isAutoCommit) throws SQLException
   {
      Connection the_created;

      if (_avalaibles.size() > 0)
         the_created = _avalaibles.remove(_avalaibles.size() - 1); //Pop

      else
         the_created = DriverManager.getConnection(formatString, user_name, password);

      the_created.setAutoCommit(isAutoCommit);
      _useds.add(the_created);

      return the_created;
   }*/
    
        //---------------------------------------------------------------------
    /*  @Override
   public String createCallProcedureFormat(String storedProcedure)
   {
      return dclSintax.formatCallProcedure(storedProcedure);
   }*/
    //---------------------------------------------------------------------
/*   @Override
    public PrintWriter getLogWriter() throws SQLException
    {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}