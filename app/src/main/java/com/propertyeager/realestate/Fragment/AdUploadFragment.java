package com.propertyeager.realestate.Fragment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.material.snackbar.Snackbar;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Retrofit.WebServiceFactory;
import com.propertyeager.realestate.Utils.ConnectionDetector;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Views.ProgressLoader;
import com.propertyeager.realestate.Wrapper.GetAdUpload.GetAdUploadWrapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

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

public class AdUploadFragment extends Fragment {

    private SmartMaterialSpinner spinnerCities, spinnerPropertyOn, spinnerPropertyType;

    private EditText ed_title, ed_price, ed_address, ed_bed, ed_bath, ed_stories, ed_area, ed_description,
            ed_Amenities, ed_near_by, ed_youtube_embed;
    public String strCity = "", strPropertyOn = "", strPropertyType = "";

    private ImageView image1, image2, image3, image4, image5, image6, floorplan;

    private File image1_file, image2_file, image3_file, image4_file, image5_file, image6_file, floorplan_file;

    MultipartBody.Part image1_part, image2_part, image3_part, image4_part, image5_part, image6_part, floorplan_part;

    String user_type = "";
    String id = " ";
    private Button btn_sumit;
    ImageView back_btn;

    ConnectionDetector cd;
    private boolean isinternetpresent = false;

    SessionManager sessionManager;

    ProgressLoader progressBar;

    AwesomeValidation mAwesomeValidation;
    File sdImageMainDirectory;
    private static final int PERMISSION_REQUEST_CODE = 200;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    public static AdUploadFragment newInstance() {
        return new AdUploadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_upload_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cd = new ConnectionDetector(getContext());
        isinternetpresent = cd.isConnectingToInternet();

        sessionManager = new SessionManager(getContext());
        progressBar = getView().findViewById(R.id.progressBar);
        //
        spinnerCities = getView().findViewById(R.id.ed_cities);
        spinnerPropertyOn = getView().findViewById(R.id.ed_property_on);
        spinnerPropertyType = getView().findViewById(R.id.ed_property_type);
        //edittext
        ed_title = getView().findViewById(R.id.ed_title);
        ed_price = getView().findViewById(R.id.ed_price);
        ed_address = getView().findViewById(R.id.ed_address);
        ed_bed = getView().findViewById(R.id.ed_bed);
        ed_bath = getView().findViewById(R.id.ed_bath);
        ed_stories = getView().findViewById(R.id.ed_stories);
        ed_area = getView().findViewById(R.id.ed_area);
        ed_description = getView().findViewById(R.id.ed_description);
        ed_Amenities = getView().findViewById(R.id.ed_Amenities);
        ed_near_by = getView().findViewById(R.id.ed_near_by);
        ed_youtube_embed = getView().findViewById(R.id.ed_youtube_embed);
        btn_sumit = getView().findViewById(R.id.btn_sumit);
        back_btn = getView().findViewById(R.id.back_btn);
        //imageview
        image1 = getView().findViewById(R.id.image1);
        image2 = getView().findViewById(R.id.image2);
        image3 = getView().findViewById(R.id.image3);
        image4 = getView().findViewById(R.id.image4);
        image5 = getView().findViewById(R.id.image5);
        image6 = getView().findViewById(R.id.image6);
        floorplan = getView().findViewById(R.id.floorplan);

        mAwesomeValidation = new AwesomeValidation(COLORATION);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (sessionManager.isClientLoggedIn()) {
            user_type = "Client";
            id = sessionManager.getClient().get("id");
        } else if (sessionManager.isAgentLoggedin()) {
            user_type = "Dealer";
            id = sessionManager.getAgent().get("id");
        }
        try {
            super.onCreate(savedInstanceState);
            File root = new File(Environment
                    .getExternalStorageDirectory()
                    + File.separator + "Property" + File.separator);
            root.mkdirs();
            String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
            sdImageMainDirectory = new File(root, "IMAGE_" + millisecond);


        } catch (Exception e) {
            e.printStackTrace();
        }

        mAwesomeValidation.addValidation(getActivity(), R.id.ed_title, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_price, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_address, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_bed, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_bath, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_stories, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_area, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_description, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_Amenities, RegexTemplate.NOT_EMPTY, R.string.error_empty);
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_near_by, RegexTemplate.NOT_EMPTY, R.string.error_empty);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        ArrayAdapter<CharSequence> citiesAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.Cities,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        citiesAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerCities.setAdapter(citiesAdapter);

        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("item", (String) parent.getItemAtPosition(position));
                strCity = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> propertyOnAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.property_on,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        propertyOnAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerPropertyOn.setAdapter(propertyOnAdapter);

