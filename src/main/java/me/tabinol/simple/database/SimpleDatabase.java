package me.tabinol.simple.database;

import me.tabinol.simple.database.impl.SimpleDatabaseImpl;

/**
 * The Simple Database API.
 */
public interface SimpleDatabase {

    /**
     * Creates a new instance of simple database.
     *
     * @return the simple database new instance
     */
    static SimpleDatabase newInstance() {
        return new SimpleDatabaseImpl();
    }

    /**
     * Set the variable “key” to the provided “value".
     *
     * @param key   the key
     * @param value the value
     */
    void put(String key, String value);

    /**
     * Set the variable “key” to the provided “value” within the transaction with ID “transactionId”.
     *
     * @param key           the key
     * @param value         the value
     * @param transactionId the transaction id
     * @throws SimpleDatabaseException the exception on failure
     */
    void put(String key, String value, String transactionId) throws SimpleDatabaseException;

    /**
     * Returns the value associated with “key”.
     *
     * @param key the key
     * @return the value associated with the key or null
     */
    String get(String key);

    /**
     * Returns the value associated with “key” within the transaction with ID “transactionId".
     *
     * @param key           the key
     * @param transactionId the transaction id
     * @return the value associated with the key within this transaction or null
     * @throws SimpleDatabaseException the exception on failure
     */
    String get(String key, String transactionId) throws SimpleDatabaseException;

    /**
     * Remove the value associated with “key".
     *
     * @param key the key
     */
    void delete(String key);

    /**
     * Remove the value associated with “key” within the transaction with ID “transactionId”.
     *
     * @param key           the key
     * @param transactionId the transaction id
     * @throws SimpleDatabaseException the exception on failure
     */
    void delete(String key, String transactionId) throws SimpleDatabaseException;

    /**
     * Starts a transaction with the specified ID. The ID must not be an activetransaction ID.
     *
     * @param transactionId the transaction id
     * @throws SimpleDatabaseException the exception on failure
     */
    void createTransaction(String transactionId) throws SimpleDatabaseException;

    /**
     * Aborts the transaction and invalidates the transaction with the specified transaction ID.
     *
     * @param transactionId the transaction id
     * @throws SimpleDatabaseException the exception on failure
     */
    void rollbackTransaction(String transactionId) throws SimpleDatabaseException;

    /**
     * Commits the transaction and invalidates the ID. If there is a conflict (meaning the transaction attempts to
     * change a value for a key that was mutated after the transaction was created), the transaction always fails with
     * an exception or an error is returned.
     *
     * @param transactionId the transaction id
     * @throws SimpleDatabaseException the exception on failure
     */
    void commitTransaction(String transactionId) throws SimpleDatabaseException;
}
