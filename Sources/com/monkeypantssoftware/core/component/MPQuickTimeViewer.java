package com.monkeypantssoftware.core.component;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;

import er.extensions.components.ERXNonSynchronizingComponent;
import er.extensions.foundation.ERXValueUtilities;

/**
 * QuickTimeViewer is a viewer for QuickTime files. For more information on 
 * parameter bindings, see Apple's documentation regarding
 * <a href="http://developer.apple.com/DOCUMENTATION/QuickTime/Conceptual/QTScripting_HTML/QTScripting_HTML_Document/chapter_1000_section_5.html">
 * QuickTime Object Parameters</a>
 * 
 * @author Travis Britt (Based on Ramsey Gurley's ERAttachmentQuickTimeViewer)
 * @binding fileName a file name of a QuickTime movie resource
 * @binding framework (optional) the framework where the fileName resource can be found
 * @binding movieUrl URL a quicktime movie
 * @binding mimeType the MIME type 
 * @binding class (optional) the class for the html &lt;object&gt;
 * @binding height (optional) the height for the html &lt;object&gt;
 * @binding id (optional) the id for the html &lt;object&gt;
 * @binding parameterDictionary (optional) the NSDictionary containing QuickTime &lt;object&gt; parameters
 * @binding title (optional) the title for the html &lt;object&gt;
 * @binding standby (optional) the text to display for the html &lt;object&gt; while it is loading
 * @binding width (optional) the width for the html &lt;object&gt;
 *
 */
public class MPQuickTimeViewer extends ERXNonSynchronizingComponent {
    
    private String item;
    @SuppressWarnings("rawtypes")
    private NSDictionary parameters;
    
    
    public MPQuickTimeViewer(WOContext context) {
        super(context);
    }
    
    @Override
    public boolean synchronizesVariablesWithBindings() {
        return false;
    }
    
    @Override
    public void reset() {
        super.reset();
        item = null;
        parameters = null;
    }
    
    public String item() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    
    @SuppressWarnings("rawtypes")
    public NSDictionary parameters() {
        if (parameters == null) {
            parameters = ERXValueUtilities.dictionaryValue(valueForBinding("parameterDictionary"));
        }
        return parameters;
    }

    public String parameterValue() {
        return (parameters() == null) ? null : (String)parameters().valueForKey(item());
    }
    
    public String framework() {
        if (hasBinding("framework"))
            return stringValueForBinding("framework");
        return "app";
    }

    public String movieUrl() {
        if (hasBinding("movieUrl"))
            return stringValueForBinding("movieUrl");
        String fileName = stringValueForBinding("fileName");
        return application().resourceManager().urlForResourceNamed(fileName, framework(), null, context().request());
    }
}