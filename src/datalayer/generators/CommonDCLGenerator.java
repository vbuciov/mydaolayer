package datalayer.generators;

import datalayer.api.IDCLGenerator;
import datalayer.api.IQueryDataTransferCollection;

/*import datalayer.api.IDCLGenerator;
import datalayer.api.IDataTransfer;*/
/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public class CommonDCLGenerator implements IDCLGenerator
{

    private String how_notify_database, how_call_function,
            call_addition;

    //--------------------------------------------------------------------
    public CommonDCLGenerator()
    {
        how_notify_database = "USE";
        how_call_function = "CALL";
        call_addition = "";
    }

    //--------------------------------------------------------------------
    public String getHowNotifyDatabase()
    {
        return how_notify_database;
    }

    //--------------------------------------------------------------------
    public void setHowNotifyDatabase(String value)
    {
        this.how_notify_database = value;
    }

    //--------------------------------------------------------------------
    public String getHowCallObject()
    {
        return how_call_function;
    }

    //--------------------------------------------------------------------
    public void setHowCallObject(String value)
    {
        this.how_call_function = value;
    }

    //--------------------------------------------------------------------
    public String getCallAddition()
    {
        return call_addition;
    }

    //--------------------------------------------------------------------
    public void setCallAddition(String value)
    {
        this.call_addition = value;
    }

    //--------------------------------------------------------------------
    @Override
    public String formatNotifyDatabase(String name)
    {
        return String.format("%s %s", how_notify_database, name);
    }

    //--------------------------------------------------------------------
    @Override
    public String formatCallProcedure(String name)
    {
        return String.format("{%s %s%s}", how_call_function, name, call_addition);
    }

    //--------------------------------------------------------------------
    @Override
    public String formatCallProcedure(String name, IQueryDataTransferCollection values)
    {

        return String.format("{%s %s(%s)}", how_call_function, name, formatParameters(values));
    }

    //---------------------------------------------------------------------
    private String formatParameters(IQueryDataTransferCollection values)
    {
        StringBuilder dmlParameters = new StringBuilder();

        for (int i = 0; i < values.size(); i++)
        {
            if (dmlParameters.length() > 0)
                dmlParameters.append(", ");
            dmlParameters.append("?");
        }
        return dmlParameters.toString();
    }

    //---------------------------------------------------------------------
    @Override
    public String formatCallFunction(String name)
    {
        return String.format("{? = %s %s()}", how_call_function, name);
    }

    //---------------------------------------------------------------------
    @Override
    public String formatCallFunction(String name, IQueryDataTransferCollection values)
    {
        return String.format("{? = %s %s(%s)}", how_call_function, name, formatParameters(values));
    }

}
