package datalayer.api;

import datalayer.utils.ProductRelation;
import datalayer.utils.Relation;
import datalayer.api.IQueryDataTransferCollection;

/**
 * 
 * @author Victor Manuel Bucio Vargas
 */
public interface IDMLGenerator
{
    String formatSelect (Relation storeObject);
    String formatSelect (Relation storeObject, IQueryDataTransferCollection conditions);
    String formatSelect (ProductRelation storeObject);
    String formatSelect (ProductRelation storeObject, IQueryDataTransferCollection conditions);
    String formatInsert(String storeObject, IQueryDataTransferCollection values);
    String formatUpdate(String storeObject, IQueryDataTransferCollection values);
    String formatDelete(String storeObject, IQueryDataTransferCollection conditions);
}
