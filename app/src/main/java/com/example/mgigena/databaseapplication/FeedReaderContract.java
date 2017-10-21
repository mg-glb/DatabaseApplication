package com.example.mgigena.databaseapplication;

import android.provider.BaseColumns;

/**
 * This class represents the schema of the Contacts table.
 */
final class FeedReaderContract {

    private FeedReaderContract() {}

    static class FeedEntry implements BaseColumns {
        static final String TABLE_NAME = "contacts";
        static final String CONTACTS_COLUMN_NAME = "name";
        static final String CONTACTS_COLUMN_EMAIL = "email";
        static final String CONTACTS_COLUMN_STREET = "street";
        static final String CONTACTS_COLUMN_CITY = "place";
        static final String CONTACTS_COLUMN_PHONE = "phone";
    }
}
