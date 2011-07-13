package activejdbc.validation;

import activejdbc.Messages;
import activejdbc.Model;

import java.util.Locale;

/**
 * Subclass this class to create custom validators.
 *
 * @author Igor Polevoy
 */
public abstract class ValidatorAdapter<T extends Model> implements Validator<T>{
    private String message;

    public abstract void validate(T m);

    public final void setMessage(String message) {
        this.message = message;
    }

    /**
     * Provides default implementation, will look for a property in resource bundle, using set message as key.
     * If property in resource bundle not found, treats message verbatim.
     *
     * @param locale locale to use.
     * @param params parameters in case a message is parametrized.
     * @return formatted message. 
     */
    public String formatMessage(Locale locale, Object ... params) {
        return locale != null ? Messages.message(message, locale, params) : Messages.message(message, params);     
    }

    public String getMessage() {
        return message;
    }
}