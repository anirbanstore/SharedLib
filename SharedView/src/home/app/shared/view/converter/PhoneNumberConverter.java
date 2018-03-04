package home.app.shared.view.converter;


import home.app.shared.view.util.HaStringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class PhoneNumberConverter implements Converter {

    public PhoneNumberConverter() {
        super();
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String string) {
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {
        if (object == null || (HaStringUtils.EMPTY).equals(object.toString())) {
            return HaStringUtils.EMPTY;
        }
        if (extractNumbers(object.toString()) != null && extractNumbers(object.toString()).length() == 10) {
            return formatPhoneNumber(extractNumbers(object.toString()));
        } else if (extractNumbers(object.toString()) != null && extractNumbers(object.toString()).length() == 15) {
            return formatLongPhoneNumber(extractNumbers(object.toString()));
        } else {
            facesContext.addMessage(uIComponent.getClientId(facesContext),
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Format Error",
                                                     "Not a valid phone number. "));
            return object.toString();
        }
    }

    private String formatPhoneNumber(final CharSequence input) {
        String extractedString = HaStringUtils.EMPTY;
        if (input != null && input.length() == 10) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(input.subSequence(0, 3));
            sb.append(") ");
            sb.append(input.subSequence(3, 6));
            sb.append("-");
            sb.append(input.subSequence(6, 10));
            extractedString = sb.toString();
        }
        return extractedString;
    }

    private String formatLongPhoneNumber(final CharSequence input) {
        String extractedString = HaStringUtils.EMPTY;
        if (input != null && input.length() == 15) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(input.subSequence(0, 3));
            sb.append(") ");
            sb.append(input.subSequence(3, 6));
            sb.append("-");
            sb.append(input.subSequence(6, 10));
            sb.append("-");
            sb.append(input.subSequence(10, 15));
            extractedString = sb.toString();
        }
        return extractedString;
    }

    private String extractNumbers(final CharSequence input) {
        String extractedString = HaStringUtils.EMPTY;
        if (input != null) {
            StringBuilder sb = new StringBuilder(input.length());
            for (int i = 0; i < input.length(); i++) {
                final char c = input.charAt(i);
                if (c > 47 && c < 58) {
                    sb.append(c);
                }
            }
            extractedString = sb.toString();
        }
        return extractedString;
    }
}
