package datalayer.generators;

import datalayer.api.IDMLGenerator;
import datalayer.utils.Join;
import datalayer.utils.ProductRelation;
import datalayer.utils.QueryParameter;
import datalayer.utils.Relation;
import datalayer.utils.RelationOptions;
import datalayer.api.IQueryDataTransferCollection;

/**
 * Generador de sentencias DML
 *
 * @author Victor Manuel Bucio Vargas
 */
public class CommonDMLGenerator implements IDMLGenerator
{

    public static String COLON = ", ";
    public static String SELECT = "SELECT ";
    public static String FROM = " FROM ";
    public static String ALL_COLUNMS = "*";
    public static String AND = " AND ";
    public static String OR = " OR ";
    public static String WHERE = " WHERE ";
    boolean usa_comillas;

    //--------------------------------------------------------------------
    public CommonDMLGenerator()
    {
        usa_comillas = false;
    }

    //--------------------------------------------------------------------
    @Override
    public String formatSelect(Relation storeObject)
    {
        StringBuilder DML = new StringBuilder();

        //SELECT {COLUMNS} FROM {STORE} [LEFT|RIGTH] [INNER | CROSS | OUTER] JOIN {STORE1} ON {CONDITIONAL} 
        DML.append(SELECT);
        DML.append(storeObject.hasProyection() ? formatProyectionSintax(storeObject.getProyection()) : ALL_COLUNMS);
        DML.append(FROM);
        DML.append(storeObject.getName());
        if (storeObject.hasOptions())
            DML.append(formatOptionSintax(storeObject.getOptions()));

        return DML.toString();
    }

    //--------------------------------------------------------------------
    @Override
    public String formatSelect(Relation storeObject, IQueryDataTransferCollection conditions)
    {
        StringBuilder DML = new StringBuilder();
        //SELECT * FROM {STORE} WHERE {COLUMNS = CONDITIONS}

        DML.append(SELECT);
        DML.append(storeObject.hasProyection() ? formatProyectionSintax(storeObject.getProyection()) : ALL_COLUNMS);
        DML.append(FROM);
        DML.append(storeObject.getName());
        DML.append(formatConditionalSintax(conditions));
        if (storeObject.hasOptions())
            DML.append(formatOptionSintax(storeObject.getOptions()));

        return DML.toString();
    }

    //--------------------------------------------------------------------
    @Override
    public String formatSelect(ProductRelation storeObject)
    {
        StringBuilder DML = new StringBuilder();

        //SELECT {COLUMNS} FROM {STORE} [LEFT|RIGTH] [INNER | CROSS | OUTER] JOIN {STORE1} ON {CONDITIONAL} 
        DML.append(SELECT);
        DML.append(storeObject.hasProyection() ? formatProyectionSintax(storeObject.getProyection()) : ALL_COLUNMS);
        DML.append(FROM);
        DML.append(storeObject.getName());
        DML.append(formatJoinSintax(storeObject.getAnothers()));
        if (storeObject.hasOptions())
            DML.append(formatOptionSintax(storeObject.getOptions()));

        return DML.toString();
    }

    //--------------------------------------------------------------------
    @Override
    public String formatSelect(ProductRelation storeObject, IQueryDataTransferCollection conditions)
    {
        StringBuilder DML = new StringBuilder();
        //SELECT * FROM {STORE} [LEFT|RIGTH] [INNER | CROSS | OUTER] JOIN {STORE1} ON {CONDITIONAL} WHERE {COLUMNS = CONDITIONS}

        DML.append(SELECT);
        DML.append(storeObject.hasProyection() ? formatProyectionSintax(storeObject.getProyection()) : ALL_COLUNMS);
        DML.append(FROM);
        DML.append(storeObject.getName());
        DML.append(formatJoinSintax(storeObject.getAnothers()));
        DML.append(formatConditionalSintax(conditions));
        if (storeObject.hasOptions())
            DML.append(formatOptionSintax(storeObject.getOptions()));

        return DML.toString();
    }

