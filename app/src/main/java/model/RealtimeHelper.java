//package model;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//
//import java.util.ArrayList;
//
//import adapter.MenuAdapter;
//import model.MenuRestaurant;
//public class RealtimeHelper {
//
//
//
//    public class MainActivity extends AppCompatActivity {
//
//        TextView textView1;
//        Button button1;
//        ImageButton imageButtonOrder;
//
//        ListView listViewMenu;
//
//        ArrayList<MenuRestaurant> dataRestaurant;
//
//        MenuAdapter menuAdapter;
//        public static ArrayList<MenuRestaurant> dataOrder;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            EdgeToEdge.enable(this);
//            //setContentView truyền layout vào
//            //R là lớp tài nguyên, layout là thư mục chứa layout, activity_main là file layout
//            setContentView(R.layout.activity_main);
//
//            init();
//            addEvents();
////        readDataFromFireBase("account1");
//            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//                return insets;
//            });
//            readDataFromFireBase("account1");
//
//        }
//
//        private void addEvents() {
//            button1.setOnClickListener(v -> {
////            onClickReadData();
//
//            });
//            imageButtonOrder.setOnClickListener(v -> {
//                Intent intent = new Intent(MainActivity.this, OderActivity2.class);
//                startActivity(intent);
//            });
//
//        }
//
//        //push data account and menu to firebase
////    private void onClickWriteData() {
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference myRef = database.getReference("account1");
//////        Account string = new Account("1", "admin", "admin", "0123456789","áđâs@fads", "Hà Nội");
////        Account menu = new Account("1", "admin", "admin", "0123456789","áđâs@fads", "Hà Nội","1", "Cơm chiên", "Cơm chiên + trứng", 5000.0, "https://i.imgur.com/ikbFUzX.png");
//////        MenuRestaurant menu1 = new MenuRestaurant("2", "Cơm trắng", "Cơm", 10000, "https://i.imgur.com/ikbFUzX.png");
////        myRef.setValue(menu, new DatabaseReference.CompletionListener() {
////            @Override
////            public void onComplete(DatabaseError error, DatabaseReference ref) {
////                if (error == null) {
////                    Toast.makeText(MainActivity.this, "onComplete: success", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(MainActivity.this, "onComplete: " + error.getMessage(), Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////
////    }
//
//        //read data from firebase
//        public void readDataFromFireBase(String id){
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference(id);
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String id1 = dataSnapshot.child("id").getValue().toString();
//                    String username = dataSnapshot.child("username").getValue().toString();
//                    String password = dataSnapshot.child("password").getValue().toString();
//                    textView1.setText(id1 + " " + username + " " + password);
//                }
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    Toast.makeText(MainActivity.this, "onCancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            DatabaseReference myRef2 = database.getReference(id+"/menuRestaurant");
//            myRef2.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                        MenuRestaurant menuRestaurant = dataSnapshot1.getValue(MenuRestaurant.class);
//                        dataRestaurant.add(menuRestaurant);
//                    }
//                    menuAdapter = new MenuAdapter(MainActivity.this, R.layout.food, dataRestaurant);
//                    listViewMenu.setAdapter(menuAdapter);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    Toast.makeText(MainActivity.this, "onCancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
////        listViewMenu.setAdapter(menuAdapter);
//        }
////    public void onClickReadData() {
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference myRef = database.getReference("account1");
////        myRef.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                // This method is called once with the initial value and again
////                // whenever data at this location is updated.
//////                Account value = dataSnapshot.getValue(Account.class);
//////                Log.d(TAG, "Value is: " + value);
//////                textView1.setText(value.toString());
////            }
////
////            @Override
////            public void onCancelled(DatabaseError error) {
////                // Failed to read value
//////                Log.w(TAG, "Failed to read value.", error.toException());
////            }
////        });
////    }
//
//        private void init() {
//            textView1 = findViewById(R.id.textView1);
//            button1 = findViewById(R.id.button1);
//            imageButtonOrder = findViewById(R.id.imageButtonOrder);
//            listViewMenu = findViewById(R.id.listViewMenu);
////        listViewOrder = findViewById(R.id.listViewOrder);
//            dataRestaurant = new ArrayList<>();
//            dataOrder = new ArrayList<>();
////        listViewMenu.setAdapter(menuAdapter);
//        }
//
//
//}
