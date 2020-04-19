package com.vapor05.dataloom.databus;

/**
 *
 * @author NicholasBocchini
 */
public interface Action<TIn, TOut> {
    
    TOut replace(TIn input) throws DataMapException;
}
