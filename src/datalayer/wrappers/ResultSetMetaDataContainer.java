package datalayer.wrappers;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Envoltorio para objetos de tipo ResultSetMetaData. Se utiliza simplemente
 * como clase base para otros envoltorios
 *
 * @author Victor Manuel Bucio Vargas
 */
public abstract class ResultSetMetaDataContainer
{

    private final ResultSetMetaData decorate;

    public ResultSetMetaDataContainer(ResultSetMetaData toDecorate)
    {
        decorate = toDecorate;
    }

    /**
     * Returns the number of decorate in this <code>ResultSet</code> object.
     *
     * @return the number of decorate
     * @exception SQLException if a database access error occurs
     */
    public int getColumnCount() throws SQLException
    {
        return decorate.getColumnCount();
    }

    /**
     * Indicates whether the designated column is automatically numbered.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isAutoIncrement(int column) throws SQLException
    {
        return decorate.isAutoIncrement(column);
    }

    /**
     * Indicates whether a column's case matters.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isCaseSensitive(int column) throws SQLException
    {
        return decorate.isCaseSensitive(column);
    }

    /**
     * Indicates whether the designated column can be used in a where clause.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isSearchable(int column) throws SQLException
    {
        return decorate.isSearchable(column);
    }

    /**
     * Indicates whether the designated column is a cash value.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isCurrency(int column) throws SQLException
    {
        return decorate.isCurrency(column);
    }

    /**
     * Indicates the nullability of values in the designated column.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the nullability status of the given column; one of
     * <code>columnNoNulls</code>, <code>columnNullable</code> or
     * <code>columnNullableUnknown</code>
     * @exception SQLException if a database access error occurs
     */
    public int isNullable(int column) throws SQLException
    {
        return decorate.isNullable(column);
    }

    /**
     * Indicates whether values in the designated column are signed numbers.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isSigned(int column) throws SQLException
    {
        return decorate.isSigned(column);
    }

    /**
     * Indicates the designated column's normal maximum width in characters.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the normal maximum number of characters allowed as the width of
     * the designated column
     * @exception SQLException if a database access error occurs
     */
    public int getColumnDisplaySize(int column) throws SQLException
    {
        return decorate.getColumnDisplaySize(column);
    }

    /**
     * Gets the designated column's suggested title for use in printouts and displays. 
     * The suggested title is usually specified by the SQL AS clause. 
     * If a SQL AS is not specified, 
     * the value returned from getColumnLabel will be the same as 
     * the value returned by the getColumnName method
     *
     * @param column the first column is 1, the second is 2, ...
     * @return column name
     * @exception SQLException if a database access error occurs
     */
    public String getColumnName(int column) throws SQLException
    {
        return decorate.getColumnLabel(column);
    }
    
    /**
     * Get the designated column's name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return column name
     * @exception SQLException if a database access error occurs
     */
    public String getColumnRealName(int column) throws SQLException
    {
        return decorate.getColumnName(column);
    }

    /**
     * Get the designated column's table's schema.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return schema name or "" if not applicable
     * @exception SQLException if a database access error occurs
     */
    public String getSchemaName(int column) throws SQLException
    {
        return decorate.getSchemaName(column);
    }

    /**
     * Get the designated column's specified column size. For numeric data, this
     * is the maximum precision. For character data, this is the length in
     * characters. For datetime datatypes, this is the length in characters of
     * the String representation (assuming the maximum allowed precision of the
     * fractional seconds component). For binary data, this is the length in
     * bytes. For the ROWID datatype, this is the length in bytes. 0 is returned
     * for data types where the column size is not applicable.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return precision
     * @exception SQLException if a database access error occurs
     */
    public int getPrecision(int column) throws SQLException
    {
        return decorate.getPrecision(column);
    }

    /**
     * Gets the designated column's number of digits to right of the decimal
     * point. 0 is returned for data types where the scale is not applicable.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return scale
     * @exception SQLException if a database access error occurs
     */
    public int getScale(int column) throws SQLException
    {
        return decorate.getScale(column);
    }

    /**
     * Gets the designated column's table name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return table name or "" if not applicable
     * @exception SQLException if a database access error occurs
     */
    public String getTableName(int column) throws SQLException
    {
        return decorate.getTableName(column);
    }

    /**
     * Gets the designated column's table's catalog name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the name of the catalog for the table in which the given column
     * appears or "" if not applicable
     * @exception SQLException if a database access error occurs
     */
    public String getCatalogName(int column) throws SQLException
    {
        return decorate.getCatalogName(column);
    }

    /**
     * Retrieves the designated column's SQL type.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return SQL type from java.sql.Types
     * @exception SQLException if a database access error occurs
     * @see Types
     */
    public int getColumnType(int column) throws SQLException
    {
        return decorate.getColumnType(column);
    }

    /**
     * Retrieves the designated column's database-specific type name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return type name used by the database. If the column type is a
     * user-defined type, then a fully-qualified type name is returned.
     * @exception SQLException if a database access error occurs
     */
    public String getColumnTypeName(int column) throws SQLException
    {
        return decorate.getColumnTypeName(column);
    }

    /**
     * Indicates whether the designated column is definitely not writable.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isReadOnly(int column) throws SQLException
    {
        return decorate.isReadOnly(column);
    }

    /**
     * Indicates whether it is possible for a write on the designated column to
     * succeed.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isWritable(int column) throws SQLException
    {
        return decorate.isWritable(column);
    }

    /**
     * Indicates whether a write on the designated column will definitely
     * succeed.response
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isDefinitelyWritable(int column) throws SQLException
    {
        return decorate.isDefinitelyWritable(column);
    }

    /**
     * <p>
     * Returns the fully-qualified name of the Java class whose instances are
     * manufactured if the method <code>ResultSet.getObject</code> is called to
     * retrieve a value from the column.  <code>ResultSet.getObject</code> may
     * return a subclass of the class returned by this method.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the fully-qualified name of the class in the Java programming
     * language that would be used by the method
     * <code>ResultSet.getObject</code> to retrieve the value in the specified
     * column. This is the class name used for custom mapping.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public String getColumnClassName(int column) throws SQLException
    {
        return decorate.getColumnClassName(column);
    }
}
