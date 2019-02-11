package part2.tech.projet.u.edit.image;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1314;
    private static final int REQUEST_TAKE_PHOTO = 1;

    String photoPath;
    private Uri photoUri;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Ask permission WRITE_EXTERNAL_STORAGE */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_TAKE_PHOTO);
            }
        } else {
        }


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMenu);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.imageView);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_picture_from_camera :
                         openCamera();
                    case R.id.action_picture_from_gallery :
                         openGallery();
                    default:
                        return MainActivity.super.onOptionsItemSelected(menuItem);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photo = null;
            try {
                photo = createImageFile();
            } catch (IOException ex)
            { }
            if (photo != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "part2.tech.projet.u.edit.image.fileprovider", photo);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + time + "_";
        File storageDir = null;
        try{
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera");
            if(!storageDir.exists())
                storageDir.mkdirs();
        }
        catch (Exception e)
        { }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        photoPath = image.getAbsolutePath();
        return image;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); //, MediaStore.Images.Media.INTERNAL_CONTENT_URI
        startActivityForResult(gallery, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {  // everything processed successfully and hearing back from the image gallery
            Uri imageUri = data.getData();  // the address of the image on the SD Card
            imageView.setImageURI(imageUri);

            Bitmap bmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

            File photo = null;
            try{
               photo = createImageFile();
            }
            catch (Exception e)
            { }

            try (FileOutputStream out = new FileOutputStream(photo)) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                galleryAddPic(photo.toString());
            }
            catch (Exception e)
            { }
        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            File photo = new File(photoPath);
            try{
                /* Get the photo */
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(photo));
                if (bmp != null)
                {
                    imageView.setImageBitmap(bmp);
                    galleryAddPic(photoPath);
                }
            }
            catch (Exception e)
            { }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    private void galleryAddPic(String Path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(Path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
