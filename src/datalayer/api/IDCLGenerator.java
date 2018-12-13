/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datalayer.api;

import datalayer.api.IQueryDataTransferCollection;

/**
 *
 * @author Victor Manuel Bucio Vargas
 */
public interface IDCLGenerator
{
     //--------------------------------------------------------------------
     String formatNotifyDatabase(String name);

    //--------------------------------------------------------------------
     String formatCallProcedure(String name);
     
    //--------------------------------------------------------------------
    String formatCallProcedure(String name, IQueryDataTransferCollection values);
     
     //--------------------------------------------------------------------
     String formatCallFunction(String name);
     
     //--------------------------------------------------------------------
     String formatCallFunction(String name, IQueryDataTransferCollection values);
}
