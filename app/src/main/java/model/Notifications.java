//package model;
//
//
//import static androidx.core.content.ContextCompat.getSystemService;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.RemoteMessage;
//
//public class Notifications {
//    private void sendNotificationAndInAppMessaging() {
//        // Gửi thông báo
//        String channelId = "my_channel_id";
//        String channelName = "My Channel";
//        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);
//
//        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("AAAA....")
//                .setNotification(new NotificationCompat.Builder(this, channelId)
//                        .setContentTitle("Tiêu đề thông báo")
//                        .setContentText("Nội dung thông báo")
//                        .setSmallIcon(R.drawable.ic_notification)
//                        .build())
//                .build());
//
//        // Hiển thị In-App Messaging
//        FirebaseInAppMessagingDisplayCampaign campaign = new FirebaseInAppMessagingDisplayCampaign.Builder()
//                .setCampaignId("my_campaign_id")
//                .setTitle("Tiêu đề tin nhắn")
//                .setMessage("Nội dung tin nhắn")
//                .setImageUrl("https://www.example.com/image.png")
//                .build();
//
//        FirebaseInAppMessaging.getInstance().displayCampaign(campaign);
//    }
//
//    private void showInAppMessaging() {
//        FirebaseInAppMessagingDisplayCampaign campaign = new FirebaseInAppMessagingDisplayCampaign.Builder()
//                .setCampaignId("my_campaign_id")
//                .setTitle("Tiêu đề tin nhắn")
//                .setMessage("Nội dung tin nhắn")
//                .setImageUrl("https://www.example.com/image.png")
//                .build();
//
//        FirebaseInAppMessaging.getInstance().displayCampaign(campaign);
//    }
//
//    private void showNotification() {
//        String channelId = "my_channel_id";
//        String channelName = "My Channel";
//        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);
//
//        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("AAAA....")
//                .setNotification(new NotificationCompat.Builder(this, channelId)
//                        .setContentTitle("Tiêu đề thông báo")
//                        .setContentText("Nội dung thông báo")
//                        .setSmallIcon(R.drawable.ic_notification)
//                        .build())
//                .build());
//    }
//
//
////    private static final String TAG = "Notifications";
////
////    // Gửi thông báo
////    public static void sendNotification(String title, String content) {
////        // Tạo NotificationChannel
////        String channelId = "my_channel_id";
////        String channelName = "My Channel";
////        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
////        NotificationManager notificationManager = getSystemService(NotificationManager.class);
////        notificationManager.createNotificationChannel(channel);
////
////        // Gửi thông báo
////        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("AAAA....")
////                .setNotification(new NotificationCompat.Builder(this, channelId)
////                        .setContentTitle(title)
////                        .setContentText(content)
////                        .setSmallIcon(R.drawable.ic_notification)
////                        .build())
////                .build());
////
////        Log.d(TAG, "Gửi thông báo thành công: " + title + " - " + content);
//    }
//
//    // Ví dụ sử dụng
////    public static void main(String[] args) {
////        sendNotification("Tiêu đề thông báo", "Nội dung thông báo");
////    }
////}
////public class MainActivity extends AppCompatActivity {
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        // Khởi tạo Firebase
////        FirebaseApp.initializeApp(this);
////
////        // Gửi thông báo
////        Notifications.sendNotification("Tiêu đề thông báo", "Nội dung thông báo");
////    }
////}