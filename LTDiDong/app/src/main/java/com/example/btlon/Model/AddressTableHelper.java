package com.example.btlon.Model;

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


    // Thêm địa chỉ cho người dùng cụ thể
    public boolean addNewAddressForUser(int userId, String newAddress, boolean isDefault) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_ADDRESS, newAddress);
        values.put(COL_IS_DEFAULT, isDefault ? 1 : 0); // Lưu trạng thái mặc định chính xác

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
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
    public void setDefaultAddress(int addressId, int userId) {
        // Bỏ trạng thái mặc định của tất cả các địa chỉ khác
        ContentValues values = new ContentValues();
        values.put(COL_IS_DEFAULT, 0);
        update(values, COL_USER_ID + "=?", new String[]{String.valueOf(userId)});

        // Đặt địa chỉ hiện tại làm mặc định
        values.put(COL_IS_DEFAULT, 1);
        update(values, COL_ADDRESS_ID + "=?", new String[]{String.valueOf(addressId)});
    }



    // Lấy tất cả địa chỉ của người dùng hiện tại
    public ArrayList<Address> getAllAddressesForUser(int userId) {
        ArrayList<Address> addresses = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Truy vấn chỉ lấy địa chỉ của người dùng hiện tại (dựa trên user_id)
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
    // Lấy địa chỉ mặc định của một người dùng
    public Address getDefaultAddressForUser(int userId) {
        Address defaultAddress = null;
        Cursor cursor = null;

        try {
            // Query to get the default address (isDefault = 1) for the user (user_id)
            String selection = COL_USER_ID + "=? AND " + COL_IS_DEFAULT + "=?";
            String[] selectionArgs = {String.valueOf(userId), "1"};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // If a default address is found, map it to an Address object
            if (cursor != null && cursor.moveToFirst()) {
                defaultAddress = mapCursorToEntity(cursor);
            }
        } catch (Exception e) {
            Log.e("AddressTableHelper", "Error fetching default address: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // If no default address is found, return a new Address with "Địa chỉ trống"
        if (defaultAddress == null) {
            defaultAddress = new Address();
            defaultAddress.setAddress("Địa chỉ trống");
        }

        Log.d("AddressTableHelper", "Default address for userId " + userId + ": " + defaultAddress.getAddress());
        return defaultAddress;
    }


}
