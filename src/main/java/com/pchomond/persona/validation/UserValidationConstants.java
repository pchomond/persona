package com.pchomond.persona.validation;

public final class UserValidationConstants {

    private void ValidationConstants() {}

    // Field Names
    public static final String FIELD_IDP_ID = "idp_id";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_DOB = "date_of_birth";
    public static final String FIELD_DOB_YEAR = "date_of_birth.year";
    public static final String FIELD_DOB_MONTH = "date_of_birth.month";
    public static final String FIELD_DOB_DAY = "date_of_birth.day";

    // Syntactic validation error messages
    public static final String MSG_INVALID_EMAIL_FORMAT = "The email address is not a valid format";
    public static final String MSG_INVALID_YEAR = "Year attribute must be between 1920 and current year value";
    public static final String MSG_INVALID_MONTH = "Month must be 1-12";
    public static final String MSG_INVALID_DAY = "Day must be 1-31";
    public static final String MSG_INVALID_DOB = "The provided date does not exist (e.g., Feb 31st)";

    // Business validation error messages
    public static final String MSG_IDP_ID_EXISTS = "A user with this idp_id already exists";
    public static final String MSG_EMAIL_EXISTS = "A user with this email address already exists";
}
