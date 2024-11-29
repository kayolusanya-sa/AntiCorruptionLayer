package com.cubelogic.anticorruptionlayer;

import com.cubelogic.anticorruptionlayer.model.Side;
import com.cubelogic.anticorruptionlayer.model.Transaction;

public class Utils {

    public static Transaction createTransaction(Side side){
        return new Transaction(1,100,1000,side);
    }

    public static Transaction createTransaction(long traderId,double price,double volume, Side side){
        return new Transaction(traderId,price,volume,side);
    }
}
