package com.json4orm.model.schema;

public final class IdGenerator {
    public static final String AUTO="auto";
    public static final String SEQUENCE="sequence";
    
    /** The Constant PROPERTY_TYPES. */
    public static final String[] ID_GENERATORS = { AUTO, SEQUENCE };

    /**
     * Checks if is type valid.
     *
     * @param type the type
     * @return true, if is type valid
     */
    public static boolean isTypeValid(final String type) {
        for (final String t : ID_GENERATORS) {
            if (t.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
    
}
