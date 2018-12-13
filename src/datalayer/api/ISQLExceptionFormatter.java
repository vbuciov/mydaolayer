package datalayer.api;

import java.sql.SQLException;

/**
 * Convierte los mensajes de error técnico, en algo entendible por el usuario.
 * @author Victor Manuel Bucio Vargas
 */
public interface ISQLExceptionFormatter
{
    String getFriendlyMessage(SQLException ex);
}