    //--------------------------------------------------------------------
    @Override
    public String formatInsert(String storeObject, IQueryDataTransferCollection values)
    {
        //INSERT INTO {STORE} ( {COLUMNS}) VALUES ({VALUES})
        StringBuilder dmlcolumns = new StringBuilder();
        StringBuilder dmlValue = new StringBuilder();
        StringBuilder DML = new StringBuilder();

        //We are doing two sintax at the same time on different memory spots
        for (int i = 0; i < values.size(); i++)
        {
            if (dmlcolumns.length() > 0)
                dmlcolumns.append(COLON);

            if (usa_comillas)
                dmlcolumns.append(String.format("\"%s\" = ?", values.getKeyName(i)));
            else
                dmlcolumns.append(values.getKeyName(i));

            if (dmlValue.length() > 0)
                dmlValue.append(COLON);

            dmlValue.append("?");
        }

        DML.append("INSERT INTO ");
        DML.append(storeObject);
        DML.append("(");
        DML.append(dmlcolumns.toString());
        DML.append(")");
        DML.append(" VALUES (");
        DML.append(dmlValue.toString());
        DML.append(")");

        return DML.toString();
    }

    //--------------------------------------------------------------------
    @Override
    public String formatUpdate(String storeObject, IQueryDataTransferCollection values)
    {
        //UPDATE {STORE} SET {COLUMNS = VALUES} WHERE {COLUMNS = CONDITIONS}
        StringBuilder dmlcolumnsvalues = new StringBuilder();
        StringBuilder dmlconditions = new StringBuilder();
        StringBuilder DML = new StringBuilder();

        //We are doing two sintax at the same time on different memory spots
        for (int i = 0; i < values.size(); i++)
        {
            if (!values.isConditional(i))
            {
                if (dmlcolumnsvalues.length() > 0)
                    dmlcolumnsvalues.append(COLON);

                dmlcolumnsvalues.append(formatSetValuesSintax(values.getKeyName(i)));
            }
            else
            {
                if (dmlconditions.length() > 0)
                    dmlconditions.append(AND);
                else
                    dmlconditions.append(WHERE);

                dmlconditions.append(formatConditionalSintax(values.getKeyName(i),
                                                             values.getOperatorOf(i)));
            }
        }

        DML.append("UPDATE ");
        DML.append(storeObject);
        DML.append(" SET ");
        DML.append(dmlcolumnsvalues.toString());
        DML.append(dmlconditions.toString());

        return DML.toString();
    }

    //--------------------------------------------------------------------
    @Override
    public String formatDelete(String storeObject, IQueryDataTransferCollection conditions)
    {
        //DELETE FROM {STORE} WHERE {COLUMNS = CONDITIONS}
        StringBuilder DML = new StringBuilder();

        DML.append("DELETE FROM ");
        DML.append(storeObject);
        DML.append(formatConditionalSintax(conditions));

        return DML.toString();
    }

    //--------------------------------------------------------------------
    private String formatSetValuesSintax(String KeyName)
    {
        if (usa_comillas)
            return String.format("\"%s\" = ?", KeyName);
        else
            return String.format("%s = ?", KeyName);
    }

    //--------------------------------------------------------------------
    private String formatConditionalSintax(IQueryDataTransferCollection conditions)
    {
        StringBuilder dmlconditions = new StringBuilder();

        for (int i = 0; i < conditions.size(); i++)
        {
            if (dmlconditions.length() > 0)
                dmlconditions.append(AND);
            else
                dmlconditions.append(WHERE);

            dmlconditions.append(formatConditionalSintax(conditions.getKeyName(i),
                                                         conditions.getOperatorOf(i)));
        }

        return dmlconditions.toString();
    }

