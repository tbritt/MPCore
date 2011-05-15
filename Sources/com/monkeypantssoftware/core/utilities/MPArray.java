package com.monkeypantssoftware.core.utilities;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSRange;

public class MPArray {

    /**
     * This goofy bastard just breaks a list up into a list of sublists
     * where the sublists never hold more than chunkSize items.
     * 
     * Initially I thought this would be a slow implementation, but
     * after running tests it's quite a bit faster than 
     * ERXArrayUtilities.batchedArrayWithSize. Should dig into that later.
     */
    public static <T>NSArray<NSArray<T>> breakIntoChunks(NSArray<T> list, int chunkSize) {
        NSMutableArray<T> mutableList = list.mutableClone();
        NSMutableArray<NSArray<T>> chunks = new NSMutableArray<NSArray<T>>();
        
        if (chunkSize == 0) {
            chunks.add(mutableList.immutableClone());
        }
        else {
            while (! mutableList.isEmpty()) {
                NSArray<T> chunk = null;
                if (mutableList.count() > chunkSize) {
                    chunk = mutableList.subarrayWithRange(new NSRange(0, chunkSize));
                }
                else {
                    chunk = mutableList.immutableClone();
                }
                chunks.add(chunk);
                mutableList.removeObjectsInArray(chunk);
            }
        }
        return chunks.immutableClone();
    }
    
}
