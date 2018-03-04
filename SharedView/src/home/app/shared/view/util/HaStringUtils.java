package home.app.shared.view.util;

import java.util.ArrayList;
import java.util.List;

public class HaStringUtils {

    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String EMAIL_AT = "@";
    public static final String COLON = ":";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String UNDERSCORE = "_";
    public static final String DOT = ".";
    public static final String DASH = "-";
    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String PERCENT = "%";

    public HaStringUtils() {
        super();
    }

    public static Boolean isString(Object source) {
        return source == null ? Boolean.FALSE : (source instanceof String);
    }

    public static Boolean isEmptyString(String input) {
        return input == null ? Boolean.TRUE : (EMPTY).equals(input);
    }

    public static Boolean isEmpty(List sourceList) {
        return sourceList == null || sourceList.isEmpty();
    }

    public static Boolean isEmpty(Object[] sourceArray) {
        return sourceArray == null || sourceArray.length == 0;
    }

    public static List<String> getSearchItems(String searchString, String delimiter) {
        List<String> sourceList = new ArrayList<String>();
        return getStrings(searchString, sourceList, delimiter);
    }

    protected static List<String> getStrings(String input, List<String> sourceList, String delimiter) {
        delimiter = (delimiter == null) ? COMMA : delimiter;
        String arr[] = input.split(delimiter, 2);
        if (!isEmptyString(arr[0])) {
            sourceList.add(arr[0].trim());
        }
        if (arr[0].equals(input)) {
            return sourceList;
        } else {
            getStrings(arr[1], sourceList, delimiter);
        }
        return sourceList;
    }

    public static String initcap(final Object input) {
        return initcap(input, false);
    }

    public static String initcap(Object input, boolean removeDelimiter) {
        if (!isString(input) || isEmptyString((String)input)) {
            return EMPTY;
        }
        String source = (String)input;
        int l = source.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l; i++) {
            char c = source.charAt(i);
            if (!isLetter(c) && !isNumber(source.charAt(i))) {
                if (!removeDelimiter) {
                    sb.append(source.substring(i, i + 1));
                }
                if ((l > 1) && (i != l-1)) {
                    sb.append(source.substring(i + 1, i + 2).toUpperCase());
                }
                i++;
            } else {
                if (l > 1) {
                    if (i == 0) {
                        sb.append(source.substring(i, i + 1).toUpperCase());
                    } else {
                        if (!isLetter(source.charAt(i-1)) && !isNumber(source.charAt(i-1))) {
                            sb.append(source.substring(i, i + 1).toUpperCase());
                        } else {
                            sb.append(source.substring(i, i + 1).toLowerCase());
                        }
                    }
                } else {
                    sb.append(source.toUpperCase());
                }
            }

        }
        return removeDelimiter ? sb.toString().trim() : sb.toString();
    }
    
    public static Boolean isLetter(final char c) {
        if ((c > 64 && c < 91) || (c > 96 && c < 123)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public static Boolean isNumber(final char c) {
        if (c > 47 && c < 58) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public static String printList(final List<String> input) {
        String output = EMPTY;
        if (isEmpty(input)) {
            return output;
        }
        
        final StringBuffer sb = new StringBuffer();
        final int size = input.size();
        if (size == 1) {
            return input.get(0);
        }
        
        for (int i = 0; i < size; i++) {
            sb.append(input.get(i));
            if (i == size - 2) {
                sb.append(SPACE);
                sb.append("and");
                sb.append(SPACE);
            } else if (i != size - 1) {
                sb.append(COMMA);
                sb.append(SPACE);
            }
        }
        return sb.toString();
    }
}
