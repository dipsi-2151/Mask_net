package com.example.mask_net;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CustomerAdapter extends ArrayAdapter<Penalty>
{

    Context context;
    int resource;
    String ID;
    List<Penalty> penaltyList;


    CustomerAdapter(Context context, int resource, List<Penalty> list)
    {
        super(context,resource,list);
        this.context=context;
        this.resource=resource;
        this.penaltyList=list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflat=LayoutInflater.from(context);

        View view=inflat.inflate(resource,null,false);

        ImageView iv=view.findViewById(R.id.imageView);
        TextView tvdate=view.findViewById(R.id.textViewdate);
        TextView tvloc=view.findViewById(R.id.textViewloc);
        Button buttontDelete=view.findViewById(R.id.buttonDelete);

        Penalty pt=penaltyList.get(position);
        iv.setImageBitmap(pt.bmp);
        tvdate.setText(pt.getDate());
        tvloc.setText(pt.getLocation());
        ID=pt.getId();


        buttontDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deletePenalty(position);
            }
        });

        return view;

    }



    public void Remove_Penalty(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://52.188.166.61:7000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Remove_Penalty_interface rm_penalty = retrofit.create(Remove_Penalty_interface.class);
        Call<List<Remove_penalty_POJO>> call1 = rm_penalty.getID(id);
        call1.enqueue(new Callback<List<Remove_penalty_POJO>>() {
            @Override
            public void onResponse(Call<List<Remove_penalty_POJO>> call, Response<List<Remove_penalty_POJO>> response) {
                List<Remove_penalty_POJO> rs = response.body();
            }

            @Override
            public void onFailure(Call<List<Remove_penalty_POJO>> call, Throwable t) {

            }
        });
    }

//    public void deletePenalty(final int position)
//    {
//        AlertDialog.Builder builder=new AlertDialog.Builder(context);
//        builder.setTitle("Are you sure you want to pay?");
//
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i)
//            {
//
//                Remove_Penalty(ID);
//                Toast.makeText(getContext(),"ID"+position,Toast.LENGTH_LONG).show();
//                penaltyList.remove(position);
//
//
//            }
//        });
//
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        AlertDialog alertDialog=builder.create();
//        alertDialog.show();
//    }
public void deletePenalty(final int position)
{
    AlertDialog.Builder builder=new AlertDialog.Builder(context);
    builder.setTitle("Are you sure want to delete this?");

    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            penaltyList.remove(position);
            notifyDataSetChanged();


            Toast.makeText(getContext(), "ID  "+ID, Toast.LENGTH_SHORT).show();
            Remove_Penalty(ID);

        }
    });

    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    });
    AlertDialog alertDialog=builder.create();
    alertDialog.show();
}




}

