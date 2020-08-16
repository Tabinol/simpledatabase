package me.tabinol.simple.database.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a data store used for the main database or a transaction id.
 */
final class DataStore {

    private final Map<String, ValueData> keyToValueData;

    DataStore() {
        keyToValueData = new HashMap<>();
    }

    void put(final String key, final String value, final long valueId) {
        keyToValueData.put(key, new ValueData(value, valueId));
    }

    String get(final String key) {
        return Optional.ofNullable(keyToValueData.get(key)).map(ValueData::getValue).orElse(null);
    }

    void delete(final String key) {
        keyToValueData.remove(key);
    }

    boolean isOneKeyChangeBefore(final DataStore transactionDatastore) {
        return transactionDatastore.keyToValueData.entrySet().stream().anyMatch(this::isKeyChangeBefore);
    }

    void putAllFrom(final DataStore transactionDatastore) {
        keyToValueData.putAll(transactionDatastore.keyToValueData);
    }

    private boolean isKeyChangeBefore(final Map.Entry<String, ValueData> keyToValueDataEntry) {
        final String key = keyToValueDataEntry.getKey();
        final ValueData valueData = keyToValueDataEntry.getValue();
        final ValueData duplicateValueData = keyToValueData.get(key);

        if (duplicateValueData != null) {
            return valueData.getValueId() < duplicateValueData.getValueId();
        }

        return false;
    }
}