        spinnerPropertyOn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("item", (String) parent.getItemAtPosition(position));
                strPropertyOn = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> propertyTypeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.property_type,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        propertyTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerPropertyType.setAdapter(propertyTypeAdapter);

        spinnerPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("item", (String) parent.getItemAtPosition(position));
                strPropertyType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_sumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAwesomeValidation.validate()) {
                    if (image1.getDrawable() != null) {
                        if (strPropertyType.isEmpty()) {
                            Toast.makeText(getActivity(), "Please select property type", Toast.LENGTH_SHORT).show();
                        } else if (strCity.isEmpty()) {
                            Toast.makeText(getActivity(), "Please select property city", Toast.LENGTH_SHORT).show();
                        } else if (strPropertyOn.isEmpty()) {
                            Toast.makeText(getActivity(), "Please select property on", Toast.LENGTH_SHORT).show();
                        } else {
                            SumitPaymentForm();
                        }
                    }
                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
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

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    selectImage(getContext(), 2, 3);
                } else {
                    requestPermission();
                    selectImage(getContext(), 2, 3);
                }
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    selectImage(getContext(), 4, 5);
                } else {
                    requestPermission();
                    selectImage(getContext(), 4, 5);
                }
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    selectImage(getContext(), 6, 7);
                } else {
                    requestPermission();
                    selectImage(getContext(), 6, 7);
                }
            }
        });
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    selectImage(getContext(), 8, 9);
                } else {
                    requestPermission();
                    selectImage(getContext(), 8, 9);
                }
            }
        });
        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    selectImage(getContext(), 10, 11);
                } else {
                    requestPermission();
                    selectImage(getContext(), 10, 11);
                }
            }
        });
        floorplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    selectImage(getContext(), 12, 13);
                } else {
                    requestPermission();
                    selectImage(getContext(), 12, 13);
                }
            }
        });

    }

    private void SumitPaymentForm() {
        progressBar.setVisibility(View.VISIBLE);

        String image_name1 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        final RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), image1_file);
        image1_part = MultipartBody.Part.createFormData("img1", image1_file.getName(), requestBody1);
        final RequestBody first_applicant_signature_imageName = RequestBody.create(MediaType.parse("text/plain"), image_name1);
        //
        String image_name2 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        final RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), image2_file);
        image2_part = MultipartBody.Part.createFormData("img2", image2_file.getName(), requestBody2);
        final RequestBody first_applicant_ID_card_front_imageName = RequestBody.create(MediaType.parse("text/plain"), image_name2);
        //
        String image_name3 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        final RequestBody requestBody3 = RequestBody.create(MediaType.parse("image/*"), image3_file);
        image3_part = MultipartBody.Part.createFormData("img3", image3_file.getName(), requestBody3);
        final RequestBody first_applicant_ID_card_back_imageName = RequestBody.create(MediaType.parse("text/plain"), image_name3);
        //
        String image_name4 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        final RequestBody requestBody4 = RequestBody.create(MediaType.parse("image/*"), image4_file);
        image4_part = MultipartBody.Part.createFormData("img4", image4_file.getName(), requestBody4);
        final RequestBody joint_applicant_signature_imageName = RequestBody.create(MediaType.parse("text/plain"), image_name4);
        //
        String image_name5 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        final RequestBody requestBody5 = RequestBody.create(MediaType.parse("image/*"), image5_file);
        image5_part = MultipartBody.Part.createFormData("img5", image5_file.getName(), requestBody5);
        final RequestBody joint_applicant_ID_card_front_imageName = RequestBody.create(MediaType.parse("text/plain"), image_name5);
        //
        String image_name6 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        final RequestBody requestBody6 = RequestBody.create(MediaType.parse("image/*"), image6_file);
        image6_part = MultipartBody.Part.createFormData("img6", image6_file.getName(), requestBody6);
        final RequestBody joint_applicant_ID_card_back_imageName = RequestBody.create(MediaType.parse("text/plain"), image_name6);
        //
        String image_name7 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        final RequestBody requestBody7 = RequestBody.create(MediaType.parse("image/*"), floorplan_file);
        floorplan_part = MultipartBody.Part.createFormData("floor", floorplan_file.getName(), requestBody7);
        final RequestBody witness_1_ID_card_front_imageName = RequestBody.create(MediaType.parse("text/plain"), image_name7);


        WebServiceFactory.getInstance().GET_AD_UPLOAD_WRAPPER_CALL(

                RequestBody.create(MediaType.parse("*/*"), user_type),
                RequestBody.create(MediaType.parse("*/*"), id),
                RequestBody.create(MediaType.parse("*/*"), ed_title.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), strPropertyOn),
                RequestBody.create(MediaType.parse("*/*"), ed_price.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_address.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_stories.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_bed.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_bath.getText().toString().trim()),

                RequestBody.create(MediaType.parse("*/*"), ed_area.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), strCity),
                RequestBody.create(MediaType.parse("*/*"), ed_description.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), ed_youtube_embed.getText().toString().trim()),
                RequestBody.create(MediaType.parse("*/*"), strPropertyType),
                RequestBody.create(MediaType.parse("*/*"), ed_Amenities.getText().toString().trim()),
                /*ed_priceType.getText().toString(),*/
                RequestBody.create(MediaType.parse("*/*"), ed_near_by.getText().toString().trim()),

                /*ed_paymentdate.getText().toString(),*/
                /*ed_declaration_date.getText().toString(),*/


                //
                image1_part,
                // first_applicant_signature_imageName,
                //
                image2_part,
                //first_applicant_ID_card_front_imageName,
                //
                image3_part,
                //first_applicant_ID_card_back_imageName,
                //
                image4_part,
                //joint_applicant_signature_imageName,
                //
                image5_part,
                //joint_applicant_ID_card_front_imageName,
                //
                image6_part,
                //joint_applicant_ID_card_back_imageName,
                //
                floorplan_part
                // witness_1_ID_card_front_imageName,
                //


        ).enqueue(new Callback<GetAdUploadWrapper>() {
            @Override
            public void onResponse(Call<GetAdUploadWrapper> call, Response<GetAdUploadWrapper> response) {
                try {

                    if (response.body() != null) {
                        if (response.body().getResult().getFlag()) {

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), response.body().getResult().getMsg(), Toast.LENGTH_SHORT).show();


/*                                    Fragment paymentform = new HomeFragment();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                    transaction.replace(R.id.container, paymentform);
                                    transaction.addToBackStack(null);

                                    transaction.commit();*/

                        } else {
                            progressBar.setVisibility(View.GONE);
                            // Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar.make(getView().findViewById(android.R.id.content), response.body().getResult().getMsg(), Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Toast.makeText(getContext(),"Something went wrong!", Toast.LENGTH_SHORT).show();
                        Snackbar.make(getView().findViewById(android.R.id.content), response.body().getResult().getMsg(), Snackbar.LENGTH_LONG).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetAdUploadWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
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
            builder.setTitle("Choose your Ad image");

            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals("Take Photo")) {
                        Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);

                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(intent, cameraRequestCode);

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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
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
/*                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String image_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            first_applicant_signature_file = convertBitmaptoFile(selectedImage, "first_applicant_signature_file" + image_name + ".jpg");
                            Log.e("IMAGE", "fa_image :" + first_applicant_signature_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        first_applicant_signature.setImageBitmap(selectedImage);
                    }*/
                    try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        image1_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image1.setImageURI(Uri.parse(image1_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
                                image1_file = new File(picturePath);
                                Log.e("IMAGE", "image1 :" + image1_file);
                                image1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
                case 2:
                   /* if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String image_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            first_applicant_ID_card_front_file = convertBitmaptoFile(selectedImage, "first_applicant_ID_card_front" + image_name + ".jpg");
                            Log.e("IMAGE", "ja_image :" + first_applicant_ID_card_front_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        first_applicant_ID_card_front.setImageBitmap(selectedImage);
                    }*/
                    try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        image2_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image2.setImageURI(Uri.parse(image2_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
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
                                image2_file = new File(picturePath);
                                Log.e("IMAGE", "image2 :" + image2_file);
                                image2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;

                case 4:
/*                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String image_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            first_applicant_ID_card_back_file = convertBitmaptoFile(selectedImage, "first_applicant_image_" + image_name + ".jpg");
                            Log.e("IMAGE", "fa_image :" + first_applicant_ID_card_back_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        first_applicant_ID_card_back.setImageBitmap(selectedImage);
                    }*/
                    try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        image3_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image3.setImageURI(Uri.parse(image3_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case 5:
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
                                image3_file = new File(picturePath);
                                Log.e("IMAGE", "ja_image :" + image3_file);
                                image3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
                case 6:
/*                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String image_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            joint_applicant_signature_file = convertBitmaptoFile(selectedImage, "joint_applicant_signature" + image_name + ".jpg");
                            Log.e("IMAGE", "ja_image :" + first_applicant_ID_card_front_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        joint_applicant_signature.setImageBitmap(selectedImage);
                    }*/
                    try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        image4_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image4.setImageURI(Uri.parse(image4_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
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
                                image4_file = new File(picturePath);
                                Log.e("IMAGE", "ja_image :" + image4_file);
                                image4.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;

                case 8:
/*                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String image_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            joint_applicant_ID_card_front_file = convertBitmaptoFile(selectedImage, "joint_applicant_ID_card_front_file" + image_name + ".jpg");
                            Log.e("IMAGE", "fa_image :" + joint_applicant_ID_card_front_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        joint_applicant_ID_card_front.setImageBitmap(selectedImage);
                    }*/
                    try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        image5_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image5.setImageURI(Uri.parse(image5_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
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
                                image5_file = new File(picturePath);
                                Log.e("IMAGE", "ja_image :" + image5_file);
                                image5.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
                case 10:
/*                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String image_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            joint_applicant_ID_card_back_file = convertBitmaptoFile(selectedImage, "joint_applicant_ID_card_back_file" + image_name + ".jpg");
                            Log.e("IMAGE", "ja_image :" + joint_applicant_ID_card_back_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        joint_applicant_ID_card_back.setImageBitmap(selectedImage);
                    }*/
                    try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        image6_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image6.setImageURI(Uri.parse(image6_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 11:
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
                                image6_file = new File(picturePath);
                                Log.e("IMAGE", "ja_image :" + image6_file);
                                image6.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;

                case 12:
/*                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        try {
                            String image_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            joint_applicant_ID_card_back_file = convertBitmaptoFile(selectedImage, "joint_applicant_ID_card_back_file" + image_name + ".jpg");
                            Log.e("IMAGE", "ja_image :" + joint_applicant_ID_card_back_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        joint_applicant_ID_card_back.setImageBitmap(selectedImage);
                    }*/
                    try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        floorplan_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        floorplan.setImageURI(Uri.parse(floorplan_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
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
                                floorplan_file = new File(picturePath);
                                Log.e("IMAGE", "ja_image :" + floorplan_file);
                                floorplan.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
}