    //--------------------------------------------------------------------
    public String formatConditionalSintax(String keyName, QueryParameter.Operator applyOperator)
    {
        String finalName;

        if (usa_comillas)
            finalName = String.format("\"%s\" = ?", keyName);
        else
            finalName = keyName;

        switch (applyOperator)
        {
            case EQUAL:
                return String.format("%s = ?", finalName);

            case GREATER_EQUAL:
                return String.format("%s >=  ?", finalName);

            case GREATER_THAN:
                return String.format("%s >  ?", finalName);

            case LOWER_EQUAL:
                return String.format("%s <=  ?", finalName);

            case LOWER_THAN:
                return String.format("%s <  ?", finalName);

            case LIKE:
                return String.format("%s LIKE ?", finalName);

            case BETWEEN:
                return String.format("%s BETWEEN ? AND ?", finalName);

            default:
                return String.format("%s = ?", finalName);
        }
    }

    //--------------------------------------------------------------------
    private String formatProyectionSintax(String[] columns)
    {
        StringBuilder dmlcolumns = new StringBuilder();

        if (columns.length > 0)
        {
            for (int i = 0; i < columns.length; i++)
            {
                if (dmlcolumns.length() > 0)
                {
                    dmlcolumns.append(COLON);
                }

                dmlcolumns.append(columns[i]);
            }
        }
        else
        {
            dmlcolumns.append(ALL_COLUNMS);
        }

        return dmlcolumns.toString();
    }

    //--------------------------------------------------------------------
    private String formatJoinSintax(Join[] storeObjects)
    {
        StringBuilder dmlJoins = new StringBuilder();

        for (int j = 0; j < storeObjects.length; j++)
        {
            switch (storeObjects[j].getOption())
            {
                case LEFT_OUTER:
                    dmlJoins.append(" LEFT OUTER JOIN ");
                    break;

                case RIGHT_OUTER:
                    dmlJoins.append(" RIGTH OUTER JOIN ");
                    break;

                case INNER:
                    dmlJoins.append(" INNER JOIN ");
                    break;

                case CROSS:
                    dmlJoins.append(" CROSS JOIN ");
                    break;

                case NATURAL:
                    dmlJoins.append(" NATURAL JOIN ");
                    break;

                case NATURAL_LEFT_OUTER:
                    dmlJoins.append(" NATURAL LEFT OUTER JOIN ");
                    break;

                case NATURAL_RIGHT_OUTER:
                    dmlJoins.append(" NATURAL RIGTH OUTER JOIN ");
                    break;

                default:
                    dmlJoins.append(" JOIN ");
                    break;
            }

            dmlJoins.append(storeObjects[j].getStoreObject());
            if (storeObjects[j].getOption() != Join.joinType.CROSS)
            {
                dmlJoins.append(" ON ");
                dmlJoins.append(storeObjects[j].getOnConditional());
            }
        }

        return dmlJoins.toString();
    }

    //--------------------------------------------------------------------
    private String formatOptionSintax(RelationOptions[] options)
    {
        StringBuilder groupBy = new StringBuilder();
        StringBuilder orderBy = new StringBuilder();
        StringBuilder current = null;
        String[] columns;
        String KeyWord = "";

        if (options.length > 0)
        {
            for (int i = 0; i < options.length; i++)
            {
                columns = options[i].getColumns();
                switch (options[i].getOperation())
                {
                    case GROUP_BY:
                        KeyWord = " GROUP BY ";
                        current = groupBy;
                        break;

                    case ORDER_BY:
                        KeyWord = " ORDER BY ";
                        current = orderBy;
                        break;
                }

                for (int j = 0; j < columns.length; j++)
                {
                    if (current.length() > 0)
                    {
                        current.append(COLON);
                    }
                    else
                    {
                        current.append(KeyWord);
                    }

                    current.append(columns[j]);
                }
            }
        }

        return groupBy.toString() + orderBy.toString();
    }
}
