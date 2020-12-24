package com.propertyeager.realestate.Fragment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.gson.Gson;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Retrofit.WebServiceFactory;

import com.propertyeager.realestate.Views.ProgressLoader;
import com.propertyeager.realestate.Wrapper.GetAgentSignup.GetAgentSignupWrapper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;
import static com.propertyeager.realestate.Utils.Permissions.hasPermissions;

public class AgentSignupFragment extends Fragment {



    ProgressLoader progressBar;

    Uri contentUri;

    AwesomeValidation mAwesomeValidation = new AwesomeValidation(COLORATION);

    //storage permission code
    private static final int PERMISSION_REQUEST_CODE = 200;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION

    };

    protected boolean _taken = true;
    File sdImageMainDirectory;
    protected static final String PHOTO_TAKEN = "photo_taken";

    EditText ed_name, ed_number, ed_email, ed_password, ed_agency_name, ed_description;
    ImageView img_logo;

    TextView upload_msg;
    File logo_file;
    MultipartBody.Part logo_image_part;

    LinearLayout ly_btn_login;

    public static AgentSignupFragment newInstance() {
        return new AgentSignupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.agent_signup_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        progressBar = getView().findViewById(R.id.progressBar);
        //
        ed_name = getView().findViewById(R.id.ed_username);
        ed_number = getView().findViewById(R.id.ed_number);
        ed_email = getView().findViewById(R.id.ed_email);
        ed_password = getView().findViewById(R.id.ed_password);

        ed_agency_name = getView().findViewById(R.id.ed_agency_name);
        ed_description = getView().findViewById(R.id.ed_description);
        //
        img_logo = getView().findViewById(R.id.img_logo);
        upload_msg = getView().findViewById(R.id.upload_msg);
        //
        ly_btn_login = getView().findViewById(R.id.ly_btn_login);

        mAwesomeValidation.setColor(Color.YELLOW);

        mAwesomeValidation.addValidation(getActivity(), R.id.ed_username, "[a-zA-Z\\s]+", R.string.err_name);
       // mAwesomeValidation.addValidation(getActivity(), R.id.fa_image, "^null|$", R.string.error_empty_image);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_password, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_email, Patterns.EMAIL_ADDRESS, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_password, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_confirm_password, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_agency_name, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_description, RegexTemplate.NOT_EMPTY, R.string.error_empty);

        //mAwesomeValidation.addValidation(getActivity(), R.id.ed_firstap_cnic, "^[0-9+]{5}-[0-9+]{7}-[0-9]{1}$", R.string.cnic_pattern);



        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    selectImage(getContext(), 0, 1);
                } else {
                    requestPermission();
                    selectImage(getContext(), 0, 1);
                }
            }
        });

        ly_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAwesomeValidation.validate() && img_logo.getDrawable() !=null ) {

                    AgentSignup();

                } else {
                    Toast.makeText(getActivity(), "Please fill all the fields or select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {

            super.onCreate(savedInstanceState);
            File root = new File(Environment
                    .getExternalStorageDirectory()
                    + File.separator + "Propertyeager" + File.separator);
            root.mkdirs();
            String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
            sdImageMainDirectory = new File(root, "IMAGE_" + millisecond);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AgentSignup() {

        final RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), logo_file);
        logo_image_part = MultipartBody.Part.createFormData("image", logo_file.getName(), requestBody1);

        progressBar.setVisibility(View.VISIBLE);
        WebServiceFactory.getInstance().GET_AGENT_SIGNUP_WRAPPER_CALL(
                RequestBody.create(MediaType.parse("*/*"), ed_name.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_number.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_email.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_password.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_agency_name.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_description.getText().toString().trim()),
                logo_image_part)
                .enqueue(new Callback<GetAgentSignupWrapper>() {
                    @Override
                    public void onResponse(Call<GetAgentSignupWrapper> call, Response<GetAgentSignupWrapper> response) {
                        try {
                            if (response.body() != null) {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                if (response.body().getResult().getFlag()) {
                                    Fragment agentLoginFragment = new AgentLoginFragment();
                                    // consider using Java coding conventions (upper first char class names!!!)
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                    // Replace whatever is in the fragment_container view with this fragment,
                                    // and add the transaction to the back stack
                                    transaction.replace(R.id.container, agentLoginFragment);


                                    // Commit the transaction
                                    transaction.commit();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), response.body().getResult().getMsg(), Toast.LENGTH_LONG).show();
                                    //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), response.body().getResult().getMsg(), Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetAgentSignupWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Invalid Email or Password provided", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private boolean checkPermission() {
        // Permission is not granted
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(PHOTO_TAKEN, _taken);
    }

   /* @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean(PHOTO_TAKEN)) {
            _taken = true;
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void selectImage(Context context, final int cameraRequestCode, final int galleryRequestCode) {
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL);
        } else {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose your profile picture");

            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals("Take Photo")) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                        startActivityForResult(takePicture, cameraRequestCode);

                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, galleryRequestCode);

                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    public File convertBitmaptoFile(Bitmap bitmap, String filename) throws IOException {
        File f = new File(requireContext().getCacheDir(), filename);
        f.createNewFile();

//Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:

                  /*  try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                       // logo_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));

                       // img_logo.setImageURI(Uri.parse(logo_file.getAbsolutePath()));
                        img_logo.setImageURI(Uri.parse(getPath(Uri.fromFile(logo_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")))))));
                        Log.e("logo file path","" +  logo_file.getPath());
                        Log.e("logo file absolute path","" +  logo_file.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String currentTime = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            logo_file = convertBitmaptoFile(selectedImage, "image_" + currentTime  + ".jpg");
                            Log.e("IMAGE", "fa_image :" + logo_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        img_logo.setImageBitmap(selectedImage);

                    }
                    // fa_image.setImageURI(Uri.parse(fa_image_file.getPath()));

                    break;
                case 1:
                  /*  if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = Uri.parse(data.getData().getEncodedPath());
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                logo_file = new File(picturePath);
                                Log.e("IMAGE", "ja_image :" + logo_file);
                                img_logo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }*/
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                logo_file = new File(picturePath);
                                Log.e("IMAGE", "ja_image :" + logo_file);
                                img_logo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;

            }
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    Uri picCollection = MediaStore.Images.Media
                            .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                    ContentValues picDetail = new ContentValues();
                    picDetail.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
                    picDetail.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    picDetail.put(MediaStore.Images.Media.RELATIVE_PATH,"DCIM/" + UUID.randomUUID().toString());
                    picDetail.put(MediaStore.Images.Media.IS_PENDING,1);
                    Uri finaluri = resolver.insert(picCollection, picDetail);
                    picDetail.clear();
                    picDetail.put(MediaStore.Images.Media.IS_PENDING, 0);
                    resolver.update(picCollection, picDetail, null, null);
                    return finaluri;
                }else {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                }

            } else {
                return null;
            }
        }
    }

    private Bitmap fileToBitmap(String path) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap bitmapImage = BitmapFactory.decodeFile(path);
        int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);

        return Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true);
    }

    public  String getPath( final Uri uri) {
        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String selection = null;
        String[] selectionArgs = null;
        // DocumentProvider
        if (isKitKat ) {
            // ExternalStorageProvider

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                String fullPath = getPathFromExtSD(split);
                if (fullPath != "") {
                    return fullPath;
                } else {
                    return null;
                }
            }


            // DownloadsProvider

            if (isDownloadsDocument(uri)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final String id;
                    Cursor cursor = null;
                    try {
                        cursor = getContext().getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String fileName = cursor.getString(0);
                            String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                            if (!TextUtils.isEmpty(path)) {
                                return path;
                            }
                        }
                    }
                    finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    id = DocumentsContract.getDocumentId(uri);
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        String[] contentUriPrefixesToTry = new String[]{
                                "content://downloads/public_downloads",
                                "content://downloads/my_downloads"
                        };
                        for (String contentUriPrefix : contentUriPrefixesToTry) {
                            try {
                                final Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));


                                return getDataColumn(getContext(), contentUri, null, null);
                            } catch (NumberFormatException e) {
                                //In Android 8 and Android P the id is not a number
                                return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                            }
                        }


                    }
                }
                else {
                    final String id = DocumentsContract.getDocumentId(uri);

                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    }
                    catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (contentUri != null) {

                        return getDataColumn(getContext(), contentUri, null, null);
                    }
                }
            }


            // MediaProvider
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;

                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};


                return getDataColumn(getContext(), contentUri, selection,
                        selectionArgs);
            }

            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri);
            }

            if(isWhatsAppFile(uri)){
                return getFilePathForWhatsApp(uri);
            }


            if ("content".equalsIgnoreCase(uri.getScheme())) {

                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                if (isGoogleDriveUri(uri)) {
                    return getDriveFilePath(uri);
                }
                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {

                    // return getFilePathFromURI(context,uri);
                    return copyFileToInternalStorage(uri,"userfiles");
                    // return getRealPathFromURI(context,uri);
                }
                else
                {
                    return getDataColumn(getContext(), uri, null, null);
                }

            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        else {

            if(isWhatsAppFile(uri)){
                return getFilePathForWhatsApp(uri);
            }

            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {
                        MediaStore.Images.Media.DATA
                };
                Cursor cursor = null;
                try {
                    cursor = getContext().getContentResolver()
                            .query(uri, projection, selection, selectionArgs, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }




        return null;
    }

    private  boolean fileExists(String filePath) {
        File file = new File(filePath);

        return file.exists();
    }

    private String getPathFromExtSD(String[] pathData) {
        final String type = pathData[0];
        final String relativePath = "/" + pathData[1];
        String fullPath = "";

        // on my Sony devices (4.4.4 & 5.1.1), `type` is a dynamic string
        // something like "71F8-2C0A", some kind of unique id per storage
        // don't know any API that can get the root path of that storage based on its id.
        //
        // so no "primary" type, but let the check here for other devices
        if ("primary".equalsIgnoreCase(type)) {
            fullPath = Environment.getExternalStorageDirectory() + relativePath;
            if (fileExists(fullPath)) {
                return fullPath;
            }
        }

        // Environment.isExternalStorageRemovable() is `true` for external and internal storage
        // so we cannot relay on it.
        //
        // instead, for each possible path, check if file exists
        // we'll start with secondary storage as this could be our (physically) removable sd card
        fullPath = System.getenv("SECONDARY_STORAGE") + relativePath;
        if (fileExists(fullPath)) {
            return fullPath;
        }

        fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath;
        if (fileExists(fullPath)) {
            return fullPath;
        }

        return fullPath;
    }

    private String getDriveFilePath(Uri uri) {
        Uri returnUri = uri;
        Cursor returnCursor = getContext().getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(getContext().getCacheDir(), name);
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    /***
     * Used for Android Q+
     * @param uri
     * @param newDirName if you want to create a directory, you can set this variable
     * @return
     */
    private String copyFileToInternalStorage(Uri uri,String newDirName) {
        Uri returnUri = uri;

        Cursor returnCursor = getContext().getContentResolver().query(returnUri, new String[]{
                OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE
        }, null, null, null);


        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));

        File output;
        if(!newDirName.equals("")) {
            File dir = new File(getContext().getFilesDir() + "/" + newDirName);
            if (!dir.exists()) {
                dir.mkdir();
            }
            output = new File(getContext().getFilesDir() + "/" + newDirName + "/" + name);
        }
        else{
            output = new File(getContext().getFilesDir() + "/" + name);
        }
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(output);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();

        }
        catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }

        return output.getPath();
    }

    private String getFilePathForWhatsApp(Uri uri){
        return  copyFileToInternalStorage(uri,"whatsapp");
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    private  boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private  boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private  boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private  boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public boolean isWhatsAppFile(Uri uri){
        return "com.whatsapp.provider.media".equals(uri.getAuthority());
    }

    private  boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }
}