package com.monkeypantssoftware.core.component;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSSelector;

import er.extensions.appserver.ERXDisplayGroup;
import er.extensions.components.ERXStatelessComponent;

public class MPSortOrder extends ERXStatelessComponent {
    
    private static Logger log = Logger.getLogger(MPSortOrder.class);
    
    public MPSortOrder(WOContext context) {
        super(context);
    }
    
    @SuppressWarnings("rawtypes")
    public ERXDisplayGroup displayGroup() {
        return (ERXDisplayGroup)valueForBinding("displayGroup");
    }
    
    @SuppressWarnings("unchecked")
    private NSArray<EOSortOrdering> sortOrderings() {
        return displayGroup().sortOrderings();
    }
    
    @SuppressWarnings("rawtypes")
    public NSArray<NSSelector> ascendings() {
        return new NSArray<NSSelector>(new NSSelector[]{
                EOSortOrdering.CompareAscending, 
                EOSortOrdering.CompareCaseInsensitiveAscending
        });
    }
    
    @SuppressWarnings("rawtypes")
    public NSArray<NSSelector> descendings() {
        return new NSArray<NSSelector>(new NSSelector[]{
                EOSortOrdering.CompareDescending, 
                EOSortOrdering.CompareCaseInsensitiveDescending
        });
    }
    
    public boolean isAscending() {
        EOSortOrdering sortOrdering = sortOrderingForKey(sortKey());
        if (sortOrdering == null)
            return false;
        return ascendings().containsObject(sortOrdering.selector());
    }
    
    public boolean isDescending() {
        EOSortOrdering sortOrdering = sortOrderingForKey(sortKey());
        if (sortOrdering == null)
            return false;
        return descendings().containsObject(sortOrdering.selector());
    }
    
    public String ascFileName() {
        return stringValueForBinding("ascFileName", "ascSort.png");
    }
    
    public String descFileName() {
        return stringValueForBinding("descFileName", "descSort.png");
    }
    
    public String noSortFileName() {
        return stringValueForBinding("noSortFileName", "noSort.png");
    }
    
    public String fileName() {
        if (isAscending())
            return ascFileName();
        if (isDescending())
            return descFileName();
        return noSortFileName();
    }
    
    public String sortKey() {
        return stringValueForBinding("sortKey", "toString");
    }

    public WOActionResults sort() {
        setSortOrder(sortKey());
        return null;
    }
    
    public void setSortOrder(String keyName) {
        setSortOrder(new NSArray<String>(keyName));
    }
    
    /**
     * Sets the sort order by the collection of key names passed in. The 
     * selector is reversed if the same set of keys are used twice.
     * 
     * @author tbritt@phigment.org
     * @param keyNames  Collection of keys or keypaths to sort on.
     */
    @SuppressWarnings("rawtypes")
    public void setSortOrder(NSArray<String> keyNames) {
        NSArray<EOSortOrdering> oldOrderings = sortOrderings();
        NSMutableArray<EOSortOrdering> orderings = new NSMutableArray<EOSortOrdering>();
        
        if ((oldOrderings != null) && 
                (oldOrderings.valueForKeyPath("key").toString().equals(
                        keyNames.toString()))) {
            
            // Reverse the sort of each key.
            for (EOSortOrdering sortOrdering: oldOrderings) {
                NSSelector newSelector = (sortOrdering.selector() == 
                    EOSortOrdering.CompareCaseInsensitiveDescending ?
                        EOSortOrdering.CompareCaseInsensitiveAscending :
                            EOSortOrdering.CompareCaseInsensitiveDescending);
                
                EOSortOrdering newOrdering = EOSortOrdering.sortOrderingWithKey(
                        sortOrdering.key(), newSelector);
                orderings.addObject(newOrdering);
            }       
        } else {
            
            // Do a default sort.
            for (String key: keyNames) {
                EOSortOrdering newOrdering = EOSortOrdering.sortOrderingWithKey(
                        key, EOSortOrdering.CompareCaseInsensitiveDescending);
                orderings.addObject(newOrdering);
            }
        }
        
        log.debug("New sort orderings: " + orderings);
        
        NSArray<EOSortOrdering>sortOrderings = orderings.immutableClone();
        displayGroup().setSortOrderings(sortOrderings);
        displayGroup().setCurrentBatchIndex(1);
        displayGroup().qualifyDisplayGroup();
    }
    
    private EOSortOrdering sortOrderingForKey(String key) {
        if (sortOrderings() == null)
            return null;
        
        for (Object obj : sortOrderings()) {
            EOSortOrdering sortOrdering = (EOSortOrdering)obj;
            if (sortOrdering.key().equalsIgnoreCase(key))
                return sortOrdering;
        }
        return null;
    }

    public String imageFramework() {
        return stringValueForBinding("imageFramework", "MPCore");
    }
}