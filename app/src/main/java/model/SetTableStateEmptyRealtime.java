package model;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetTableStateEmptyRealtime {
    public static String tableIsUsing = "Đang sử dụng";
    public static void setTableIsUsing(String account, String table, String state) {

        String url = account+"/"+table;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refTable = database.getReference(url);
        refTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lặp qua tất cả child
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Tạo instance mới của ClassTable
                    if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
                        String id = childSnapshot.child("id").getValue(String.class);
                        if (id.equals(table)) {
                            childSnapshot.child( "stateEmpty").getRef().setValue(state);
                            tableIsUsing = state;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
            }
        });
    }
    //đock biến staticBooleanVariable trong real-time database
    public static void getTableIsUsingString (String account, String table) {

        String url = account+"/"+table;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(url);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Tạo instance mới của ClassTable
                    if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
                        String id = childSnapshot.child("id").getValue(String.class);
                        if (id.equals(table)) {
                            tableIsUsing=childSnapshot.child( "stateEmpty").getValue(String.class);
                            System.out.println("=====-0---- tableIsUsing: "+tableIsUsing);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
}

