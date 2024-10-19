package com.gokulsundar4545.dropu;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.HashMap;
import java.util.Map;

public class ContactHelper {

    public static Map<String, String> getContacts(ContentResolver contentResolver) {
        Map<String, String> contactsMap = new HashMap<>();

        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsMap.put(contactNumber, contactName);
            }
            cursor.close();
        }

        return contactsMap;
    }
}

