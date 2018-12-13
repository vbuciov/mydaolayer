package datalayer.wrappers;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.Calendar;

/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public abstract class ParameterCollectionReader
{
    private final CallableStatement decorate;
    
    public ParameterCollectionReader(CallableStatement theCallable)
    {
         decorate = theCallable;
    }
    
      /**
     * Reports whether the last column read had a value of SQL
     * <code>NULL</code>. Note that you must first call one of the getter
     * methods on a column to try to read its value and then call the method
     * <code>wasNull</code> to see if the value read was SQL <code>NULL</code>.
     *
     * @return <code>true</code> if the last column value read was SQL
     * <code>NULL</code> and <code>false</code> otherwise
     * @exception SQLException if a database access error occurs or this method
     * is called on a closed result set
     */
    public boolean wasNull() throws SQLException
    {
        return decorate.wasNull();
    }

    // Methods for accessing results by column index
    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>String</code> in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public String getString(int columnIndex) throws SQLException
    {
        return decorate.getString(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>boolean</code> in the Java
     * programming language.
     *
     * <P>
     * If the designated column has a datatype of CHAR or VARCHAR and contains a
     * "0" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and
     * contains a 0, a value of <code>false</code> is returned. If the
     * designated column has a datatype of CHAR or VARCHAR and contains a "1" or
     * has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains
     * a 1, a value of <code>true</code> is returned.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>false</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public boolean getBoolean(int columnIndex) throws SQLException
    {
        return decorate.getBoolean(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>byte</code> in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public byte getByte(int columnIndex) throws SQLException
    {
        return decorate.getByte(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>short</code> in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public short getShort(int columnIndex) throws SQLException
    {
        return decorate.getShort(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>int</code> in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public int getInt(int columnIndex) throws SQLException
    {
        return decorate.getInt(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>long</code> in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public long getLong(int columnIndex) throws SQLException
    {
        return decorate.getLong(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>float</code> in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public float getFloat(int columnIndex) throws SQLException
    {
        return decorate.getFloat(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>double</code> in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public double getDouble(int columnIndex) throws SQLException
    {
        return decorate.getDouble(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.BigDecimal</code> in
     * the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param scale the number of digits to the right of the decimal point
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @deprecated Use {@code getBigDecimal(int columnIndex)} or
     * {@code getBigDecimal(String columnLabel)}
     */
    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException
    {
        return decorate.getBigDecimal(columnIndex, scale);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>byte</code> array in the Java
     * programming language. The bytes represent the raw values returned by the
     * driver.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public byte[] getBytes(int columnIndex) throws SQLException
    {
        return decorate.getBytes(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
     * the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public java.sql.Date getDate(int columnIndex) throws SQLException
    {
        return decorate.getDate(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
     * the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public java.sql.Time getTime(int columnIndex) throws SQLException
    {
        return decorate.getTime(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
     * in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public java.sql.Timestamp getTimestamp(int columnIndex) throws SQLException
    {
        return decorate.getTimestamp(columnIndex);
    }


    // Methods for accessing results by column label
    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>String</code> in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public String getString(String columnLabel) throws SQLException
    {
        return decorate.getString(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>boolean</code> in the Java
     * programming language.
     *
     * <P>
     * If the designated column has a datatype of CHAR or VARCHAR and contains a
     * "0" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and
     * contains a 0, a value of <code>false</code> is returned. If the
     * designated column has a datatype of CHAR or VARCHAR and contains a "1" or
     * has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains
     * a 1, a value of <code>true</code> is returned.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>false</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public boolean getBoolean(String columnLabel) throws SQLException
    {
        return decorate.getBoolean(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>byte</code> in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public byte getByte(String columnLabel) throws SQLException
    {
        return decorate.getByte(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>short</code> in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public short getShort(String columnLabel) throws SQLException
    {
        return decorate.getShort(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>int</code> in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public int getInt(String columnLabel) throws SQLException
    {
        return decorate.getInt(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>long</code> in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public long getLong(String columnLabel) throws SQLException
    {
        return decorate.getLong(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>float</code> in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public float getFloat(String columnLabel) throws SQLException
    {
        return decorate.getFloat(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>double</code> in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public double getDouble(String columnLabel) throws SQLException
    {
        return decorate.getDouble(columnLabel);
    }


    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>byte</code> array in the Java
     * programming language. The bytes represent the raw values returned by the
     * driver.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public byte[] getBytes(String columnLabel) throws SQLException
    {
        return decorate.getBytes(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
     * the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public java.sql.Date getDate(String columnLabel) throws SQLException
    {
        return decorate.getDate(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
     * the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public java.sql.Time getTime(String columnLabel) throws SQLException
    {
        return decorate.getTime(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
     * in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public java.sql.Timestamp getTimestamp(String columnLabel) throws SQLException
    {
        return decorate.getTimestamp(columnLabel);
    }

    /**
     * <p>
     * Gets the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language.
     *
     * <p>
     * This method will return the value of the given column as a Java object.
     * The type of the Java object will be the default Java object type
     * corresponding to the column's SQL type, following the mapping for
     * built-in types specified in the JDBC specification. If the value is an
     * SQL <code>NULL</code>, the driver returns a Java <code>null</code>.
     *
     * <p>
     * This method may also be used to read database-specific abstract data
     * types.
     *
     * In the JDBC 2.0 API, the behavior of method <code>getObject</code> is
     * extended to materialize data of SQL user-defined types.
     * <p>
     * If <code>Connection.getTypeMap</code> does not throw a
     * <code>SQLFeatureNotSupportedException</code>, then when a column contains
     * a structured or distinct value, the behavior of this method is as if it
     * were a call to: <code>getObject(columnIndex,
     * this.getStatement().getConnection().getTypeMap())</code>.
     *
     * If <code>Connection.getTypeMap</code> does throw a
     * <code>SQLFeatureNotSupportedException</code>, then structured values are
     * not supported, and distinct values are mapped to the default Java class
     * as determined by the underlying SQL type of the DISTINCT type.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>java.lang.Object</code> holding the column value
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public Object getObject(int columnIndex) throws SQLException
    {
        return decorate.getObject(columnIndex);
    }

    /**
     * <p>
     * Gets the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language.
     *
     * <p>
     * This method will return the value of the given column as a Java object.
     * The type of the Java object will be the default Java object type
     * corresponding to the column's SQL type, following the mapping for
     * built-in types specified in the JDBC specification. If the value is an
     * SQL <code>NULL</code>, the driver returns a Java <code>null</code>.
     * <P>
     * This method may also be used to read database-specific abstract data
     * types.
     * <P>
     * In the JDBC 2.0 API, the behavior of the method <code>getObject</code> is
     * extended to materialize data of SQL user-defined types. When a column
     * contains a structured or distinct value, the behavior of this method is
     * as if it were a call to: <code>getObject(columnIndex,
     * this.getStatement().getConnection().getTypeMap())</code>.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>java.lang.Object</code> holding the column value
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public Object getObject(String columnLabel) throws SQLException
    {
        return decorate.getObject(columnLabel);
    }

    /**
     * <p>
     * Gets the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language.
     *
     * <p>
     * This method will return the value of the given column as a Java object.
     * The type of the Java object will be the default Java object type
     * corresponding to the column's SQL type, following the mapping for
     * built-in types specified in the JDBC specification. If the value is an
     * SQL <code>NULL</code>, the driver returns a Java <code>null</code>.
     *
     * <p>
     * This method may also be used to read database-specific abstract data
     * types.
     *
     * In the JDBC 2.0 API, the behavior of method <code>getObject</code> is
     * extended to materialize data of SQL user-defined types.
     * <p>
     * If <code>Connection.getTypeMap</code> does not throw a
     * <code>SQLFeatureNotSupportedException</code>, then when a column contains
     * a structured or distinct value, the behavior of this method is as if it
     * were a call to: <code>getObject(columnIndex,
     * this.getStatement().getConnection().getTypeMap())</code>.
     *
     * If <code>Connection.getTypeMap</code> does throw a
     * <code>SQLFeatureNotSupportedException</code>, then structured values are
     * not supported, and distinct values are mapped to the default Java class
     * as determined by the underlying SQL type of the DISTINCT type.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>java.lang.Object</code> holding the column value
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public boolean isDBNull(int columnIndex) throws SQLException
    {
        return decorate.getObject(columnIndex) != null;
    }

    /**
     * <p>
     * Gets the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language.
     *
     * <p>
     * This method will return the value of the given column as a Java object.
     * The type of the Java object will be the default Java object type
     * corresponding to the column's SQL type, following the mapping for
     * built-in types specified in the JDBC specification. If the value is an
     * SQL <code>NULL</code>, the driver returns a Java <code>null</code>.
     * <P>
     * This method may also be used to read database-specific abstract data
     * types.
     * <P>
     * In the JDBC 2.0 API, the behavior of the method <code>getObject</code> is
     * extended to materialize data of SQL user-defined types. When a column
     * contains a structured or distinct value, the behavior of this method is
     * as if it were a call to: <code>getObject(columnIndex,
     * this.getStatement().getConnection().getTypeMap())</code>.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>java.lang.Object</code> holding the column value
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     */
    public boolean isDBNull(String columnLabel) throws SQLException
    {
        return decorate.getObject(columnLabel) != null;
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.io.Reader</code> object.
     *
     * @return a <code>java.io.Reader</code> object that contains the column
     * value; if the value is SQL <code>NULL</code>, the value returned is
     * <code>null</code> in the Java programming language.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public java.io.Reader getCharacterStream(int columnIndex) throws SQLException
    {
        return decorate.getCharacterStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.io.Reader</code> object.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>java.io.Reader</code> object that contains the column
     * value; if the value is SQL <code>NULL</code>, the value returned is
     * <code>null</code> in the Java programming language
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public java.io.Reader getCharacterStream(String columnLabel) throws SQLException
    {
        return decorate.getCharacterStream(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code> with
     * full precision.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value (full precision); if the value is SQL
     * <code>NULL</code>, the value returned is <code>null</code> in the Java
     * programming language.
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {
        return decorate.getBigDecimal(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code> with
     * full precision.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value (full precision); if the value is SQL
     * <code>NULL</code>, the value returned is <code>null</code> in the Java
     * programming language.
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     *
     */
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException
    {
        return decorate.getBigDecimal(columnLabel);
    }



    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language. If the value is an SQL <code>NULL</code>, the
     * driver returns a Java <code>null</code>. This method uses the given
     * <code>Map</code> object for the custom mapping of the SQL structured or
     * distinct type that is being retrieved.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param map a <code>java.util.Map</code> object that contains the mapping
     * from SQL type names to classes in the Java programming language
     * @return an <code>Object</code> in the Java programming language
     * representing the SQL value
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Object getObject(int columnIndex, java.util.Map<String, Class<?>> map)
            throws SQLException
    {
        return decorate.getObject(columnIndex, map);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Ref</code> object in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>Ref</code> object representing an SQL <code>REF</code>
     * value
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Ref getRef(int columnIndex) throws SQLException
    {
        return decorate.getRef(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Blob</code> object in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>Blob</code> object representing the SQL <code>BLOB</code>
     * value in the specified column
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Blob getBlob(int columnIndex) throws SQLException
    {
        return decorate.getBlob(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Clob</code> object in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>Clob</code> object representing the SQL <code>CLOB</code>
     * value in the specified column
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Clob getClob(int columnIndex) throws SQLException
    {
        return decorate.getClob(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Array</code> object in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return an <code>Array</code> object representing the SQL
     * <code>ARRAY</code> value in the specified column
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Array getArray(int columnIndex) throws SQLException
    {
        return decorate.getArray(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Object</code> in the Java
     * programming language. If the value is an SQL <code>NULL</code>, the
     * driver returns a Java <code>null</code>. This method uses the specified
     * <code>Map</code> object for custom mapping if appropriate.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @param map a <code>java.util.Map</code> object that contains the mapping
     * from SQL type names to classes in the Java programming language
     * @return an <code>Object</code> representing the SQL value in the
     * specified column
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Object getObject(String columnLabel, java.util.Map<String, Class<?>> map)
            throws SQLException
    {
        return decorate.getObject(columnLabel, map);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Ref</code> object in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>Ref</code> object representing the SQL <code>REF</code>
     * value in the specified column
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Ref getRef(String columnLabel) throws SQLException
    {
        return decorate.getRef(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Blob</code> object in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>Blob</code> object representing the SQL <code>BLOB</code>
     * value in the specified column
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Blob getBlob(String columnLabel) throws SQLException
    {
        return decorate.getBlob(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>Clob</code> object in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>Clob</code> object representing the SQL <code>CLOB</code>
     * value in the specified column
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Clob getClob(String columnLabel) throws SQLException
    {
        return decorate.getClob(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as an <code>Array</code> object in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return an <code>Array</code> object representing the SQL
     * <code>ARRAY</code> value in the specified column
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.2
     */
    public Array getArray(String columnLabel) throws SQLException
    {
        return decorate.getArray(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
     * the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the date if the underlying
     * database does not store timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal the <code>java.util.Calendar</code> object to use in
     * constructing the date
     * @return the column value as a <code>java.sql.Date</code> object; if the
     * value is SQL <code>NULL</code>, the value returned is <code>null</code>
     * in the Java programming language
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public java.sql.Date getDate(int columnIndex, Calendar cal) throws SQLException
    {
        return decorate.getDate(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
     * the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the date if the underlying
     * database does not store timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @param cal the <code>java.util.Calendar</code> object to use in
     * constructing the date
     * @return the column value as a <code>java.sql.Date</code> object; if the
     * value is SQL <code>NULL</code>, the value returned is <code>null</code>
     * in the Java programming language
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public java.sql.Date getDate(String columnLabel, Calendar cal) throws SQLException
    {
        return decorate.getDate(columnLabel, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
     * the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the time if the underlying
     * database does not store timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal the <code>java.util.Calendar</code> object to use in
     * constructing the time
     * @return the column value as a <code>java.sql.Time</code> object; if the
     * value is SQL <code>NULL</code>, the value returned is <code>null</code>
     * in the Java programming language
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public java.sql.Time getTime(int columnIndex, Calendar cal) throws SQLException
    {
        return decorate.getTime(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
     * the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the time if the underlying
     * database does not store timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @param cal the <code>java.util.Calendar</code> object to use in
     * constructing the time
     * @return the column value as a <code>java.sql.Time</code> object; if the
     * value is SQL <code>NULL</code>, the value returned is <code>null</code>
     * in the Java programming language
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public java.sql.Time getTime(String columnLabel, Calendar cal) throws SQLException
    {
        return decorate.getTime(columnLabel, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
     * in the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the timestamp if the
     * underlying database does not store timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal the <code>java.util.Calendar</code> object to use in
     * constructing the timestamp
     * @return the column value as a <code>java.sql.Timestamp</code> object; if
     * the value is SQL <code>NULL</code>, the value returned is
     * <code>null</code> in the Java programming language
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public java.sql.Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException
    {
        return decorate.getTimestamp(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
     * in the Java programming language. This method uses the given calendar to
     * construct an appropriate millisecond value for the timestamp if the
     * underlying database does not store timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @param cal the <code>java.util.Calendar</code> object to use in
     * constructing the date
     * @return the column value as a <code>java.sql.Timestamp</code> object; if
     * the value is SQL <code>NULL</code>, the value returned is
     * <code>null</code> in the Java programming language
     * @exception SQLException if the columnLabel is not valid or if a database
     * access error occurs or this method is called on a closed result set
     * @since 1.2
     */
    public java.sql.Timestamp getTimestamp(String columnLabel, Calendar cal)
            throws SQLException
    {
        return decorate.getTimestamp(columnLabel, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.net.URL</code> object in
     * the Java programming language.
     *
     * @param columnIndex the index of the column 1 is the first, 2 is the
     * second,...
     * @return the column value as a <code>java.net.URL</code> object; if the
     * value is SQL <code>NULL</code>, the value returned is <code>null</code>
     * in the Java programming language
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs; this method is called on a closed result set or if a
     * URL is malformed
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.4
     */
    public java.net.URL getURL(int columnIndex) throws SQLException
    {
        return decorate.getURL(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.net.URL</code> object in
     * the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value as a <code>java.net.URL</code> object; if the
     * value is SQL <code>NULL</code>, the value returned is <code>null</code>
     * in the Java programming language
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs; this method is called on a closed result set or if a
     * URL is malformed
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.4
     */
    public java.net.URL getURL(String columnLabel) throws SQLException
    {
        return decorate.getURL(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.RowId</code> object in
     * the Java programming language.
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @return the column value; if the value is a SQL <code>NULL</code> the
     * value returned is <code>null</code>
     * @throws SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public RowId getRowId(int columnIndex) throws SQLException
    {
        return decorate.getRowId(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.RowId</code> object in
     * the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value ; if the value is a SQL <code>NULL</code> the
     * value returned is <code>null</code>
     * @throws SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public RowId getRowId(String columnLabel) throws SQLException
    {
        return decorate.getRowId(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>NClob</code> object in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>NClob</code> object representing the SQL
     * <code>NCLOB</code> value in the specified column
     * @exception SQLException if the columnIndex is not valid; if the driver
     * does not support national character sets; if the driver can detect that a
     * data conversion error could occur; this method is called on a closed
     * result set or if a database access error occurs
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public NClob getNClob(int columnIndex) throws SQLException
    {
        return decorate.getNClob(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>NClob</code> object in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>NClob</code> object representing the SQL
     * <code>NCLOB</code> value in the specified column
     * @exception SQLException if the columnLabel is not valid; if the driver
     * does not support national character sets; if the driver can detect that a
     * data conversion error could occur; this method is called on a closed
     * result set or if a database access error occurs
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public NClob getNClob(String columnLabel) throws SQLException
    {
        return getNClob(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> as a <code>java.sql.SQLXML</code> object in the
     * Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>SQLXML</code> object that maps an <code>SQL XML</code>
     * value
     * @throws SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public SQLXML getSQLXML(int columnIndex) throws SQLException
    {
        return decorate.getSQLXML(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> as a <code>java.sql.SQLXML</code> object in the
     * Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>SQLXML</code> object that maps an <code>SQL XML</code>
     * value
     * @throws SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public SQLXML getSQLXML(String columnLabel) throws SQLException
    {
        return decorate.getSQLXML(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>String</code> in the Java
     * programming language. It is intended for use when accessing
     * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
     * columns.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public String getNString(int columnIndex) throws SQLException
    {
        return decorate.getNString(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>String</code> in the Java
     * programming language. It is intended for use when accessing
     * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
     * columns.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public String getNString(String columnLabel) throws SQLException
    {
        return decorate.getNString(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.io.Reader</code> object. It
     * is intended for use when accessing
     * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
     * columns.
     *
     * @return a <code>java.io.Reader</code> object that contains the column
     * value; if the value is SQL <code>NULL</code>, the value returned is
     * <code>null</code> in the Java programming language.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @exception SQLException if the columnIndex is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public java.io.Reader getNCharacterStream(int columnIndex) throws SQLException
    {
        return decorate.getNCharacterStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.io.Reader</code> object. It
     * is intended for use when accessing
     * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
     * columns.
     *
     * @param columnLabel the label for the column specified with the SQL AS
     * clause. If the SQL AS clause was not specified, then the label is the
     * name of the column
     * @return a <code>java.io.Reader</code> object that contains the column
     * value; if the value is SQL <code>NULL</code>, the value returned is
     * <code>null</code> in the Java programming language
     * @exception SQLException if the columnLabel is not valid; if a database
     * access error occurs or this method is called on a closed result set
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not
     * support this method
     * @since 1.6
     */
    public java.io.Reader getNCharacterStream(String columnLabel) throws SQLException
    {
        return decorate.getNCharacterStream(columnLabel);
    }
}
