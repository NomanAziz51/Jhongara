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
import android.util.Base64;
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
import com.propertyeager.realestate.Wrapper.GetADEditImages.GetADEditImagesWrapper;
import com.propertyeager.realestate.Wrapper.GetADEditText.GetADEditTextWrapper;
import com.propertyeager.realestate.Wrapper.GetAdDetails.GetAdDetailWrapper;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class AdEditFragment extends Fragment {


    private SmartMaterialSpinner spinnerCities, spinnerPropertyOn, spinnerPropertyType;

    private EditText ed_title, ed_price, ed_address, ed_bed, ed_bath, ed_stories, ed_area, ed_description,
            ed_Amenities, ed_near_by, ed_youtube_embed;
    public String strCity = "", strPropertyOn = "", strPropertyType = "", image_position = "";

    private ImageView image1, image2, image3, image4, image5, image6, floorplan;

    private File image_file;

    MultipartBody.Part image_part;

    String user_type = "";
    String id = "", ad_id = "";
    private Button btn_sumit;

    ConnectionDetector cd;
    private boolean isinternetpresent = false;

    SessionManager sessionManager;

    ProgressLoader progressBar;

    AwesomeValidation mAwesomeValidation = new AwesomeValidation(COLORATION);
    File sdImageMainDirectory;
    private static final int PERMISSION_REQUEST_CODE = 200;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    private String decodedDescription;

    public static AdEditFragment newInstance() {
        return new AdEditFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_edit_fragment, container, false);
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
        //imageview
        image1 = getView().findViewById(R.id.image1);
        image2 = getView().findViewById(R.id.image2);
        image3 = getView().findViewById(R.id.image3);
        image4 = getView().findViewById(R.id.image4);
        image5 = getView().findViewById(R.id.image5);
        image6 = getView().findViewById(R.id.image6);
        floorplan = getView().findViewById(R.id.floorplan);

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

        ad_id = getArguments().getString("id");
        GetAdDetail(ad_id);


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
       // mAwesomeValidation.addValidation(getActivity(), R.id.ed_youtube_embed, RegexTemplate.NOT_EMPTY, R.string.error_empty);

        //  if (cbAppartment.isChecked() || cbFoodCourt.isChecked() || cbOfiice.isChecked() || cbShop.isChecked() || cbSuit.isChecked()) {
      /*  mAwesomeValidation.addValidation(getActivity(), R.id.ed_property_on, new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equalsIgnoreCase("Select City")) {
                    return false;
                } else {
                    return true;
                }
            }
        }, new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(validationHolder.getErrMsg());
                textViewError.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(null);
                textViewError.setTextColor(Color.BLACK);
            }
        }, R.string.err_tech_stacks);
        //   }

        //  if (cbAppartment.isChecked() || cbFoodCourt.isChecked() || cbOfiice.isChecked() || cbShop.isChecked() || cbSuit.isChecked()) {
        mAwesomeValidation.addValidation(getActivity(), R.id.ed_city, validationHolder -> {
            if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equalsIgnoreCase("Property On")) {
                return false;
            } else {
                return true;
            }
        }, validationHolder -> {
            TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
            textViewError.setError(validationHolder.getErrMsg());
            textViewError.setTextColor(Color.RED);
        }, validationHolder -> {
            TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
            textViewError.setError(null);
            textViewError.setTextColor(Color.BLACK);
        }, R.string.err_tech_stacks);

        mAwesomeValidation.addValidation(getActivity(), R.id.ed_property_type, validationHolder -> {
            if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equalsIgnoreCase("Property Type")) {
                return false;
            } else {
                return true;
            }
        }, validationHolder -> {
            TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
            textViewError.setError(validationHolder.getErrMsg());
            textViewError.setTextColor(Color.RED);
        }, validationHolder -> {
            TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
            textViewError.setError(null);
            textViewError.setTextColor(Color.BLACK);
        }, R.string.err_tech_stacks);*/

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
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Are you sure you want to edit this ad ?\nNote: It will edit text only for image select it individually.");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SumitPaymentForm();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();


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
                image_position = "1";
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
                image_position = "2";
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
                image_position = "3";
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
                image_position = "4";
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
                image_position = "5";
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
                image_position = "6";
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
                image_position = "7";
            }
        });

    }

    private void SumitPaymentForm() {
        progressBar.setVisibility(View.VISIBLE);


        WebServiceFactory.getInstance().GET_AD_EDIT_TEXT_WRAPPER_CALL(

                ad_id, ed_title.getText().toString().trim(), ed_price.getText().toString().trim(), ed_address.getText().toString().trim(),
                ed_stories.getText().toString().trim(), ed_bed.getText().toString().trim(), ed_bath.getText().toString().trim(), ed_area.getText().toString().trim(),
                strCity, strPropertyType, strPropertyOn, ed_description.getText().toString().trim(), ed_youtube_embed.getText().toString().trim(),
                ed_Amenities.getText().toString().trim(), ed_near_by.getText().toString().trim()

        ).enqueue(new Callback<GetADEditTextWrapper>() {
            @Override
            public void onResponse(Call<GetADEditTextWrapper> call, Response<GetADEditTextWrapper> response) {
                try {

                    if (response.body() != null) {
                        if (response.body().getStatus() == 1 ) {

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();


/*                                    Fragment paymentform = new HomeFragment();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                    transaction.replace(R.id.container, paymentform);
                                    transaction.addToBackStack(null);

                                    transaction.commit();*/

                        } else {
                            progressBar.setVisibility(View.GONE);
                             Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            //Snackbar.make(getView().findViewById(android.R.id.content), response.body().getResult().getMsg(), Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                         Toast.makeText(getActivity(),"Something went wrong!", Toast.LENGTH_SHORT).show();
                        //Snackbar.make(getView().findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetADEditTextWrapper> call, Throwable t) {
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
                        image_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image1.setImageURI(Uri.parse(image_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadAdImage();
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
                                Log.e("IMAGE", "image1 :" + image_file);
                                image1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                        UploadAdImage();
                    }
                    break;
                case 2:

                    try {
                        String millisecond = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        image_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image2.setImageURI(Uri.parse(image_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadAdImage();
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
                                image_file = new File(picturePath);
                                Log.e("IMAGE", "image1 :" + image_file);
                                image2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                        UploadAdImage();
                    }
                    break;
                case 4:
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
                        image_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image3.setImageURI(Uri.parse(image_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadAdImage();
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
                                image_file = new File(picturePath);
                                Log.e("IMAGE", "image1 :" + image_file);
                                image3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                        UploadAdImage();
                    }
                    break;
                case 6:
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
                        image_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image4.setImageURI(Uri.parse(image_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadAdImage();
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
                                image_file = new File(picturePath);
                                Log.e("IMAGE", "image1 :" + image_file);
                                image4.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                        UploadAdImage();
                    }
                    break;
                case 8:
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
                        image_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image5.setImageURI(Uri.parse(image_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadAdImage();
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
                                image_file = new File(picturePath);
                                Log.e("IMAGE", "image1 :" + image_file);
                                image5.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                        UploadAdImage();
                    }
                    break;
                case 10:
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
                        image_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        image6.setImageURI(Uri.parse(image_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadAdImage();
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
                                image_file = new File(picturePath);
                                Log.e("IMAGE", "image1 :" + image_file);
                                image6.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                        UploadAdImage();
                    }
                    break;
                case 12:
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
                        image_file = new File(String.valueOf(convertBitmaptoFile(fileToBitmap(sdImageMainDirectory.getPath()), "IMAGE_" + millisecond + ".jpg")));
                        floorplan.setImageURI(Uri.parse(image_file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadAdImage();
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
                                image_file = new File(picturePath);
                                Log.e("IMAGE", "image1 :" + image_file);
                                floorplan.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                        UploadAdImage();
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

    private void GetAdDetail(String id) {
        progressBar.setVisibility(View.VISIBLE);

        // Log.e("TAG_onCheck", sessionManager.GetAccessToken());
        WebServiceFactory.getInstance().GET_AD_DETAIL_WRAPPER_CALL(id)
                .enqueue(new Callback<GetAdDetailWrapper>() {
                    @Override
                    public void onResponse(Call<GetAdDetailWrapper> call, final Response<GetAdDetailWrapper> response) {
                        if (response.body() != null) {
                            // JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 1) {

                                Picasso.with(getContext())
                                        .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getPic1())
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .fit().centerInside().into(image1);

                                Picasso.with(getContext())
                                        .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getPic2())
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .fit().centerInside().into(image2);

                                Picasso.with(getContext())
                                        .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getPic3())
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .fit().centerInside().into(image3);

                                Picasso.with(getContext())
                                        .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getPic4())
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .fit().centerInside().into(image4);

                                Picasso.with(getContext())
                                        .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getPic5())
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .fit().centerInside().into(image5);

                                Picasso.with(getContext())
                                        .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getPic6())
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .fit().centerInside().into(image6);

                                Picasso.with(getContext())
                                        .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getFloorPlan())
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .fit().centerInside().into(floorplan);

/*
                                videoId = response.body().getResponse().getResult().getVideoId().trim();
                                //YPlayer.loadVideo("1qcWXa2J1T4&list=RDMM1qcWXa2J1T4&start_radio=1");
                                if (!videoId.isEmpty()) {
                                    YPlayer.cueVideo(videoId);
                                    YPlayer.setFullscreen(false);
                                } else {
                                    frameLayout.setVisibility(View.GONE);
                                }*/
                                ed_price.setText(response.body().getResponse().getResult().getPrice());
                                ed_title.setText(response.body().getResponse().getResult().getTitle());
                                ed_address.setText(response.body().getResponse().getResult().getAddress());

                                ed_bed.setText(response.body().getResponse().getResult().getRooms());
                                ed_bath.setText(response.body().getResponse().getResult().getBaths());
                                ed_stories.setText(response.body().getResponse().getResult().getStories());
                                ed_area.setText(response.body().getResponse().getResult().getArea());
                                ed_near_by.setText(response.body().getResponse().getResult().getNearby());
                                ed_youtube_embed.setText(response.body().getResponse().getResult().getVideoLink());
                                //spinnerCities.setSelection(response.body().getResponse().getResult().getCity().toString().get);
                                byte[] data = Base64.decode(response.body().getResponse().getResult().getDiscription(), Base64.DEFAULT);
                                try {
                                    decodedDescription = new String(data, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                ed_description.setText(decodedDescription);
                                ed_Amenities.setText(response.body().getResponse().getResult().getAnimites());
                                progressBar.setVisibility(View.GONE);

                            } else {
                                progressBar.setVisibility(View.GONE);
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<GetAdDetailWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    private void UploadAdImage() {
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), image_file);
        image_part = MultipartBody.Part.createFormData("img", image_file.getName(), requestBody);
        progressBar.setVisibility(View.VISIBLE);
        WebServiceFactory.getInstance().AD_EDIT_IMAGES_WRAPPER_CALL(RequestBody.create(MediaType.parse("*/*"), ad_id), RequestBody.create(MediaType.parse("*/*"), image_position), image_part)
                .enqueue(new Callback<GetADEditImagesWrapper>() {
                    @Override
                    public void onResponse(Call<GetADEditImagesWrapper> call, Response<GetADEditImagesWrapper> response) {
                   //     try {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 1) {
                                    Toast.makeText(getContext(), "Your profile picture is successfully uploaded", Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }


                  /*     } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                    }

                    @Override
                    public void onFailure(Call<GetADEditImagesWrapper> call, Throwable t) {
                        Snackbar.make(getView(), "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}