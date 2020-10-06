package com.example.velocam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;


import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;

import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 101;
    private static final int PICK_IMAGE_REQUEST = 102;
    private static final int REQUEST_CODE_CROP_IMAGE = 103;
    private Bitmap cameraBitmap;
    private ImageView imgPro, imgUploadPro;
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "TAG";
    EditText first,last,address,email,company_code,password,designation,company_start;
    //  <!--    ($first_name,$last_name,$phone,$address,$email,$company_code,$password,$designation,$company_start)-->
    Button register,location;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    private Wave mWaveDrawable;
    private Circle mCircleDrawable;
    private ChasingDots mChasingDotsDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
//        userID = fAuth.getCurrentUser().getUid();
        register=(Button) findViewById(R.id.register_admin);
        location=(Button) findViewById(R.id.browse_Location);
        first=findViewById(R.id.first);
        company_code=findViewById(R.id.code);
        last=findViewById(R.id.last);
        address=findViewById(R.id.address);
        email=findViewById(R.id.email_reg);
        password=findViewById(R.id.password);
        designation=findViewById(R.id.designation);
        company_start=findViewById(R.id.start);
        imgPro = (ImageView) findViewById(R.id.imageView);
        imgUploadPro = (ImageView) findViewById(R.id.imgUploadProfile);
        //  velocamAPI= Common.getApi();
        addListener();


    }

    private void addListener() {
        register.setOnClickListener(this::onClick);
        imgUploadPro.setOnClickListener(this::onClick);
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.register_admin:
                if (first.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "First Name cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    first.setError("First Name cannot be Blank");

                    return;

                }
                else if (last.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Last Name cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    last.setError("Last Name cannot be Blank");

                    return;

                }
                else if (company_start.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Company Start date cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    last.setError("Company Start  cannot be Blank");

                    return;

                }
                else if (designation.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Designation cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    last.setError("Designation cannot be Blank");

                    return;

                }
                else if (company_code.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Company Code cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    last.setError("Company Code cannot be Blank");

                    return;

                }
                else if (email.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Email cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    last.setError("Email cannot be Blank");

                    return;

                }
                else if (address.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Address cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    last.setError("Address cannot be Blank");

                    return;

                }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                        email.getText().toString()).matches()) {
                    // Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Invalid Email",
                            Toast.LENGTH_LONG).show();
                    email.setError("Invalid Email");

                    return;

                } else if (password.getText().length() <= 7) {

                    Toast.makeText(getApplicationContext(),
                            "Password must be 8 characters above",
                            Toast.LENGTH_LONG).show();
                    password.setError("Password must be 8 characters above");

                    return;

                }

                else {

                    insertData();
                }

                break;

            case R.id.browse_Location:
//
//                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(i);
//                finish();

                break;

            case R.id.imgUploadProfile:
                selectImage();
                // showFileChooser();

                break;

            default:
                break;
        }

    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {

                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {

                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File out = Environment.getExternalStorageDirectory();
        out = new File(out, "abc");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void insertData() {

        final String fname=first.getText().toString().trim();
        final     String lname=last.getText().toString().trim();
        final       String addr=address.getText().toString().trim();
        final       String em=email.getText().toString().trim();
        final        String co=company_code.getText().toString().trim();
        final        String pass=password.getText().toString().trim();
        final        String des=designation.getText().toString().trim();
        final        String com_s=company_start.getText().toString();
        final        String phone=fAuth.getCurrentUser().getPhoneNumber();

        BitmapDrawable drawable = (BitmapDrawable) imgPro.getDrawable();
        Bitmap profileBitmap = drawable.getBitmap();

        Bitmap bitmap = null;

        if (cameraBitmap == null) {
            bitmap = profileBitmap;
        } else {
            bitmap = cameraBitmap;
        }

        final String image = getStringImage(bitmap);


        final ProgressDialog dialog;
        /**
         * Progress Dialog for User Interaction
         */
        dialog = new ProgressDialog(DetailsActivity.this);
        dialog.setTitle("Loding");
        dialog.setMessage("Please Wait...");
        dialog.show();

        //Creating an object of our api interface
        ApiInterface api =ApiClient.getApiService();

        Call<ResponseBody> call = api.registerUser(fname,lname,addr,em, co,pass,des,com_s,phone,image);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Dismiss Dialog
                    dialog.dismiss();
                    try {


                        if(response.body().toString().contains("User Register Successfully"))
                        {
                            Toast.makeText(DetailsActivity.this, response.body().string().toString(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(DetailsActivity.this,
                                    LoginActivity.class);
                            startActivity(i);

                            finish();

                        }
                        else
                        {
                            Toast.makeText(DetailsActivity.this, response.body().string().toString(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(DetailsActivity.this,
                                    DetailsActivity.class);
                            startActivity(i);

                            finish();
                        }




                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Dismiss Dialog
                dialog.dismiss();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case PICK_IMAGE_REQUEST:

                    Uri u = data.getData();
                    performCrop(u);

                    break;

                case REQUEST_CAMERA:

                    File file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "abc");
                    // Crop the captured image using an other intent
                    try {
                        /* the user's device may not support cropping */
                        performCrop(Uri.fromFile(file));
                    } catch (ActivityNotFoundException aNFE) {
                        // display an error message if user device doesn't support
                        String errorMessage = "Sorry - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;

                case REQUEST_CODE_CROP_IMAGE:

                    if (resultCode == Activity.RESULT_OK) {
                        Bundle extras = data.getExtras();
                        cameraBitmap = extras.getParcelable("data");
                        imgPro.setImageBitmap(cameraBitmap);
                    }
                    break;
            }
        }
    }


    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQUEST_CODE_CROP_IMAGE);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}

