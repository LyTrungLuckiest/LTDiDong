package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class AddressTableHelper extends BaseTableHelper<Address> {
    private static final String TABLE_NAME = "Addresses";
    private static final String COL_ADDRESS_ID = "address_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_ADDRESS = "address";
    private static final String COL_IS_DEFAULT = "isDefault";
    private SqliteHelper sqliteHelper;

    public AddressTableHelper(Context context) {
        super(context);
        this.sqliteHelper = new SqliteHelper(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Address mapCursorToEntity(Cursor cursor) {

        int addressIdIndex = cursor.getColumnIndex(COL_ADDRESS_ID);
        int userIdIndex = cursor.getColumnIndex(COL_USER_ID);
        int addressIndex = cursor.getColumnIndex(COL_ADDRESS);
        int isDefaultIndex = cursor.getColumnIndex(COL_IS_DEFAULT);


        if (addressIdIndex >= 0 && userIdIndex >= 0 && addressIndex >= 0 && isDefaultIndex >= 0) {
            int addressId = cursor.getInt(addressIdIndex);
            int userId = cursor.getInt(userIdIndex);
            String address = cursor.getString(addressIndex);
            boolean isDefault = cursor.getInt(isDefaultIndex) == 1;
            return new Address(addressId, userId, address, isDefault);
        } else {

            Log.e("AddressTableHelper", "One or more columns are missing in the cursor");
            return null;
        }
    }


    public boolean addAddressForUser(int userId, String newAddress) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_ADDRESS, newAddress);
        values.put(COL_IS_DEFAULT, 0);


        Log.d("AddressTableHelper", "Thêm địa chỉ mới cho userId " + userId + ": " + newAddress);

        return insert(values);
    }


    // Update address
    public boolean updateAddress(Address address) {
        ContentValues values = new ContentValues();
        values.put(COL_ADDRESS, address.getAddress());
        values.put(COL_IS_DEFAULT, address.isDefault() ? 1 : 0);
        return update(values, COL_ADDRESS_ID + "=?", new String[]{String.valueOf(address.getId())});  // Use COL_ADDRESS_ID here
    }

    public boolean deleteAddress(int addressId) {
        return delete(COL_ADDRESS_ID + "=?", new String[]{String.valueOf(addressId)});  // Use COL_ADDRESS_ID here
    }


    public ArrayList<Address> getAllAddressesForUser(int userId) {
        ArrayList<Address> addresses = new ArrayList<>();
        Cursor cursor = null;

        try {

            String selection = COL_USER_ID + "=?";
            String[] selectionArgs = {String.valueOf(userId)};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Address address = mapCursorToEntity(cursor);
                    if (address != null) {
                        addresses.add(address);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("AddressTableHelper", "Error fetching addresses: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d("AddressTableHelper", "Loaded " + addresses.size() + " addresses for userId " + userId);
        return addresses;
    }

}
