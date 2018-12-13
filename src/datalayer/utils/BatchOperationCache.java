package datalayer.utils;

import java.sql.PreparedStatement;

/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public class BatchOperationCache
{
    PreparedStatement _request;
    int batchCount, batchAffected, start;
    
    //--------------------------------------------------------------------
    public BatchOperationCache(PreparedStatement request)
    {
        _request = request;
         batchCount = 0;
         batchAffected = 0;
         start = 0;
    }

    //--------------------------------------------------------------------
    public PreparedStatement getRequest()
    {
        return _request;
    }

    //--------------------------------------------------------------------
    public int getBatchCount()
    {
        return batchCount;
    }

    //--------------------------------------------------------------------
    public int getBatchAffected()
    {
        return batchAffected;
    }

    //--------------------------------------------------------------------
    public int getStart()
    {
        return start;
    }
    
    //--------------------------------------------------------------------
    public void increaseBatchCountby1()
    {
        batchCount++;
    }
    
    //--------------------------------------------------------------------
    public void increaseStartBy(int value)
    {
        start += value;
    }
    
    //--------------------------------------------------------------------
    public void resetBatchCount ()
    {
        batchCount = 0;
    }
}