package com.propertyeager.realestate.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Retrofit.WebServiceFactory;
import com.propertyeager.realestate.Utils.ConnectionDetector;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Views.ProgressLoader;
import com.propertyeager.realestate.Wrapper.GetAgentProfileData.GetAgentProfileDataWrapper;
import com.google.android.material.snackbar.Snackbar;
import com.propertyeager.realestate.Wrapper.GetProfileEdit.GetProfileEditWrapper;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
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
import static com.propertyeager.realestate.Utils.SessionManager.KEY_AGENT_ID;


public class ProfileFragment extends Fragment {



    private ConstraintLayout ly_btn_checkin_meeting, ly_logout, ly_profile;

    ConnectionDetector cd;
    private boolean isinternetpresent = false;

    public  String currency="", decodedDescription ="";
    public byte[] data;

    AwesomeValidation mAwesomeValidation = new AwesomeValidation(COLORATION);
    //storage permission code
    private static final int PERMISSION_REQUEST_CODE = 200;

    private TextView name, email, branch_name;
    SessionManager sessionManager;

    FrameLayout ly_agency_discription, ly_agency_name;
    ProgressLoader progressBar;
    String user_type;

    File image_file;
    CircleImageView agent_image;
    private TextView agent_name;
    private EditText agent_name_2, agent_number, agency_name, agency_description;
    private ImageView edit, camera, back;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    MultipartBody.Part image_part;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agent_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        agent_name = getView().findViewById(R.id.agent_name);
        agent_name_2 = getView().findViewById(R.id.agent_name_2);
        agent_number = getView().findViewById(R.id.agent_number);
        agency_description = getView().findViewById(R.id.agency_description);
        agency_name = getView().findViewById(R.id.agency_name);
        //
        edit = getView().findViewById(R.id.edit);
        back = getView().findViewById(R.id.back);
        camera = getView().findViewById(R.id.camera);
        agent_image = getView().findViewById(R.id.agent_image);
        //
        ly_agency_discription = getView().findViewById(R.id.ly_agency_discription);
        ly_agency_name = getView().findViewById(R.id.ly_agency_name);
        //


        //  private TextView agent_name, agent_name_2, agent_number, agency_name, agency_description;
        //
        //    ImageView edit;
        sessionManager = new SessionManager(getContext());
        //

      /*  agent_name.setText(sessionManager.GetAgentName());
        agent_name_2.setText(sessionManager.GetAgentName());
        agent_number.setText(sessionManager.Get);*/
        progressBar= getView().findViewById(R.id.progressBar);
      //  GetAgentData();

