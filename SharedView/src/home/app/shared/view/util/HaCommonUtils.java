package home.app.shared.view.util;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCParameter;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.ControlBinding;
import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class HaCommonUtils {

    public HaCommonUtils() {
        super();
    }

    public static ApplicationModule getApplicationModule(final String configModule, final String configurationName) {
        return Configuration.createRootApplicationModule(configModule, configurationName);
    }

    public static void releaseApplicationModule(final ApplicationModule am) {
        Configuration.releaseRootApplicationModule(am, false);
    }

    /**
     *
     * @param iteratorName
     * @return
     */
    public static DCIteratorBinding findIterator(final String iteratorName) {
        DCBindingContainer dc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterator = dc.findIteratorBinding(iteratorName);
        return iterator;
    }
    
    /**
     * @param bindingContainer
     * @param iterator
     * @return
     */
    public static DCIteratorBinding findIterator(String bindingContainer, String iterator) {
        DCBindingContainer bindings = (DCBindingContainer) resolveExpression("#{" + bindingContainer + "}");
        if (bindings == null) {
            throw new RuntimeException("Binding container '" + bindingContainer + "' not found");
        }
        DCIteratorBinding iter = bindings.findIteratorBinding(iterator);
        if (iter == null) {
            throw new RuntimeException("Iterator '" + iterator + "' not found");
        }
        return iter;
    }
    
    /**
     * @param name
     * @return
     */
    public static JUCtrlValueBinding findCtrlBinding(String name) {
        JUCtrlValueBinding rowBinding = (JUCtrlValueBinding) getDCBindingContainer().findCtrlBinding(name);
        if (rowBinding == null) {
            throw new RuntimeException("CtrlBinding " + name + "' not found");
        }
        return rowBinding;
    }

    /**
     *
     * @param bindingName
     * @return
     */
    public static OperationBinding findOperation(final String bindingName) {
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationBinding = bindings.getOperationBinding(bindingName);
        return operationBinding;
    }
    
    /**
     * Find an operation binding in the current binding container by name.
     *
     * @param bindingContianer binding container name
     * @param opName operation binding name
     * @return operation binding
     */
    public static OperationBinding findOperation(String bindingContianer, String opName) {
        DCBindingContainer bindings = (DCBindingContainer) resolveExpression("#{" + bindingContianer + "}");
        if (bindings == null) {
            throw new RuntimeException("Binding container '" + bindingContianer + "' not found");
        }
        OperationBinding op = bindings.getOperationBinding(opName);
        if (op == null) {
            throw new RuntimeException("Operation '" + opName + "' not found");
        }
        return op;
    }

    /**
     * Convenience method for setting Session variables.
     * @param key object key
     * @param object value to store
     */
    public static void storeOnSession(final String key, final Object object) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String, Object> sessionState = ctx.getExternalContext().getSessionMap();
        sessionState.put(key, object);
    }

    /**
     * Convenience method for removing Session variables.
     * @param key object key
     */
    public static void removeFromSession(final String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map sessionState = ctx.getExternalContext().getSessionMap();
        sessionState.remove(key);
    }

    /**
     * Convenience method for setting Session variables.
     * @param key object key
     * @param object value to store
     */
    public static void storeOnPageFlow(final String key, final Object object) {
        ADFContext ctx = ADFContext.getCurrent();
        Map<String, Object> pageflowState = ctx.getPageFlowScope();
        pageflowState.put(key, object);
    }

    /**
     * Convenience method for removing PageFlow variables.
     * @param key object key
     */
    public static void removeFromPageflow(final String key) {
        ADFContext ctx = ADFContext.getCurrent();
        Map pageflowState = ctx.getPageFlowScope();
        pageflowState.remove(key);
    }

    /**
     * Convenience method for getting Session variables.
     * @param key object key
     */
    public static Object getFromSession(final String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map sessionState = ctx.getExternalContext().getSessionMap();
        return sessionState.get(key);
    }

    public static Object getFromPageflow(final String key) {
        ADFContext ctx = ADFContext.getCurrent();
        Map pageflowState = ctx.getPageFlowScope();
        return pageflowState.get(key);
    }

    /**
     *
     * @param bundle
     * @param key
     * @param defaultValue
     * @return
     */
    private static String getStringSafely(final ResourceBundle bundle, final String key, final String defaultValue) {
        String resource = null;
        try {
            resource = bundle.getString(key);
        } catch (MissingResourceException mrex) {
            if (defaultValue != null) {
                resource = defaultValue;
            } else {
                resource = null;
            }
        }
        return resource;
    }

    /**
     * Returns the bundle name
     * @return
     */
    private static ResourceBundle getBundle() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        UIViewRoot uiRoot = ctx.getViewRoot();
        Locale locale = uiRoot.getLocale();
        ClassLoader ldr = Thread.currentThread().getContextClassLoader();
        return ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), locale, ldr);
    }

    /**
     *
     * @param errType
     * @param key
     * @param severity
     * @return
     */
    public static FacesMessage getMessageFromBundle(final String errType, final String key,
                                                    final FacesMessage.Severity severity) {
        ResourceBundle bundle = getBundle();
        String summary = getStringSafely(bundle, key + "_summary", null);
        String detail = getStringSafely(bundle, key + "_detail", summary);
        FacesMessage message;
        if (errType != null)
            message = new FacesMessage(summary + " " + errType, detail);
        else
            message = new FacesMessage(summary, detail);

        message.setSeverity(severity);
        return message;
    }

    /**
     * This public method returns the String value for the corresponding key
     * from the language bundle file.
     * @param key
     * @return String
     */
    public static String getDisplayMessageFromBundle(final String key) {
        ResourceBundle bundle = getBundle();
        String displayMessage = getStringSafely(bundle, key, null);
        return displayMessage;
    }

    /**
     * Public convenience method to return ADF Faces Context
     * @return
     */
    public static AdfFacesContext getADFFacesContext() {
        return AdfFacesContext.getCurrentInstance();
    }

    /**
     * Public convenience method to return External Context
     * @return
     */
    public static ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    /**
     * Public convenience method to return Faces Context
     * @return
     */
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Public convenience method to refresh an ADF UI component. The parameter can be any ADF Rich component
     * @param uicomponent
     */
    public static void addPartialTarget(final UIComponent uicomponent) {
        getADFFacesContext().addPartialTarget(uicomponent);
    }

    /**
     * Does  a partial target refresh of the UIComponent in view root with the specified id.
     * @param id UIComponent id
     */
    public static void addPartialTarget(final String id) {
        UIComponent changedComp = getComponent(id);
        if (changedComp != null) {
            getADFFacesContext().addPartialTarget(changedComp);
        }
    }

    /**
     * Does a partial target refresh of the provided components. Requires
     * components have clientComponent=true.
     * @param changedComps
     */
    public static void addPartialTargets(final Collection<UIComponent> changedComps) {
        if (changedComps != null) {
            for (UIComponent comp : changedComps) {
                addPartialTarget(comp);
            }
        }
    }

    /**
     * Does  a partial target refresh of the provided components.
     * @param uiComponents
     */
    public static void addPartialTargets(UIComponent... uiComponents) {
        for (UIComponent uiComponent : uiComponents) {
            addPartialTarget(uiComponent);
        }
    }

    public static UIComponent getComponent(String componentId) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Iterator iter = ctx.getViewRoot().getChildren().iterator();
        while (iter.hasNext()) {
            UIComponent component = (UIComponent) iter.next();
            UIComponent targetComponent = getComponent(component, componentId);
            if (targetComponent != null)
                return targetComponent;
        }
        return null;
    }

    private static UIComponent getComponent(final UIComponent uiComponent, final String componentId) {
        UIComponent targetComponent = uiComponent.findComponent(componentId);
        if (targetComponent != null)
            return targetComponent;
        Iterator iter = uiComponent.getFacetsAndChildren();
        while (iter.hasNext()) {
            UIComponent component = (UIComponent) iter.next();
            UIComponent target = getComponent(component, componentId);
            if (target != null)
                return target;
        }
        return null;
    }

    public static UIComponent findComponentInRoot(String id) {
        UIComponent component = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            UIComponent root = facesContext.getViewRoot();
            component = findComponent(root, id);
        }
        return component;
    }

    /**
     * Locate an UIComponent from its root component.
     * Taken from http://www.jroller.com/page/mert?entry=how_to_find_a_uicomponent
     * @param base root Component (parent)
     * @param id UIComponent id
     * @return UIComponent object
     */
    public static UIComponent findComponent(UIComponent component, String name) {
        if (name.equals(component.getId()))
            return component;

        List<UIComponent> items = component.getChildren();
        Iterator<UIComponent> facets = component.getFacetsAndChildren();

        if (items.size() > 0) {
            for (UIComponent item : items) {
                UIComponent found = findComponent(item, name);
                if (found != null) {
                    return found;
                }
                if (item.getId().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        } else if (facets.hasNext()) {
            while (facets.hasNext()) {
                UIComponent facet = facets.next();
                UIComponent found = findComponent(facet, name);
                if (found != null) {
                    return found;
                }
                if (facet.getId().equalsIgnoreCase(name)) {
                    return facet;
                }
            }
        }
        return null;
    }

    public static void addComponent(final UIComponent parentUIComponent, final UIComponent childUIComponent) {
        parentUIComponent.getChildren().add(childUIComponent);
    }

    public static MethodExpressionActionListener addMethodBinding(final String methodName) {
        MethodExpression methodExpression =
            getExpressionFactory().createMethodExpression(getELContext(), methodName, Object.class, new Class[] {
                                                          ActionEvent.class });
        return new MethodExpressionActionListener(methodExpression);
    }

    public static void addFacesMessage(String errType, String errMessage) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        FacesMessage fm = new FacesMessage();
        if (errType == null)
            errType = "ERROR";
        if (errType.equals("ERROR")) {
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            fm.setSummary("ERROR");
        } else if (errType.equals("WARNING")) {
            fm.setSeverity(FacesMessage.SEVERITY_WARN);
            fm.setSummary("WARNING");
        } else if (errType.equals("INFO")) {
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            fm.setSummary("INFO");
        }
        fm.setDetail(errMessage);
        fctx.addMessage(null, fm);
    }

    public static void showPopup(final RichPopup popup) {
        final RichPopup.PopupHints hints = new RichPopup.PopupHints();
        if (popup != null) {
            popup.show(hints);
        }
    }
    
    public static void showPopup(final String popupId) {
        final RichPopup.PopupHints hints = new RichPopup.PopupHints();
        final UIComponent popup = findComponentInRoot(popupId);
        if (popup != null && popup instanceof RichPopup) {
            ((RichPopup) popup).show(hints);
        } else {
            throw new RuntimeException("Cannot find popup to show.");
        }
    }

    public static Application getApplication() {
        return getFacesContext() != null ? getFacesContext().getApplication() : null;
    }

    public static ExpressionFactory getExpressionFactory() {
        return getApplication() != null ? getApplication().getExpressionFactory() : null;
    }

    public static MethodExpression getMethodExpression(final ELContext elContext, final String expression,
                                                       Class returnType, Class[] argTypes) {
        return getExpressionFactory() != null ?
               getExpressionFactory().createMethodExpression(elContext, expression, returnType, argTypes) : null;
    }

    public static ELContext getELContext() {
        return getFacesContext() != null ? getFacesContext().getELContext() : null;
    }

    public static Object invokeMethodExpression(final String expression, Class returnType, Class[] argTypes,
                                                 Object[] argValues) {
        Object ret = null;
        final ELContext ectx = getELContext();
        if (ectx != null) {
            final MethodExpression methodExpression = getMethodExpression(ectx, expression, returnType, argTypes);
            if (methodExpression != null) {
                ret = methodExpression.invoke(ectx, argValues);
            }
        }
        return ret;
    }

    public static Object invokeMethodExpression(final String expression, Class returnType, Class argTypes,
                                                Object argValues) {
        return invokeMethodExpression(expression, returnType, new Class[] { argTypes }, new Object[] { argValues });
    }

    public static void updateModel(final ValueChangeEvent valueChangeEvent) {
        final UIComponent uiComponent = valueChangeEvent.getComponent();
        uiComponent.processUpdates(getFacesContext());
    }

    public static ValueExpression getValueExpression(final String expression) {
        final ELContext elContext = getELContext();
        final ExpressionFactory exFactory = getExpressionFactory();
        return exFactory.createValueExpression(elContext, expression, Object.class);
    }

    public static Object resolveExpression(final String expression) {
        final ValueExpression ve = getValueExpression(expression);
        return ve.getValue(getELContext());
    }

    public static void writeJavasciptToClient(final String script) {
        final FacesContext fctx = getFacesContext();
        final ExtendedRenderKitService erks = Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
        erks.addScript(fctx, script);
    }

    public static Object getComponentAttribute(final UIComponent ui, final String attrName) {
        Object attrValue = null;
        if (ui != null) {
            final Map attrMap = ui.getAttributes();
            if (attrMap != null && attrName != null) {
                attrValue = attrMap.get(attrName);
            }
        }
        return attrValue;
    }

    /**
     * Convenience method to find a DCControlBinding as an AttributeBinding
     * to get able to then call getInputValue() or setInputValue() on it.
     * @param bindingContainer binding container
     * @param attributeName name of the attribute binding.
     * @return the control value binding with the name passed in.
     *
     */
    public static AttributeBinding findControlBinding(BindingContainer bindingContainer, String attributeName) {
        if (attributeName != null) {
            if (bindingContainer != null) {
                ControlBinding ctrlBinding = bindingContainer.getControlBinding(attributeName);
                if (ctrlBinding instanceof AttributeBinding) {
                    return (AttributeBinding) ctrlBinding;
                }
            }
        }
        return null;
    }

    /**
     * A convenience method for getting the value of a bound attribute in the
     * current page context programatically.
     * @param attributeName of the bound value in the pageDef
     * @return value of the attribute
     */
    public static Object getBoundAttributeValue(String attributeName) {
        return findControlBinding(attributeName).getInputValue();
    }

    /**
     * A convenience method for setting the value of a bound attribute in the
     * context of the current page.
     * @param attributeName of the bound value in the pageDef
     * @param value to set
     */
    public static void setBoundAttributeValue(String attributeName, Object value) {
        findControlBinding(attributeName).setInputValue(value);
    }
    
    /**
     * Returns the evaluated value of a pageDef parameter.
     * @param pageDefName reference to the page definition file of the page with the parameter
     * @param parameterName name of the pagedef parameter
     * @return evaluated value of the parameter as a String
     */
    public static Object getPageDefParameterValue(String pageDefName, String parameterName) {
        BindingContainer bindings = findBindingContainer(pageDefName);
        DCParameter param = ((DCBindingContainer) bindings).findParameter(parameterName);
        return param.getValue();
    }

    /**
     * Convenience method to find a DCControlBinding as a JUCtrlValueBinding
     * to get able to then call getInputValue() or setInputValue() on it.
     * @param attributeName name of the attribute binding.
     * @return the control value binding with the name passed in.
     *
     */
    public static AttributeBinding findControlBinding(String attributeName) {
        return findControlBinding(getBindingContainer(), attributeName);
    }
    
    /**
     * Find the BindingContainer for a page definition by name.
     *
     * Typically used to refer eagerly to page definition parameters. It is
     * not best practice to reference or set bindings in binding containers
     * that are not the one for the current page.
     *
     * @param pageDefName name of the page defintion XML file to use
     * @return BindingContainer ref for the named definition
     */
    private static BindingContainer findBindingContainer(String pageDefName) {
        BindingContext bctx = getDCBindingContainer().getBindingContext();
        BindingContainer foundContainer = bctx.findBindingContainer(pageDefName);
        return foundContainer;
    }

    /**
     * Return the current page's binding container.
     * @return the current page's binding container
     */
    public static BindingContainer getBindingContainer() {
        return (BindingContainer) resolveExpression("#{bindings}");
    }
    
    public static BindingContainer getBindings() {
        BindingContext bctx = BindingContext.getCurrent();
        BindingContainer bindings = bctx.getCurrentBindingsEntry();
        return bindings;
    }

    /**
     * Return the Binding Container as a DCBindingContainer.
     * @return current binding container as a DCBindingContainer
     */
    public static DCBindingContainer getDCBindingContainer() {
        return (DCBindingContainer) getBindingContainer();
    }
}
