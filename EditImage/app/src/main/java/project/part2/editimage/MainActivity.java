package project.part2.editimage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    private static final int GALLERY_REQUEST = 1314;
    private static final int REQUEST_TAKE_PHOTO = 1;

    RelativeLayout rl;
    String photoPath;
    static ImageView imageView;
    Bitmap copyBmp;  // allow to reset imageView

    // position of imageView
    float posx;
    float posy;

    boolean zoom = false;
    double d;
    // position of two fingers
    float x0 = 0, x1 = 0, y0 = 0, y1 = 0;

    private GestureDetector gd;

    static Toolbar toolbar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
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
        }

        imageView = findViewById(R.id.imageView);
        gd = new GestureDetector(this, new Gesture());
        imageView.setOnTouchListener(touchListener);
        posx = imageView.getX();
        posy = imageView.getY();

        final View layoutMain = findViewById(R.id.layout_main);
        rl = (RelativeLayout) findViewById(R.id.imageViewRoot);

        toolbar = findViewById(R.id.toolbarMenu);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_picture_from_camera :
                        openCamera();
                        return true;
                    case R.id.action_picture_from_gallery :
                        openGallery();
                        return true;
                    case R.id.action_save_photo :
                        savePicture();
                        return true;
                    case R.id.action_cancel :
                        cancel();
                        return true;
                    case R.id.action_theme :
                        changeTheme(layoutMain);
                        return true;
                    default:
                        return MainActivity.super.onOptionsItemSelected(menuItem);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeFragment(new MenuFragment());
    }

    public void changeTheme(View layoutMain){
        final Switch mSwitch = ((Switch) toolbar.getMenu().findItem(R.id.action_theme).getActionView());
        if(mSwitch.isChecked()){
            mSwitch.setChecked(false);
            toolbar.setBackgroundResource(R.color.colorPrimaryDark);
            layoutMain.setBackgroundResource(R.color.colorBackgroundDark);
        }else {
            mSwitch.setChecked(true);
            toolbar.setBackgroundResource(R.color.colorPrimaryLight);
            layoutMain.setBackgroundResource(R.color.colorBackgroundLight);
        }
    }

    public void cancel(){
        if (imageView.getDrawable() != null){
            imageView.setImageBitmap(copyBmp);
            resetImgView();
            Toast.makeText(this, "Image reset", Toast.LENGTH_SHORT).show();
            changeFragment(new MenuFragment());
        }
    }

    public void changeFragment(Fragment fragment){
        if (imageView.getDrawable() == null) {
            Alert();
            fragment = new MenuFragment();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activityMainFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbar.getMenu().findItem(R.id.action_theme).setActionView(new Switch(getContext()));
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
                Uri photoURI = FileProvider.getUriForFile(this, "project.part2.editimage.fileprovider", photo);
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
            resetImgView();

            Bitmap bmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            copyBmp = bmp;

            File photo = null;
            try{
                photo = createImageFile();
            }
            catch (Exception e)
            { }

            try (FileOutputStream out = new FileOutputStream(photo)) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            }
            catch (Exception e)
            { }
        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            File photo = new File(photoPath);
            try{
                /* Get the photo */
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(photo));
                copyBmp = bmp;
                if (bmp != null) {
                    imageView.setImageBitmap(bmp);
                    resetImgView();
                }
            }
            catch (Exception e)
            { }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            }
        }
    }

    private void savePicture() {
        try {
            View rootView = findViewById(R.id.imageViewRoot);  //android.R.id.content
            Bitmap bmp = viewToBitmap(rootView);

            FileOutputStream outStream;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/editImage");
            dir.mkdirs();
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File outFile = new File(dir, fileName);
            outStream = new FileOutputStream(outFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            galleryAddPic(outFile.getAbsolutePath());
            Toast.makeText(this, "Image saved",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        { }
    }

    private void galleryAddPic(String Path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(Path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void Alert() {
        AlertDialog msg = new AlertDialog.Builder(this).create();
        msg.setTitle("Attention !");
        msg.setMessage("Veuillez ouvrir une photo depuis la galerie ou prendre une photo avec la caméra pour continuer !");
        msg.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        msg.show();
    }

    public static Context getContext() {
        return MainActivity.context;
    }

    public static double dist(float x1, float x2, float y1, float y2)
    {
        float a = x1-x2;
        float b = y1 - y2;

        return Math.sqrt(a*a + b*b);
    }

    public void resetImgView() {
        imageView.setScaleX(1);
        imageView.setScaleY(1);
        imageView.scrollTo(0, 0);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener(){
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch(e.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // un doigt
                    x0 = e.getX();
                    y0 = e.getY();
                    break;
                case MotionEvent.ACTION_UP: // le doigt se lève
                    break;
                case MotionEvent.ACTION_POINTER_UP: { // deuxième doigt se lève
                    zoom = false;
                    break;
                }
                case MotionEvent.ACTION_POINTER_DOWN: // deux doigts
                    x0 = e.getX(0);
                    y0 = e.getY(0);
                    x1 = e.getX(1);
                    y1 = e.getY(1);
                    zoom = true;
                    d = dist(x0, x1, y0, y1);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (zoom) {
                        try{
                            if (dist(x0, e.getX(1), y0, e.getY(1)) > d) {
                                imageView.setScaleX(imageView.getScaleX() + 0.025f);
                                imageView.setScaleY(imageView.getScaleY() + 0.025f);
                                rl.getLayoutParams().height = (int)(imageView.getHeight() * imageView.getScaleY());
                                rl.getLayoutParams().width = (int)(imageView.getWidth() * imageView.getScaleX());
                                rl.requestLayout();
                            }
                            if (dist(x0, e.getX(1), y0, e.getY(1)) < d) {
                                imageView.setScaleX(imageView.getScaleX() - 0.025f);
                                imageView.setScaleY(imageView.getScaleY() - 0.025f);

                                rl.getLayoutParams().height = (int)(imageView.getHeight() * imageView.getScaleY());
                                rl.getLayoutParams().width = (int)(imageView.getWidth() * imageView.getScaleX());
                                rl.requestLayout();
                            }
                            if (imageView.getScaleX() > 2.5) {
                                imageView.setScaleX(2.5f);
                                imageView.setScaleY(2.5f);
                            }
                            if(imageView.getScaleX() <= 1){
                                resetImgView();
                            }
                        }
                        catch (Exception ex)
                        { }
                    }
            }
            return gd.onTouchEvent(e);
        }
    };

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT); // background of bitmap
        view.draw(canvas);
        return bitmap;
    }
}