        if(sessionManager.isClientLoggedIn()){
            ly_agency_discription.setVisibility(View.GONE);
            ly_agency_name.setVisibility(View.GONE);
        }else if(sessionManager.isAgentLoggedin()){
            ly_agency_discription.setVisibility(View.VISIBLE);
            ly_agency_name.setVisibility(View.VISIBLE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setImageResource(R.drawable.ic_save);

                agent_image.setClickable(true);
                //
                agent_name_2.setEnabled(true);
                agent_name_2.setFocusable(true);
                agent_name_2.setClickable(true);
                //
                agent_number.setEnabled(true);
                agent_number.setFocusable(true);
                agent_number.setClickable(true);
                //
                agency_name.setEnabled(true);
                agency_name.setFocusable(true);
                agency_name.setClickable(true);
                //
                agency_description.setEnabled(true);
                agency_description.setFocusable(true);
                agency_description.setClickable(true);
                //
                camera.setVisibility(View.VISIBLE);
                if(sessionManager.isClientLoggedIn()){
                    agency_description.setVisibility(View.GONE);
                    agency_name.setVisibility(View.GONE);
                    camera.setVisibility(View.GONE);
                    agent_image.setClickable(false);
                }

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sessionManager.isClientLoggedIn()){
                           EditClientProfile();
                        }else if(sessionManager.isAgentLoggedin()){
                            EditDealerProfile();
                        }

                        edit.setImageResource(R.drawable.ic_edit);

                        agent_image.setClickable(false);
                        //
                        agent_name_2.setEnabled(false);
                        agent_name_2.setFocusable(false);
                        agent_name_2.setClickable(false);
                        //
                        agent_number.setEnabled(false);
                        agent_number.setFocusable(false);
                        agent_number.setClickable(false);
                        //
                        agency_name.setEnabled(false);
                        agency_name.setFocusable(false);
                        agency_name.setClickable(false);
                        //
                        agency_description.setEnabled(false);
                        agency_description.setFocusable(false);
                        agency_description.setClickable(false);
                        return;
                    }
                });


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        agent_image.setOnClickListener(new View.OnClickListener() {
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

        if(sessionManager.isClientLoggedIn()){
            user_type = "Client";
        }else if(sessionManager.isAgentLoggedin()){
            user_type = "Dealer";
        }

        GetAgentData();
    }

    private void GetAgentData() {
        progressBar.setVisibility(View.VISIBLE);
        WebServiceFactory.getInstance().GET_AGENT_PROFILE_DATA_WRAPPER_CALL(sessionManager.getAgent().get(KEY_AGENT_ID),user_type)
                .enqueue(new Callback<GetAgentProfileDataWrapper>() {
                    @Override
                    public void onResponse(Call<GetAgentProfileDataWrapper> call, final Response<GetAgentProfileDataWrapper> response) {

                            if (response.body() != null) {

                                    agent_name.setText(response.body().getResult().getName());
                                    agent_name_2.setText(response.body().getResult().getName());
                                    agent_number.setText(response.body().getResult().getNumber());


                                    if(user_type.equalsIgnoreCase("Dealer")) {
                                        agency_name.setText(response.body().getResult().getAgencyName());
                                        data = Base64.decode(response.body().getResult().getDiscription(), Base64.DEFAULT);
                                        try {
                                            decodedDescription = new String(data, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        agency_description.setText(decodedDescription);
                                        //
                                        Picasso.with(getContext())
                                                .load("https://propertyeager.com/"
                                                        + response.body().getResult().getAgencyLogo())
                                                .networkPolicy(NetworkPolicy.OFFLINE)
                                                .placeholder(R.drawable.profile_pic)
                                                .into(agent_image, new com.squareup.picasso.Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        //Try again online if cache failed
                                                        Picasso.with(getContext())
                                                                .load("https://propertyeager.com/"
                                                                        + response.body().getResult().getAgencyLogo())

                                                                .into(agent_image, new com.squareup.picasso.Callback() {
                                                                    @Override
                                                                    public void onSuccess() {
                                                                    }

                                                                    @Override
                                                                    public void onError() {
                                                                        Log.v("Picasso", "Could not fetch image");
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                    progressBar.setVisibility(View.GONE);

                            } else {
                                progressBar.setVisibility(View.GONE);
                            }



                    }

                    @Override
                    public void onFailure(Call<GetAgentProfileDataWrapper> call, Throwable t) {
                        Snackbar.make(getView(), "Something went wrong!", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void UploadAgentImage() {
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), image_file);
        image_part = MultipartBody.Part.createFormData("Logo", image_file.getName(), requestBody);
        progressBar.setVisibility(View.VISIBLE);
        WebServiceFactory.getInstance().agent_profile_edit_pic( RequestBody.create(MediaType.parse("*/*"),sessionManager.GetAgentId()),RequestBody.create(MediaType.parse("*/*"), user_type),image_part)
                .enqueue(new Callback<GetProfileEditWrapper>() {
                    @Override
                    public void onResponse(Call<GetProfileEditWrapper> call, Response<GetProfileEditWrapper> response) {
                        try {
                            if (response.body() != null) {
                                if (response.body().getResult().getFlag()) {
                                    Toast.makeText(getActivity(), "Your profile picture is successfully uploaded", Toast.LENGTH_SHORT).show();
                                    //if(user_type.equalsIgnoreCase("Dealer")){
                                    sessionManager.CreateAgentSession(response.body().getResult().getId(),
                                            response.body().getResult().getName(),
                                            response.body().getResult().getEmail(),
                                            response.body().getResult().getNumber(),
                                            response.body().getResult().getPassword(),
                                            response.body().getResult().getAgencyLogo(),
                                            response.body().getResult().getAgencyName(),
                                            response.body().getResult().getDiscription());

                                   // }
                                    /*else if(user_type.equalsIgnoreCase("Client")){
                                        sessionManager.CreateClientSession(
                                                response.body().getResult().getId(),
                                                response.body().getResult().getName(),
                                                response.body().getResult().getEmail(),
                                                response.body().getResult().getNumber(),
                                                response.body().getResult().getPassword()
                                        );
                                    }*/
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Unautherization", Toast.LENGTH_SHORT).show();
                                    //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetProfileEditWrapper> call, Throwable t) {
                        Snackbar.make(getView(), "Something went wrong!", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void EditClientProfile() {

        progressBar.setVisibility(View.VISIBLE);
        WebServiceFactory.getInstance().GET_CLIENT_EDIT_PROFILE_DATA_WRAPPER_CALL(
               sessionManager.GetAgentId(),
                user_type,
                agent_name_2.getText().toString(),
                agent_number.getText().toString(),
                sessionManager.GetRememberClientPassword()
                )
                .enqueue(new Callback<GetProfileEditWrapper>() {
                    @Override
                    public void onResponse(Call<GetProfileEditWrapper> call, Response<GetProfileEditWrapper> response) {
                        try {
                            if (response.body() != null) {
                                if (response.body().getResult().getFlag()) {
                                    Toast.makeText(getActivity(), response.body().getResult().getMsg(), Toast.LENGTH_SHORT).show();
                                    //if(user_type.equalsIgnoreCase("Dealer")){
                                    sessionManager.CreateClientSession(
                                            response.body().getResult().getId(),
                                            response.body().getResult().getName(),
                                            response.body().getResult().getEmail(),
                                            response.body().getResult().getNumber(),
                                            response.body().getResult().getPassword());

                                    // }
                                    /*else if(user_type.equalsIgnoreCase("Client")){
                                        sessionManager.CreateClientSession(
                                                response.body().getResult().getId(),
                                                response.body().getResult().getName(),
                                                response.body().getResult().getEmail(),
                                                response.body().getResult().getNumber(),
                                                response.body().getResult().getPassword()
                                        );
                                    }*/
                                    progressBar.setVisibility(View.GONE);
                                    if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
                                        getFragmentManager().popBackStack();
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Unautherization", Toast.LENGTH_SHORT).show();
                                    //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetProfileEditWrapper> call, Throwable t) {
                        Snackbar.make(getView(), "Something went wrong!", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void EditDealerProfile() {

        progressBar.setVisibility(View.VISIBLE);
        WebServiceFactory.getInstance().GET_AGENT_EDIT_PROFILE_DATA_WRAPPER_CALL(
                sessionManager.GetAgentId(),
                user_type,
                agent_name_2.getText().toString(),
                agent_number.getText().toString(),
                agency_name.getText().toString(),
                agency_description.getText().toString(),
                sessionManager.GetRememberAgentPassword())
                .enqueue(new Callback<GetProfileEditWrapper>() {
                    @Override
                    public void onResponse(Call<GetProfileEditWrapper> call, Response<GetProfileEditWrapper> response) {
                        try {
                            if (response.body() != null) {
                                if (response.body().getResult().getFlag()) {
                                    Toast.makeText(getActivity(), response.body().getResult().getMsg(), Toast.LENGTH_SHORT).show();
                                    //if(user_type.equalsIgnoreCase("Dealer")){
                                    sessionManager.CreateAgentSession(response.body().getResult().getId(),
                                            response.body().getResult().getName(),
                                            response.body().getResult().getEmail(),
                                            response.body().getResult().getNumber(),
                                            response.body().getResult().getPassword(),
                                            response.body().getResult().getAgencyLogo(),
                                            response.body().getResult().getAgencyName(),
                                            response.body().getResult().getDiscription());

                                    // }
                                    /*else if(user_type.equalsIgnoreCase("Client")){
                                        sessionManager.CreateClientSession(
                                                response.body().getResult().getId(),
                                                response.body().getResult().getName(),
                                                response.body().getResult().getEmail(),
                                                response.body().getResult().getNumber(),
                                                response.body().getResult().getPassword()
                                        );
                                    }*/
                                    progressBar.setVisibility(View.GONE);
                                    if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
                                        getFragmentManager().popBackStack();
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Unautherization", Toast.LENGTH_SHORT).show();
                                    //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetProfileEditWrapper> call, Throwable t) {
                        Snackbar.make(getView(), "Something went wrong!", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
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
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, cameraRequestCode);

                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 50 /*ignored for PNG*/, bos);
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
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String currentTime = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            image_file = convertBitmaptoFile(selectedImage, "image_" + currentTime  + ".jpg");
                            Log.e("IMAGE", "fa_image :" + image_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        agent_image.setImageBitmap(selectedImage);

                    }
                    UploadAgentImage();
                    break;
                case 1:
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
                                image_file = new File(picturePath);
                                Log.e("IMAGE", "ja_image :" + image_file);
                                agent_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    UploadAgentImage();
                    break;

            }
        }
    }

    private boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            // Permission is not granted
            return false;
        }
        return true;
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
}