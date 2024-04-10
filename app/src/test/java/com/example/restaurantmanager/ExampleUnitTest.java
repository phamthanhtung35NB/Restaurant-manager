package com.example.restaurantmanager;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.restaurantmanager.FireBase.UploadImageToFirebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Mock
    FirebaseStorage firebaseStorage;

    @Mock
    StorageReference storageReference;
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testDeleteImageFromFirebase() {
        // Khởi tạo các mock object
        MockitoAnnotations.initMocks(this);

        // Giả lập việc lấy tham chiếu từ URL
        when(firebaseStorage.getReferenceFromUrl(any(String.class))).thenReturn(storageReference);

        // Gọi hàm cần kiểm thử
        UploadImageToFirebase.deleteImageFromFirebase("https://firebasestorage.googleapis.com/v0/b/restaurantmanager-f01f9.appspot.com/o/minhchung.png?alt=media&token=4f7e3798-584b-42f9-a3f3-cedca5591fb4");

        // Kiểm tra xem phương thức delete() có được gọi chưa
        verify(storageReference).delete();
    }
}