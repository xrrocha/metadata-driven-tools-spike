package org.metaxava.model;

import java.util.Locale;

/**
 * Validator interface for type-safe validation with localized error messages
 *
 * DESIGN:
 * - validate() returns fast boolean (no locale needed)
 * - errorMessage() called only when validation fails (lazy evaluation)
 * - Implementations MUST have empty constructor (framework convention)
 * - CATCH_ALL provides no-op default (always valid)
 *
 * USAGE PATTERN:
 * <pre>
 * if (!validator.validate(value)) {
 *     String error = validator.errorMessage(value, currentLocale);
 *     reportError(error);
 * }
 * </pre>
 *
 * INTEGRATION:
 * - Metamodel level: Validator<T> instances referenced by OXValueType
 * - Generated code: Maps to javax.validation annotations or custom ConstraintValidator
 * - Runtime: Minimal JAR (just this interface + converters)
 *
 * @param <T> The type being validated (e.g., Integer, String, Email)
 *           NOT the metamodel type (OXValueType) - we validate VALUES, not metadata
 */
public interface Validator<T> {

    /**
     * Fast validation check
     *
     * @param t The value to validate
     * @return true if valid, false otherwise
     */
    boolean validate(T t);

    /**
     * Localized error message for failed validation
     *
     * @param t The invalid value
     * @param locale The locale for error message
     * @return Localized error message (only called when validate() returns false)
     */
    String errorMessage(T t, Locale locale);

    /**
     * Default catch-all validator that accepts everything
     * Use when no validation is needed for a type
     */
    Validator<Object> CATCH_ALL = new Validator<Object>() {
        @Override
        public boolean validate(Object t) {
            return true;
        }

        @Override
        public String errorMessage(Object t, Locale locale) {
            throw new IllegalStateException(
                "CATCH_ALL.errorMessage() should never be called - validate() always returns true"
            );
        }
    };
}
