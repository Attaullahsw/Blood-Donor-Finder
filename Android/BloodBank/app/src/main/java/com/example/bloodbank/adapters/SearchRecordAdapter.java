package com.example.bloodbank.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.MainActivity;
import com.example.bloodbank.activities.ProfileActivity;
import com.example.bloodbank.activities.RegisterUserActivity;
import com.example.bloodbank.dataholder.SearchRecordDataHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class SearchRecordAdapter extends RecyclerView.Adapter<SearchRecordAdapter.SearchRecordViewHolder> {

    ArrayList<SearchRecordDataHolder> items;
    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;
    private Context context;
    private boolean unicheck;


    public SearchRecordAdapter(Context context, ArrayList<SearchRecordDataHolder> items) {
        this.context = context;
        this.items = items;
        unicheck = false;

        logPre = context.getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


    }



    @NonNull
    @Override
    public SearchRecordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.search_record_list_item, viewGroup, false);

        return new SearchRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecordViewHolder viewHolder, int i) {


        final SearchRecordDataHolder item = items.get(i);

        if(item.isUniversal_accepter() && unicheck == false){
            viewHolder.txt_accepter.setVisibility(View.VISIBLE);
            viewHolder.view_accepter_line.setVisibility(View.VISIBLE);
            unicheck = true;
        }

        // viewHolder.profile_image
        viewHolder.txt_name.setText(item.getName());
        viewHolder.txt_address.setText(item.getAddress());
        viewHolder.txt_blood_group.setText(item.getBlood_gropu());
        viewHolder.txt_age.setText("Age:" + item.getAge());
        Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + item.getImage())
                .placeholder(R.drawable.breakimage)
                .error(R.drawable.breakimage)
                .into(viewHolder.profile_image);

        viewHolder.lin_search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logPre.getBoolean("check_login", false)) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("user_id",item.getId());
                    context.startActivity(intent);
                } else {
                    signin();
                }
            }
        });

        viewHolder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logPre.getBoolean("check_login", false)) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + item.getContact_no()));
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    } else {
                        context.startActivity(callIntent);
                    }


                } else {
                    signin();
                }
            }
        });


        viewHolder.img_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logPre.getBoolean("check_login", false)) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Send SMS / Email")
                            .setContentText("Select Your Choice")
                            .setConfirmText("Send SMS")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sendSMSDialog(item.getContact_no());
                                    sDialog.dismissWithAnimation();
                                }
                            }).setCancelButton("Send Email", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            sendEmailDialog(item.getEmail());

                        }
                    }).show();
                } else {
                    signin();
                }



            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class SearchRecordViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_image;
        TextView txt_name, txt_address, txt_blood_group, txt_age,txt_accepter;
        ImageView img_call, img_email;
        LinearLayout lin_search_list;
        View view_accepter_line;



        public SearchRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = (CircleImageView) itemView.findViewById(R.id.profile_image);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            txt_blood_group = (TextView) itemView.findViewById(R.id.txt_blood_group);
            txt_age = (TextView) itemView.findViewById(R.id.txt_age);

            txt_accepter = (TextView) itemView.findViewById(R.id.txt_accepter);
            view_accepter_line = (View) itemView.findViewById(R.id.view_accepter_line);

            img_call = (ImageView) itemView.findViewById(R.id.img_call);
            img_email = (ImageView) itemView.findViewById(R.id.img_email);



            lin_search_list = (LinearLayout) itemView.findViewById(R.id.lin_search_list);
        }
    }


    public void sendSMSDialog(final String c){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sendmessage, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_send_message);
        Button btn = (Button) dialogView.findViewById(R.id.btn_message_send);
        TextView txtCancel = (TextView) dialogView.findViewById(R.id.btn_send_message_cancel);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = editText.getText().toString();
                if(msg.equals("")){
                    Toast.makeText(context, "Empty Message", Toast.LENGTH_SHORT).show();
                }else {

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                    {
                        try
                        {
                            SmsManager smsMgrVar = SmsManager.getDefault();
                            smsMgrVar.sendTextMessage(c, null, msg, null, null);
                            Toast.makeText(context, "Message Sent",
                                    Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                        }
                        catch (Exception ErrVar)
                        {
                            Toast.makeText(context,ErrVar.getMessage().toString(),
                                    Toast.LENGTH_LONG).show();
                            ErrVar.printStackTrace();
                        }
                    }
                    else
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.SEND_SMS}, 10);
                        }
                    }
                }
            }
        });


        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


    public void sendEmailDialog(final String c){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sendmessage, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_send_message);
        Button btn = (Button) dialogView.findViewById(R.id.btn_message_send);
        TextView txtCancel = (TextView) dialogView.findViewById(R.id.btn_send_message_cancel);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = editText.getText().toString();
                if(msg.equals("")){
                    Toast.makeText(context, "Empty Message", Toast.LENGTH_SHORT).show();
                }else {

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{c});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Need Blood!");
                    emailIntent.putExtra(Intent.EXTRA_TEXT,msg);
                    emailIntent.setType("message/rfc822");
                    context.startActivity(Intent.createChooser(emailIntent, "Chose an email client!"));
                }
            }
        });


        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


    public void signin(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.signuporlogin, null);
        dialogBuilder.setView(dialogView);

        Button btn_sign_in = (Button) dialogView.findViewById(R.id.btn_signin);
        Button btn_sign_up = (Button) dialogView.findViewById(R.id.btn_singUp);
        TextView txtCancel = (TextView) dialogView.findViewById(R.id.btn_sign_cancel);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterUserActivity.class);
                context.startActivity(intent);
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

}
