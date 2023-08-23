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
import android.widget.LinearLayout;
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
import com.example.bloodbank.activities.MainActivity;
import com.example.bloodbank.activities.RegisterUserActivity;
import com.example.bloodbank.dataholder.CityDataHolder;
import com.example.bloodbank.dataholder.FeedBackDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FeedBackListAdapter extends RecyclerView.Adapter<FeedBackListAdapter.FeedBackViewHolder> {

    Context context;
    ArrayList<FeedBackDataHolder> items;
    ProgressDialog progressDialog;
    EditText editText;

    AlertDialog alertDialog;

    AlertDialog.Builder dialogBuilder;

    public FeedBackListAdapter(Context context, ArrayList<FeedBackDataHolder> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public FeedBackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.all_city_item, viewGroup, false);

        return new FeedBackListAdapter.FeedBackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedBackViewHolder cityListViewHolder, int i) {
        final FeedBackDataHolder item = items.get(i);
        cityListViewHolder.txt_count.setText(String.valueOf(i + 1));
        cityListViewHolder.txt_name.setText(item.getName());


        if(item.getStatus() == 0){
            cityListViewHolder.lin_feed_back.setBackgroundColor(context.getResources().getColor(R.color.background));
            cityListViewHolder.btn_delete_city.setBackgroundColor(context.getResources().getColor(R.color.background));
        }else{
            cityListViewHolder.lin_feed_back.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            cityListViewHolder.btn_delete_city.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        cityListViewHolder.lin_feed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.feedback_dialog, null);
                dialogBuilder.setView(dialogView);

                Button btn = (Button) dialogView.findViewById(R.id.btn_mark_read);
                TextView txtCancel = (TextView) dialogView.findViewById(R.id.btn_feedback_cancel);
                TextView txt_feed_back_msg = (TextView) dialogView.findViewById(R.id.txt_feed_back_msg);

                txt_feed_back_msg.setText(item.getMsg());

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteCity(item.getId(), Integer.parseInt(cityListViewHolder.txt_count.getText().toString()),false);
                    }
                });
                alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        cityListViewHolder.btn_update_city.setVisibility(View.GONE);



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

        String strings = context.getResources().getString(R.string.url) + "uploadFeedBack.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Something went wrong!")
                                .show();
                    } else {


                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText(jsonObject.getString("msg"))
                                .setConfirmText("ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        if(check){
                                            items.remove(p - 1);
                                            notifyItemRemoved(p - 1);
                                            notifyItemRangeChanged(p - 1, items.size());
                                        }else {
                                            items.get(p-1).setStatus(1);
                                            alertDialog.dismiss();

                                        }
                                        notifyDataSetChanged();
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
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
                    params.put("feedback_delete_id", String.valueOf(id));
                }else {
                    params.put("feedback_update_id", String.valueOf(id));
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

    public class FeedBackViewHolder extends RecyclerView.ViewHolder {

        TextView txt_count, txt_name;
        ImageButton btn_update_city, btn_delete_city;
        LinearLayout lin_feed_back;

        public FeedBackViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            btn_update_city = (ImageButton) itemView.findViewById(R.id.btn_update_city);
            btn_delete_city = (ImageButton) itemView.findViewById(R.id.btn_delete_city);

            lin_feed_back = (LinearLayout) itemView.findViewById(R.id.lin_feed_back);
        }
    }




}
