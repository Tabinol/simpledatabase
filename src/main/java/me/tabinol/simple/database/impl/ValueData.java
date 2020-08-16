package me.tabinol.simple.database.impl;

/**
 * Represents a value (String) with a value ID.
 */
final class ValueData {

    /**
     * The value.
     */
    private final String value;

    /**
     * The value id to know the transaction order.
     */
    private final long valueId;

    ValueData(final String value, final long valueId) {
        this.value = value;
        this.valueId = valueId;
    }

    String getValue() {
        return value;
    }

    long getValueId() {
        return valueId;
    }
}
