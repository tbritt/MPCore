package com.monkeypantssoftware.core.appserver;

import er.extensions.batching.ERXBatchingDisplayGroup;

public class MPBatchingDisplayGroup<T> extends ERXBatchingDisplayGroup<T> implements MPDisplayGroupAdditions {
    
    private MPDisplayGroupAdditions.DefaultImplementation defaultImplementation;
    
    private MPDisplayGroupAdditions.DefaultImplementation defaultImplementation() {
        if (defaultImplementation == null) {
            defaultImplementation = MPDisplayGroupAdditions.DefaultImplementation.newDefaultImplementation(this);
        }
        return defaultImplementation;
    }
    
    @Override
    public Object selectNext() {
        return defaultImplementation().selectNext();
    }

    @Override
    public Object selectPrevious() {
        return defaultImplementation().selectPrevious();
    }

    public void clearSelectedObject() {
        defaultImplementation().clearSelectedObject();
    }

    public boolean hasPrevious() {
        return defaultImplementation().hasPrevious();
    }

    public boolean hasNext() {
        return defaultImplementation().hasNext();
    }

    public void showAll() {
        defaultImplementation().showAll();
    }

}
