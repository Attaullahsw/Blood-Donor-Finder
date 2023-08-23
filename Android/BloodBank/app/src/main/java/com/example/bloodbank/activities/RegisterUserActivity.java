package com.example.bloodbank.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodbank.R;
import com.example.bloodbank.activities.adminactivity.AdminAddBloodBankActivity;
import com.example.bloodbank.util.InternetWarningDialog;
import com.example.bloodbank.util.Permissions;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterUserActivity extends AppCompatActivity {


    /************** Reference Variable to View******************/
    EditText edt_name, edt_email, edt_password, edt_contact_no, edt_diseas,edt_confirm_password;
    AutoCompleteTextView edt_city;
    MaterialSpinner spn_blood_group;
    Button btn_register, edt_date_of_birth;
    ImageView edt_image;
    TextView edt_image_name;
    RadioGroup radio_gender;
    RadioButton radio_male_female;

    BottomNavigationView navigation;

    CheckBox chb_diseas;
    /************** End Reference Variable to View******************/


    Calendar myCalendar;

    String city_name[];
    int city_id[];

    String blood_group[];
    int blood_id[];

    int user_profile_id, diseas_status;
    String user_name, email, user_pass, date_of_birth, contact, user_city = "", gender, image = "", diseas, blood_user_group = "";

    ProgressDialog progressDialog;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    Intent intent = new Intent(RegisterUserActivity.this, HomeActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_registeration:


                    return true;
                case R.id.navigation_add_blood_bank:
                    Intent intent2 = new Intent(RegisterUserActivity.this, AddBloodBankActivity.class);
                    finish();
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        /************* View Initiallization ************************/
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_confirm_password = (EditText) findViewById(R.id.edt_confirm_password);
        edt_date_of_birth = (Button) findViewById(R.id.edt_date_of_birth);
        edt_contact_no = (EditText) findViewById(R.id.edt_contact_no);
        edt_diseas = (EditText) findViewById(R.id.edt_diseas);
        edt_image = (ImageView) findViewById(R.id.edt_image);
        edt_image_name = (TextView) findViewById(R.id.edt_image_name);

        edt_city = (AutoCompleteTextView) findViewById(R.id.edt_city);

        spn_blood_group = (MaterialSpinner) findViewById(R.id.spn_blood_group);

        radio_gender = (RadioGroup) findViewById(R.id.radio_gender);

        chb_diseas = (CheckBox) findViewById(R.id.chb_diseas);

        btn_register = (Button) findViewById(R.id.btn_register);

        /*************End View Initiallization************************/


        user_profile_id = getIntent().getIntExtra("user_profile_id", 0);
        if (user_profile_id != 0) {
            diseas_status = getIntent().getIntExtra("diseas_status", 0);

            user_name = getIntent().getStringExtra("user_name");
            email = getIntent().getStringExtra("email");
            user_pass = getIntent().getStringExtra("user_pass");
            date_of_birth = getIntent().getStringExtra("date_of_birth");
            contact = getIntent().getStringExtra("contact");
            user_city = getIntent().getStringExtra("city");
            gender = getIntent().getStringExtra("gender");
            image = getIntent().getStringExtra("image");
            diseas = getIntent().getStringExtra("diseas");
            blood_user_group = getIntent().getStringExtra("blood_group");

            btn_register.setText("Update Profile");

            edt_name.setText(user_name);
            edt_email.setText(email);
            edt_password.setText(user_pass);
            edt_confirm_password.setText(user_pass);
            edt_date_of_birth.setText(date_of_birth);
            edt_contact_no.setText(contact);
            //  edt_image_name.setText(image);

            Picasso.get().load(getResources().getString(R.string.url) + "images/" + image.toString())
                    .placeholder(R.drawable.breakimage)
                    .error(R.drawable.breakimage)
                    .into(edt_image);

            if (gender.equals("Male")) {
                radio_male_female = findViewById(R.id.radio_male);
                radio_male_female.setChecked(true);
            } else {
                radio_male_female = findViewById(R.id.radio_female);
                radio_male_female.setChecked(true);
            }
            if (diseas_status == 1) {
                chb_diseas.setChecked(true);
            }

            edt_diseas.setText(diseas);


        }


        getData();

        /****************Bottom Navigation******************/
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_registeration);
        /****************End Bottom Navigation******************/

        myCalendar = Calendar.getInstance();


        chb_diseas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    edt_diseas.setVisibility(View.VISIBLE);
                } else {
                    edt_diseas.setVisibility(View.GONE);
                }
            }
        });


        edt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Permissions.isStoragePermissionGranted(RegisterUserActivity.this)) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                } else {
                    Toast.makeText(RegisterUserActivity.this, "First Grant The Storage Permission!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        edt_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterUserActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = edt_name.getText().toString();
                final String email = edt_email.getText().toString();
                final String password = edt_password.getText().toString();
                final String confirm_password = edt_confirm_password.getText().toString();
                final String date_birth = edt_date_of_birth.getText().toString();
                final String contact_no = edt_contact_no.getText().toString();


                int c_id = 0;
                String selectedCity = edt_city.getText().toString();
                for (int i = 0; i < city_name.length; i++) {
                    if (selectedCity.equals(city_name[i])) {
                        c_id = city_id[i];
                    }
                }


                final String image = edt_image_name.getText().toString();

                final int blood_groupspn = spn_blood_group.getSelectedIndex();

                int selectedId = radio_gender.getCheckedRadioButtonId();
                radio_male_female = (RadioButton) findViewById(selectedId);


                if (name.equals("")) {
                    edt_image.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "Name Field Is Required", Toast.LENGTH_SHORT).show();

                } else if (email.equals("")) {
                    edt_email.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "Email Field Is Required", Toast.LENGTH_SHORT).show();

                } else if (password.equals("")) {
                    edt_password.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "Password Field Is Required", Toast.LENGTH_SHORT).show();

                } else if (blood_groupspn == 0) {
                    spn_blood_group.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "Blood Group Field Is Required", Toast.LENGTH_SHORT).show();

                } else if (date_birth.equals("")) {
                    edt_date_of_birth.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "Date Of Birth Field Is Required", Toast.LENGTH_SHORT).show();

                } else if (contact_no.equals("")) {
                    edt_contact_no.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "Contact Field Is Required", Toast.LENGTH_SHORT).show();
                } else if (c_id == 0) {
                    edt_city.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "City Field Is Required", Toast.LENGTH_SHORT).show();
                } else if (radio_male_female == null) {
                    radio_gender.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "Gender Field Is Required", Toast.LENGTH_SHORT).show();
                } else if(!password.equals(confirm_password)){
                    edt_confirm_password.requestFocus();
                    Toast.makeText(RegisterUserActivity.this, "Confirm password does not matched!", Toast.LENGTH_SHORT).show();
                }else {

                    if (user_profile_id != 0) {
                        confirmPassword(name, email, password, date_birth, contact_no, blood_groupspn, c_id);
                    } else {
                        insertUser(name, email, password, date_birth, contact_no, blood_groupspn, c_id, "");
                    }


                }


            }
        });


    }

    public void initiallizeView() {

    }

    /**
     * set Date to Edit Text
     */
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edt_date_of_birth.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Get Image From Gallery
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                String file_name = filePath.substring(filePath.lastIndexOf("/") + 1);

                Bitmap bm = BitmapFactory.decodeFile(filePath);
                edt_image.setImageBitmap(bm);
                edt_image_name.setText(filePath);

