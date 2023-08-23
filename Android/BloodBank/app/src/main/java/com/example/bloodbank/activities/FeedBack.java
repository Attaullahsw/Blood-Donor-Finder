package com.example.bloodbank.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedBack extends AppCompatActivity {

    EditText edt_name,edt_message,edt_email;
    Button btn_send;
    ProgressDialog progressDialog;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Feed Back");
        actionBar.setDisplayHomeAsUpEnabled(true);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_message = (EditText) findViewById(R.id.edt_msg);
        btn_send = (Button) findViewById(R.id.btn_send_feed_back);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = edt_name.getText().toString();
                final String email = edt_email.getText().toString();
                final String msg = edt_message.getText().toString();

                if(name.equals("") || msg.equals("")){
                    TextView txt = (TextView) findViewById(R.id.txt_error_msg);
                    txt.setText("Please Fill All The Field");
                }else{




                    String strings = getResources().getString(R.string.url)+"uploadFeedBack.php";
                    StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean error = jsonObject.getBoolean("error");
                                if (error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(FeedBack.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(FeedBack.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                                    edt_name.setText("");
                                    edt_message.setText("");
                                }


                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            showCustomDialog();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("name",name);
                            params.put("email",email);
                            params.put("msg",msg);

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(FeedBack.this);
                    requestQueue.add(request);
                    progressDialog = new ProgressDialog(FeedBack.this);
                    progressDialog.setTitle("Uploading Data");
                    progressDialog.setMessage("Please wait a while...");
                    progressDialog.show();





                }
            }
        });

    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
