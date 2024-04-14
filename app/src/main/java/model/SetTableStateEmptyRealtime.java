package model;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetTableStateEmptyRealtime {
    public static String tableIsUsing = "Đang sử dụng";
    public static void setTableIsUsing(String account, String table, String state) {
        System.out.println("=====-0---- tableIsUsing: "+tableIsUsing);
        String url = account+"/"+table;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refTable = database.getReference(url);
        refTable.child("stateEmpty").setValue(state);
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

