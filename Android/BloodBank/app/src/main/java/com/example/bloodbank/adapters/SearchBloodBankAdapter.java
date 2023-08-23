package com.example.bloodbank.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.bloodbank.dataholder.BloodBankDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SearchBloodBankAdapter extends RecyclerView.Adapter<SearchBloodBankAdapter.SearchBloodBankViewHolder> {
    Context context;
    ArrayList<BloodBankDataHolder> items;
    boolean admin;
    ProgressDialog progressDialog;

    public SearchBloodBankAdapter(Context context, ArrayList<BloodBankDataHolder> items) {
        this.context = context;
        this.items = items;
        admin = false;
    }

    public SearchBloodBankAdapter(Context context, ArrayList<BloodBankDataHolder> items, boolean admin) {
        this.context = context;
        this.items = items;
        this.admin = admin;
    }

    @NonNull
    @Override
    public SearchBloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.blood_bank_listview_item, viewGroup, false);



        return new SearchBloodBankViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull SearchBloodBankViewHolder searchBloodBankViewHolder, final int i) {
        final BloodBankDataHolder item = items.get(i);
        searchBloodBankViewHolder.txt_blood_bank_name.setText(item.getName());
        searchBloodBankViewHolder.txt_blood_bank_email.setText(item.getEmail());
        searchBloodBankViewHolder.txt_blood_bank_contact_no.setText(item.getContact());
        searchBloodBankViewHolder.txt_blood_bank_address.setText(item.getAddress());

        searchBloodBankViewHolder.btn_delete_blood_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are You Sure!")
                        .setContentText("Are you sure to delete blood bank!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                deleteCity(item.getId(),i);
                                sDialog.dismissWithAnimation();
                            }
                        }).setCancelButton("Cancel!", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
            }
        });

        searchBloodBankViewHolder.btn_update_blood_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminAddBloodBankActivity.class);
                intent.putExtra("id",item.getId());
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("con",item.getContact());
                intent.putExtra("address",item.getAddress());
                intent.putExtra("city",item.getCity_id());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class SearchBloodBankViewHolder extends RecyclerView.ViewHolder {

        TextView txt_blood_bank_email, txt_blood_bank_contact_no, txt_blood_bank_address, txt_blood_bank_name;
        ImageButton btn_update_blood_bank, btn_delete_blood_bank;

        public SearchBloodBankViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_blood_bank_name = (TextView) itemView.findViewById(R.id.txt_blood_bank_name);
            txt_blood_bank_email = (TextView) itemView.findViewById(R.id.txt_blood_bank_email);
            txt_blood_bank_contact_no = (TextView) itemView.findViewById(R.id.txt_blood_bank_contact_no);
            txt_blood_bank_address = (TextView) itemView.findViewById(R.id.txt_blood_bank_address);
            btn_update_blood_bank = itemView.findViewById(R.id.btn_update_blood_bank);
            btn_delete_blood_bank = itemView.findViewById(R.id.btn_delete_blood_bank);
            if (admin) {
                btn_update_blood_bank.setVisibility(View.VISIBLE);
                btn_delete_blood_bank.setVisibility(View.VISIBLE);
            }


        }
    }



    public void deleteCity(final int id, final int p) {

        String strings = context.getResources().getString(R.string.url) + "deletebloodbank.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {


                        boolean insert = jsonObject.getBoolean("deletebloodbank");
                            if (insert) {
                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Blood Bank Deleted Successfully!")
                                        .setConfirmText("ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                items.remove(p);
                                                notifyItemRemoved(p);
                                                notifyItemRangeChanged(p, items.size());
                                                notifyDataSetChanged();
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                            } else {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Something went wrong!")
                                        .show();
                            }



                        /***********END GET CITY NAME FROM SERVER**********/
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
                Toast.makeText(context, "Connection Problem", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                    params.put("blood_bank_id", String.valueOf(id));



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

}
