package datalayer.wrappers;

import java.sql.ParameterMetaData;
import java.sql.SQLException;

/**
 * Envoltorio para objetos de tipo ParameterMetaData.
 * Se utiliza simplemente como clase base para otros envoltorios
 * @author Victor Manuel Bucio Vargas
 */
public abstract class ParameterMetaDataContainer
{
    private final ParameterMetaData decorate;

    public ParameterMetaDataContainer(ParameterMetaData toDecorate)
    {
        decorate = toDecorate;
    }

      /**
     * Retrieves the number of parameters in the <code>PreparedStatement</code>
     * object for which this <code>ParameterMetaData</code> object contains
     * information.
     *
     * @return the number of parameters
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getParameterCount() throws SQLException
    {
        return decorate.getParameterCount();
    }

    /**
     * Retrieves whether null values are allowed in the designated parameter.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return the nullability status of the given parameter; one of
     * <code>ParameterMetaData.parameterNoNulls</code>,
     * <code>ParameterMetaData.parameterNullable</code>, or
     * <code>ParameterMetaData.parameterNullableUnknown</code>
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int isNullable(int param) throws SQLException
    {
        return decorate.isNullable(param);
    }

    /**
     * Retrieves whether values for the designated parameter can be signed
     * numbers.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public boolean isSigned(int param) throws SQLException
    {
        return decorate.isSigned(param);
    }

    /**
     * Retrieves the designated parameter's specified column size.
     *
     * <P>
     * The returned value represents the maximum column size for the given
     * parameter. For numeric data, this is the maximum precision. For character
     * data, this is the length in characters. For datetime datatypes, this is
     * the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For
     * binary data, this is the length in bytes. For the ROWID datatype, this is
     * the length in bytes. 0 is returned for data types where the column size
     * is not applicable.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return precision
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getPrecision(int param) throws SQLException
    {
        return decorate.getPrecision(param);
    }

    /**
     * Retrieves the designated parameter's specified column size.
     *
     * <P>
     * The returned value represents the maximum column size for the given
     * parameter. For numeric data, this is the maximum precision. For character
     * data, this is the length in characters. For datetime datatypes, this is
     * the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For
     * binary data, this is the length in bytes. For the ROWID datatype, this is
     * the length in bytes. 0 is returned for data types where the column size
     * is not applicable.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return precision
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getScale(int param) throws SQLException
    {
        return decorate.getPrecision(param);
    }

    /**
     * Retrieves the designated parameter's specified column size.
     *
     * <P>
     * The returned value represents the maximum column size for the given
     * parameter. For numeric data, this is the maximum precision. For character
     * data, this is the length in characters. For datetime datatypes, this is
     * the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For
     * binary data, this is the length in bytes. For the ROWID datatype, this is
     * the length in bytes. 0 is returned for data types where the column size
     * is not applicable.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return precision
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getParameterType(int param) throws SQLException
    {
        return decorate.getParameterType(param);
    }

    /**
     * Retrieves the designated parameter's database-specific type name.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return type the name used by the database. If the parameter type is a
     * user-defined type, then a fully-qualified type name is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public String getParameterTypeName(int param) throws SQLException
    {
        return decorate.getParameterTypeName(param);
    }

    /**
     * Retrieves the fully-qualified name of the Java class whose instances
     * should be passed to the method <code>PreparedStatement.setObject</code>.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return the fully-qualified name of the class in the Java programming
     * language that would be used by the method
     * <code>PreparedStatement.setObject</code> to set the value in the
     * specified parameter. This is the class name used for custom mapping.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public String getParameterClassName(int param) throws SQLException
    {
        return decorate.getParameterClassName(param);
    }

    /**
     * Retrieves the designated parameter's mode.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return mode of the parameter; one of
     * <code>ParameterMetaData.parameterModeIn</code>,
     * <code>ParameterMetaData.parameterModeOut</code>, or
     * <code>ParameterMetaData.parameterModeInOut</code>
     * <code>ParameterMetaData.parameterModeUnknown</code>.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getParameterMode(int param) throws SQLException
    {
        return decorate.getParameterMode(param);
    }

    /**
     * Returns an object that implements the given interface to allow access to
     * non-standard methods, or standard methods not exposed by the proxy.
     *
     * If the receiver implements the interface then the result is the receiver
     * or a proxy for the receiver. If the receiver is a wrapper and the wrapped
     * object implements the interface then the result is the wrapped object or
     * a proxy for the wrapped object. Otherwise return the the result of
     * calling <code>unwrap</code> recursively on the wrapped object or a proxy
     * for that result. If the receiver is not a wrapper and does not implement
     * the interface, then an <code>SQLException</code> is thrown.
     *
     * @param <T> the type of the class modeled by this Class object
     * @param iface A Class defining an interface that the result must
     * implement.
     * @return an object that implements the interface. May be a proxy for the
     * actual implementing object.
     * @throws java.sql.SQLException If no object found that implements the
     * interface
     * @since 1.6
     */
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        return decorate.unwrap(iface);
    }

    /**
     * Returns true if this either implements the interface argument or is
     * directly or indirectly a wrapper for an object that does. Returns false
     * otherwise. If this implements the interface then return true, else if
     * this is a wrapper then return the result of recursively calling
     * <code>isWrapperFor</code> on the wrapped object. If this does not
     * implement the interface and is not a wrapper, return false. This method
     * should be implemented as a low-cost operation compared to
     * <code>unwrap</code> so that callers can use this method to avoid
     * expensive <code>unwrap</code> calls that may fail. If this method returns
     * true then calling <code>unwrap</code> with the same argument should
     * succeed.
     *
     * @param iface a Class defining an interface.
     * @return true if this implements the interface or directly or indirectly
     * wraps an object that does.
     * @throws java.sql.SQLException if an error occurs while determining
     * whether this is a wrapper for an object with the given interface.
     * @since 1.6
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return decorate.isWrapperFor(iface);
    }
}