//                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//
//                } else {
//                    Toast.makeText(RegisterUserActivity.this,"Select The image Only!",Toast.LENGTH_LONG).show();
//                }
            }
    }

    /**
     * Get The Path Of Image
     *
     * @param uri Uri Of Image
     * @return Image Path
     */
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    /**
     * Convert Image to Base64 String
     *
     * @param imagePath image path which is selected
     * @return String Base64
     */
    private String convertToBase64(String imagePath) {

        Bitmap bm = BitmapFactory.decodeFile(imagePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] byteArrayImage = baos.toByteArray();

        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        return encodedImage;

    }


    /**
     * Request to the server for data
     */
    public void getData() {

        String strings = getResources().getString(R.string.url) + "fetchBloodCity.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUserActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {

                        int bloodindex = -1;
                        int cityindex = -1;

                        /***********GET BLOOD GROUP FROM SERVER**********/
                        JSONArray bloodGroupJsonArray = jsonObject.getJSONArray("blood_group");
                        blood_group = new String[bloodGroupJsonArray.length() + 1];
                        blood_group[0] = "Select Blood Group...";
                        blood_id = new int[bloodGroupJsonArray.length() + 1];
                        blood_id[0] = 0;

                        for (int i = 0; i < bloodGroupJsonArray.length(); i++) {
                            JSONObject blood = bloodGroupJsonArray.getJSONObject(i);
                            blood_id[i + 1] = blood.getInt("blood_group_id");
                            blood_group[i + 1] = blood.getString("blood_group");
                            if (blood_user_group.equals(blood.getString("blood_group"))) {
                                bloodindex = i+1;
                            }
                        }
                        /***********END GET BLOOD GROUP FROM SERVER**********/


                        /***********GET CITY NAME FROM SERVER**********/
                        JSONArray cityJsonArray = jsonObject.getJSONArray("city");
                        city_id = new int[cityJsonArray.length()];
                        city_name = new String[cityJsonArray.length()];

                        for (int i = 0; i < cityJsonArray.length(); i++) {
                            JSONObject city = cityJsonArray.getJSONObject(i);
                            city_id[i] = city.getInt("city_id");
                            city_name[i] = city.getString("city_name");

                            if (user_city.equals(city.getString("city_name"))) {
                                cityindex = i;
                            }
                        }
                        /***********END GET CITY NAME FROM SERVER**********/


                        spn_blood_group.setItems(blood_group);
                        ArrayAdapter adapter = new ArrayAdapter(RegisterUserActivity.this, android.R.layout.simple_list_item_1, city_name);
                        edt_city.setAdapter(adapter);
                        edt_city.setThreshold(1);

                        if (bloodindex > -1) {
                            spn_blood_group.setText(blood_group[bloodindex]);
                            spn_blood_group.setSelectedIndex(bloodindex);

                        }

                        if (cityindex > -1) {
                            edt_city.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    edt_city.showDropDown();
                                }
                            }, 500);
                            edt_city.setText(city_name[cityindex]);
                            edt_city.setSelection(cityindex);
                        }
                        //     edt_city.setSelection(cityindex);


                    }

                    progressDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                //Toast.makeText(RegisterUserActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(RegisterUserActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterUserActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(RegisterUserActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    void confirmPassword(final String name, final String email, final String password, final String date_birth, final String contact_no, final int blood_groupspn, final int finalC_id) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterUserActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_update_profile_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_confirm_password);
        Button btn = (Button) dialogView.findViewById(R.id.btn_confirm_update);
        TextView txtCancel = (TextView) dialogView.findViewById(R.id.btn_confirm_password_cancel);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = editText.getText().toString();
                if (pass.equals("")) {
                    Toast.makeText(RegisterUserActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else {
                    insertUser(name, email, password, date_birth, contact_no, blood_groupspn, finalC_id, pass);
                }
            }
        });
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


    void insertUser(final String name, final String email, final String password, final String date_birth, final String contact_no, final int blood_groupspn, final int finalC_id,
                    final String confirmPass) {
        String strings = getResources().getString(R.string.url) + "adduser.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUserActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {


                        if (user_profile_id != 0) {
                            if (jsonObject.getBoolean("confirm_password_error")) {
                                new SweetAlertDialog(RegisterUserActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Old Password in incorrect!")
                                        .show();
                            } else {
                                if (jsonObject.getBoolean("checkerror")) {
                                    new SweetAlertDialog(RegisterUserActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("User With This Email already Exits!")
                                            .show();
                                } else {
                                    if (jsonObject.getBoolean("insert")) {
                                        new SweetAlertDialog(RegisterUserActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Your Account Is Updated Successfully!")
                                                .setConfirmText("ok")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();

                                                        finish();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new SweetAlertDialog(RegisterUserActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText("Something went wrong!")
                                                .show();
                                    }

                                }
                            }

                        } else {

                            if (jsonObject.getBoolean("checkerror")) {
                                new SweetAlertDialog(RegisterUserActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("User With This Email already Exits!")
                                        .show();
                            } else {
                                if (jsonObject.getBoolean("insert")) {
                                    new SweetAlertDialog(RegisterUserActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Your Account Is Created Successfully!")
                                            .setConfirmText("ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    finish();

                                                }
                                            })
                                            .show();
                                } else {
                                    new SweetAlertDialog(RegisterUserActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Something went wrong!")
                                            .show();
                                }

                            }
                        }


                        progressDialog.dismiss();

                    }

                    progressDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                //Toast.makeText(RegisterUserActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(RegisterUserActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("date_birth", date_birth);
                params.put("contact_no", contact_no);
                params.put("blood_group", String.valueOf(blood_id[blood_groupspn]));
                params.put("city", String.valueOf(finalC_id));
                params.put("Gender", radio_male_female.getText().toString());
                if (chb_diseas.isChecked()) {
                    params.put("diseas_status", "1");
                    params.put("diseas", edt_diseas.getText().toString());
                } else {
                    params.put("diseas_status", "0");
                    params.put("diseas", "No Diseas");
                }
                String imagename = edt_image_name.getText().toString();
                if (imagename.equals("")) {
                    params.put("imagename", image);
                    params.put("image", "");
                } else {
                    params.put("imagename", imagename.substring(imagename.lastIndexOf("/") + 1));
                    params.put("image", convertToBase64(imagename));
                }

                if (!confirmPass.equals("")) {
                    params.put("confirm_pass", confirmPass);
                    params.put("user_id", String.valueOf(user_profile_id));
                }


                params.put("addnewuser", "add");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterUserActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(RegisterUserActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_registeration);
    }


}
