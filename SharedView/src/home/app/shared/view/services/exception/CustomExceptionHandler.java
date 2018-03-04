package home.app.shared.view.services.exception;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.share.logging.ADFLogger;
import oracle.adf.view.rich.context.ExceptionHandler;


public class CustomExceptionHandler extends ExceptionHandler {

    private static final ADFLogger LOGGER = ADFLogger.createADFLogger(CustomExceptionHandler.class);
    private static final String CLASSNAME = CustomExceptionHandler.class.toString();

    public CustomExceptionHandler() {
        super();
    }

    @Override
    public void handleException(FacesContext facesContext, Throwable throwable, PhaseId phaseId) {

        LOGGER.entering(CLASSNAME, "handleException()", throwable != null ? throwable.getMessage() : null);

        final String errorMessage = throwable != null ? throwable.getMessage() : null;

        if (errorMessage != null) {
            if (errorMessage.indexOf("ADF_FACES-30107") > -1) {
                // Ignore ViewExpiredException
                LOGGER.fine(CLASSNAME, "handleException()", "Suppressing ViewExpiredException and forcing a log off.");
                redirect("/adfAuthentication?logout=true&end_url=/faces/login");
            } else {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Generic error. Please contact your administrator."));
        }

        LOGGER.exiting(CLASSNAME, "handleException()");

    }

    private void redirect(final String url) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        final ExternalContext ectx = fctx.getExternalContext();
        final HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
        final HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
        sendForward(request, response, url);
    }

    private void sendForward(HttpServletRequest request, HttpServletResponse response, String forwardUrl) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException se) {
            LOGGER.severe("ServletException", se);
        } catch (IOException ie) {
            LOGGER.severe("IOException", ie);
        }
        ctx.responseComplete();
    }
}
