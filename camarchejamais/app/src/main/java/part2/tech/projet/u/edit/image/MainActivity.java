package part2.tech.projet.u.edit.image;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> images = new ArrayList<Integer>(Arrays.asList(
            R.drawable.peppers, R.drawable.mer,
            R.drawable.landscape, R.drawable.image_test,
            R.drawable.image1, R.drawable.image2,
            R.drawable.image3, R.drawable.image4,
            R.drawable.image5, R.drawable.image6,
            R.drawable.image8, R.drawable.image9));

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter recyclerAdapter;
    private static final int SELECTED_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMenu);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // layout manager determines the position of elements
        layoutManager = new GridLayoutManager(this, 3);   // 2 columns
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // adapter display the contents
        recyclerAdapter = new RecyclerAdapter(images);
        recyclerView.setAdapter(recyclerAdapter);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_search_meme :
                        // ...
                    case R.id.action_picture_from_camera :
                        // openCamera();
                    case R.id.action_picture_from_gallery :
                        // openGallery();
                    default:
                        return MainActivity.super.onOptionsItemSelected(menuItem);
                }
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 0);
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); //, MediaStore.Images.Media.INTERNAL_CONTENT_URI
        startActivityForResult(gallery, SELECTED_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {  // everything processed successfully and hearing back from the image gallery
            Uri imageUri = data.getData();  // the address of the image on the SD Card
            images.add(R.drawable.image_test);
        }
    }
}
