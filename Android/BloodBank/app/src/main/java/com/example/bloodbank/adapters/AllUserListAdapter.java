package com.example.bloodbank.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.bloodbank.activities.ProfileActivity;
import com.example.bloodbank.activities.adminactivity.AllUsersActivity;
import com.example.bloodbank.dataholder.SearchRecordDataHolder;
import com.example.bloodbank.util.InternetWarningDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class AllUserListAdapter extends RecyclerView.Adapter<AllUserListAdapter.AllUserRecordViewHolder>{



    ArrayList<SearchRecordDataHolder> items;
    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;
    private Context context;
    ProgressDialog progressDialog;

    static int  count;


    public AllUserListAdapter(Context context, ArrayList<SearchRecordDataHolder> items) {
        this.context = context;
        this.items = items;

        count = 0;

        logPre = context.getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


    }

    @NonNull
    @Override
    public AllUserRecordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.admin_user_item, viewGroup, false);

        return new AllUserListAdapter.AllUserRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllUserRecordViewHolder allUserRecordViewHolder, int i) {


        final SearchRecordDataHolder item = items.get(i);
        count = i;

        // viewHolder.profile_image
        allUserRecordViewHolder.admin_txt_name.setText(item.getName());

        allUserRecordViewHolder.txt_admin_counter.setText(String.valueOf(i));

        allUserRecordViewHolder.admin_txt_address.setText(item.getAddress());
        allUserRecordViewHolder.admin_txt_blood_group.setText(item.getBlood_gropu());
        allUserRecordViewHolder.admin_txt_age.setText("Age:" + item.getAge());
        Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + item.getImage())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(allUserRecordViewHolder.admin_profile_image);


        allUserRecordViewHolder.lin_search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("user_id",item.getId());
                    context.startActivity(intent);
            }
        });

        allUserRecordViewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure!")
                        .setContentText("Are you sure to delete this user?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                deleteCity(item.getId(), Integer.parseInt(allUserRecordViewHolder.txt_admin_counter.getText().toString()));
                                sDialog.dismissWithAnimation();
                            }
                        }).setCancelButton("NO", new SweetAlertDialog.OnSweetClickListener() {
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

    public class AllUserRecordViewHolder extends RecyclerView.ViewHolder {

        CircleImageView admin_profile_image;
        TextView admin_txt_name,admin_txt_address,admin_txt_blood_group,admin_txt_age,txt_admin_counter;
        ImageView img_delete;
        LinearLayout lin_search_list;


        public AllUserRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            admin_profile_image = (CircleImageView) itemView.findViewById(R.id.admin_profile_image);
            admin_txt_name = (TextView) itemView.findViewById(R.id.admin_txt_name);

            txt_admin_counter = (TextView) itemView.findViewById(R.id.txt_admin_counter);

            admin_txt_address = (TextView) itemView.findViewById(R.id.admin_txt_address);
            admin_txt_blood_group = (TextView) itemView.findViewById(R.id.admin_txt_blood_group);
            admin_txt_age = (TextView) itemView.findViewById(R.id.admin_txt_age);


            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);

            lin_search_list = (LinearLayout) itemView.findViewById(R.id.lin_search_list);
        }
    }



    public void deleteCity(final int id,final int p) {

        String strings = context.getResources().getString(R.string.url) + "deleteuser.php";
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

                        boolean deletestatus = jsonObject.getBoolean("deletestatus");
                        if(deletestatus){
                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("User Deleted Successfully!")
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
                        }else {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .show();
                        }
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
                // Toast.makeText(SearchRecordActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(context);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("deleteuserid", String.valueOf(id));



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
