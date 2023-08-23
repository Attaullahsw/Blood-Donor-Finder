package com.example.bloodbank.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.bloodbank.dataholder.CityDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListViewHolder> {

    Context context;
    ArrayList<CityDataHolder> items;
    ProgressDialog progressDialog;
    EditText editText;

    AlertDialog alertDialog;

    public CityListAdapter(Context context, ArrayList<CityDataHolder> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CityListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.all_city_item, viewGroup, false);

        return new CityListAdapter.CityListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CityListViewHolder cityListViewHolder, int i) {
        final CityDataHolder item = items.get(i);
        cityListViewHolder.txt_count.setText(String.valueOf(i + 1));
        cityListViewHolder.txt_name.setText(item.getName());
        cityListViewHolder.btn_update_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCity(item.getId(), Integer.parseInt(cityListViewHolder.txt_count.getText().toString()));
            }
        });

        cityListViewHolder.btn_delete_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are You Sure!")
                        .setContentText("Are you sure to delete city!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                              deleteCity(item.getId(), Integer.parseInt(cityListViewHolder.txt_count.getText().toString()),true);
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Request to the server for data
     */
    public void deleteCity(final int id, final int p, final boolean check) {

        String strings = context.getResources().getString(R.string.url) + "deleteupdatecity.php";
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


                        boolean insert = jsonObject.getBoolean("deletecity");
                        if(check) {
                            if (insert) {
                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("City Deleted Successfully!")
                                        .setConfirmText("ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                items.remove(p - 1);
                                                notifyItemRemoved(p - 1);
                                                notifyItemRangeChanged(p - 1, items.size());
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
                        }else {
                            if (insert) {
                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("City Updated Successfully!")
                                        .setConfirmText("ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                items.get(p-1).setName(editText.getText().toString());
                                                notifyDataSetChanged();
                                                sDialog.dismissWithAnimation();
                                                alertDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Something went wrong!")
                                        .show();
                            }
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
                if(check){
                    params.put("city_delete_id", String.valueOf(id));
                }else {
                    params.put("city_update_id", String.valueOf(id));
                    params.put("city_update_name", editText.getText().toString());
                }


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

    public class CityListViewHolder extends RecyclerView.ViewHolder {

        TextView txt_count, txt_name;
        ImageButton btn_update_city, btn_delete_city;

        public CityListViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            btn_update_city = (ImageButton) itemView.findViewById(R.id.btn_update_city);
            btn_delete_city = (ImageButton) itemView.findViewById(R.id.btn_delete_city);


        }
    }


    public void updateCity(final int id,final int p) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_city_dialog, null);
        dialogBuilder.setView(dialogView);

        editText = (EditText) dialogView.findViewById(R.id.edt_update_city_name);
        editText.setText(items.get(p-1).getName());
        Button btn = (Button) dialogView.findViewById(R.id.btn_update_city_name);
        TextView txtCancel = (TextView) dialogView.findViewById(R.id.btn_update_city_cancel);

        alertDialog = dialogBuilder.create();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(context, "Field is required!", Toast.LENGTH_SHORT).show();
                } else {
                   deleteCity(id,p,false);
                }
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


}
