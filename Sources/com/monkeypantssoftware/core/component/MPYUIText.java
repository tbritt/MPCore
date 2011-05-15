package com.monkeypantssoftware.core.component;

import java.io.IOException;
import java.io.InputStream;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSLog;

import er.extensions.appserver.ERXResponseRewriter;
import er.extensions.appserver.ERXWOContext;
import er.extensions.components.ERXComponent;
import er.extensions.foundation.ERXFileUtilities;

public class MPYUIText extends ERXComponent {
    
    private String id;
    private String value;
    private String buttons;
    private String height;
    private String width;
    private String editorTitle;
    private String onBlur;
    private String onCollapse;
    
    public MPYUIText(WOContext context) {
        super(context);
    }
    
    @Override
    public void appendToResponse(WOResponse r, WOContext c) {
        super.appendToResponse(r, c);
        ERXResponseRewriter.addStylesheetResourceInHead(r, c, null, "http://yui.yahooapis.com/2.7.0/build/assets/skins/sam/skin.css");
        ERXResponseRewriter.addScriptResourceInHead(r, c, null, "http://yui.yahooapis.com/2.7.0/build/yahoo-dom-event/yahoo-dom-event.js");
        ERXResponseRewriter.addScriptResourceInHead(r, c, null, "http://yui.yahooapis.com/2.7.0/build/element/element-min.js");
        ERXResponseRewriter.addScriptResourceInHead(r, c, null, "http://yui.yahooapis.com/2.7.0/build/container/container_core-min.js");
        ERXResponseRewriter.addScriptResourceInHead(r, c, null, "http://yui.yahooapis.com/2.7.0/build/menu/menu-min.js");
        ERXResponseRewriter.addScriptResourceInHead(r, c, null, "http://yui.yahooapis.com/2.7.0/build/button/button-min.js");
        ERXResponseRewriter.addScriptResourceInHead(r, c, null, "http://yui.yahooapis.com/2.7.0/build/editor/editor-min.js");
    }
    
    public String id() {
        if (id == null) 
            id = ERXWOContext.safeIdentifierName(context(), true);
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String javascriptEditorVar() {
        return "editor" + id();
    }
    
    /**
     * The value being edited.
     */
    public String value() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    public WOActionResults save() {
        return null;
    }

    /**
     * The JSON configuration for the YUI Editor's buttons. See YUI docs for
     * examples. If not bound the default config is loaded from YUIEditorButtons.txt
     */
    public String buttons() {
        if (buttons == null) {
            
            // Load defaults.
            try {
                InputStream buttonDataStream = application().resourceManager().inputStreamForResourceNamed("YUIEditorButtons.txt", "BoiseApplicationCore", null);
                String result =  ERXFileUtilities.stringFromInputStream(buttonDataStream);
                buttons = result;
            }
            catch (IOException exception) {
                NSLog.err.appendln("Could not read YUIEditorButtons.txt");
            }
            
        }
        return buttons;
    }

    public void setButtons(String buttons) {
        this.buttons = buttons;
    }

    /**
     * The height of the editor in pixels.
     */
    public String height() {
        if (height == null)
            return "200";
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * The width of the editor in pixels.
     */
    public String width() {
        if (width == null)
            return "400";
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * The displayed title of the editor window.
     */
    public String editorTitle() {
        return editorTitle;
    }

    public void setEditorTitle(String editorTitle) {
        this.editorTitle = editorTitle;
    }

    public String onBlur() {
        return onBlur;
    }
    
    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }
    
    public String onCollapse() {
        return onCollapse;
    }
    
    public void setOnCollapse(String onCollapse) {
        this.onCollapse = onCollapse;
    }
    
}