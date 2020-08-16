package me.tabinol.simple.database.impl;

import me.tabinol.simple.database.SimpleDatabase;
import me.tabinol.simple.database.SimpleDatabaseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The implementation class for our database.
 */
public final class SimpleDatabaseImpl implements SimpleDatabase {

    /**
     * The committed data store.
     */
    private final DataStore dataStore;

    /**
     * The map for transaction ids and the associated data store.
     */
    private final Map<String, DataStore> transactionIdToDatastore;

    /**
     * A value id counter to know the transaction order.
     */
    private long valueIdCounter;

    public SimpleDatabaseImpl() {
        dataStore = new DataStore();
        transactionIdToDatastore = new HashMap<>();
        valueIdCounter = 0;
    }

    @Override
    public void put(final String key, final String value) {
        dataStore.put(key, value, valueIdCounter++);
    }

    @Override
    public void put(final String key, final String value, final String transactionId) throws SimpleDatabaseException {
        final DataStore transactionDataStore = getDataStoreFromTransactionId(transactionId);
        transactionDataStore.put(key, value, valueIdCounter++);
    }

    @Override
    public String get(final String key) {
        return dataStore.get(key);
    }

    @Override
    public String get(final String key, final String transactionId) throws SimpleDatabaseException {
        final DataStore transactionDataStore = getDataStoreFromTransactionId(transactionId);
        return Optional.ofNullable(transactionDataStore.get(key)).orElse(dataStore.get(key));
    }

    @Override
    public void delete(final String key) {
        dataStore.delete(key);
    }

    @Override
    public void delete(final String key, final String transactionId) throws SimpleDatabaseException {
        final DataStore transactionDataStore = getDataStoreFromTransactionId(transactionId);
        transactionDataStore.delete(key);
    }

    @Override
    public void createTransaction(final String transactionId) throws SimpleDatabaseException {
        if (transactionIdToDatastore.putIfAbsent(transactionId, new DataStore()) != null) {
            throw new SimpleDatabaseException("The transaction ID is already created [transactionId=" + transactionId + "]");
        }
    }

    @Override
    public void rollbackTransaction(final String transactionId) throws SimpleDatabaseException {
        if (transactionIdToDatastore.remove(transactionId) == null) {
            throw new SimpleDatabaseException("The non-existing transaction ID can't be removed [transactionId=" + transactionId + "]");
        }
    }

    @Override
    public void commitTransaction(final String transactionId) throws SimpleDatabaseException {
        final DataStore transactionDataStore = transactionIdToDatastore.remove(transactionId);

        if (transactionDataStore == null) {
            throw new SimpleDatabaseException("The transaction id doesn't exist [transactionId=" + transactionId + "]");
        }

        // Be sure the time (value id) is not BEFORE a committed key
        if (dataStore.isOneKeyChangeBefore(transactionDataStore)) {
            throw new SimpleDatabaseException("The transaction contains a key changed before a corresponding key in the database");
        }

        dataStore.putAllFrom(transactionDataStore);
    }

    private DataStore getDataStoreFromTransactionId(final String transactionId) throws SimpleDatabaseException {
        final DataStore transactionDataStore = transactionIdToDatastore.get(transactionId);

        if (transactionDataStore == null) {
            throw new SimpleDatabaseException("The transaction id doesn't exist [transactionId=" + transactionId + "]");
        }

        return transactionDataStore;
    }
}
