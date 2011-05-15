package com.monkeypantssoftware.core.appserver;

import er.extensions.appserver.ERXDisplayGroup;

public interface MPDisplayGroupAdditions {

    public Object selectNext();
    public Object selectPrevious();
    public void clearSelectedObject();
    public boolean hasPrevious();
    public boolean hasNext();
    public void showAll();
    
    public class DefaultImplementation implements MPDisplayGroupAdditions {
        
        @SuppressWarnings("rawtypes")
        private ERXDisplayGroup displayGroup;
        
        private DefaultImplementation(){};
        
        @SuppressWarnings("rawtypes")
        public static DefaultImplementation newDefaultImplementation(ERXDisplayGroup displayGroup) {
            DefaultImplementation defaultImplementation = new DefaultImplementation();
            defaultImplementation.displayGroup = displayGroup;
            return defaultImplementation;
        }

        public Object selectNext() {
            int index = displayGroup.displayedObjects().indexOfIdenticalObject(displayGroup.selectedObject());
            int indexOfNextObj = (index + 1);

            if (displayGroup.displayedObjects().count() == indexOfNextObj) {
                displayGroup.displayNextBatch();
                displayGroup.setSelectedObject(displayGroup.displayedObjects().objectAtIndex(0));
            } else {
                displayGroup.setSelectedObject(displayGroup.displayedObjects().objectAtIndex(indexOfNextObj));
            }

            return null;
        }

        public Object selectPrevious() {
            int index = displayGroup.displayedObjects().indexOfIdenticalObject(displayGroup.selectedObject());
            int indexOfPreviousObj = (index - 1);

            if (indexOfPreviousObj < 0) {
                displayGroup.displayPreviousBatch();
                displayGroup.setSelectedObject(displayGroup.displayedObjects().lastObject());
            } else {
                displayGroup.setSelectedObject(displayGroup.displayedObjects().objectAtIndex(indexOfPreviousObj));
            }

            return null;
        }
        
        public void clearSelectedObject() {
            displayGroup.setSelectedObject(null);
        }
        
        public boolean hasPrevious() {
            if (displayGroup.displayedObjects().isEmpty())
                return false;

            if (displayGroup.hasMultipleBatches() && displayGroup.currentBatchIndex() > 1)
                return true;

            return displayGroup.displayedObjects().objectAtIndex(0) != displayGroup.selectedObject();
        }

        public boolean hasNext() {
            if (displayGroup.displayedObjects().isEmpty())
                return false;

            if (displayGroup.hasMultipleBatches() && displayGroup.currentBatchIndex() < displayGroup.batchCount())
                return true;

            return displayGroup.displayedObjects().lastObject() != displayGroup.selectedObject();
        }

        public void showAll() {
            displayGroup.queryBindings().removeAllObjects();
            displayGroup.queryMatch().removeAllObjects();
            displayGroup.queryMax().removeAllObjects();
            displayGroup.queryMin().removeAllObjects();
            displayGroup.queryOperator().removeAllObjects();
            displayGroup.qualifyDisplayGroup();
        }
    }
}
