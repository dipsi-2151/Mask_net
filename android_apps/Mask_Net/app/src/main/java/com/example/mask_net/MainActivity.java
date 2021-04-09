package com.example.mask_net;

import androidx.annotation.NonNull;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.android.volley.RequestQueue;
import com.example.mask_net.FileUtils;

public class MainActivity extends Activity {

    Button btsignin;
    File path;
    Bitmap photo;
    String imagepath;
    String user;
    TextView textView;
    ImageView imageview2;
    List<String> jsonResponses = new ArrayList<>();
    Task task;
    ListView listView;



    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btsignin = findViewById(R.id.btsignin);
        //textView= findViewById(R.id.textView);
        //imageview2=findViewById(R.id.imageView2);
       // listView=findViewById(R.id.list_view);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.bttakeimg);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });


        btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii=new Intent(getApplicationContext(), SecondScreen.class);
                ii.putExtra("user_name",user);
                startActivity(ii);

            }
            });
               //volleyGet();


    }








//   final Task task = new Task(user_name);
//   Toast.makeText(getApplicationContext(),"task"+""+task.user_name,Toast.LENGTH_LONG).show();
//    Call<Task> call = apiInterface.createTask(task);
//    call.enqueue(new Callback<Task>() {
//        @Override
//        public void onResponse(Call<Task> call, Response<Task> response) {
//
//            Task task = response.body();

//            Log.e("keshav", "loginResponse 1 --> " + task);
//            if (task != null) {
//                Log.e("keshav", "getUserId          -->  " + task.getDate());
//                Log.e("keshav", "getFirstName       -->  " + task.getLocation());
//
//                Log.e("keshav", "getProfilePicture  -->  " + task.getLocation());
//
//                String responseCode = task.getResponseCode();
//                Log.e("keshav", "getResponseCode  -->  " + task.getResponseCode());
//                Log.e("keshav", "getResponseMessage  -->  " + task.getResponseMessage());
//                if (responseCode != null && responseCode.equals("404")) {
//                    Toast.makeText(MainActivity.this, "Invalid Login Details \n Please try again", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Welcome " + task.getlocation(), Toast.LENGTH_SHORT).show();
//                }
//            }



//            try {
//                Toast.makeText(getApplicationContext(), "after request", Toast.LENGTH_LONG).show();
//
//                Task task = response.body();
//                Toast.makeText(getApplicationContext(), "after req"+task, Toast.LENGTH_LONG).show();
//            }
//            catch (Exception e)
//            {
//                Log.e("Upload error:", e.getMessage());
//                Toast.makeText(getApplicationContext(),"stacktrace"+e.getMessage(),Toast.LENGTH_LONG).show();
//            }


//        @Override
//        public void onFailure(Call<Task> call, Throwable t) {
//            Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
//            Log.e("Upload error:", t.getMessage());
//            //call.cancel();
//        }
//    });
//}
//    private void loginRetrofit2Api(String user)
//    {
//        final LoginResponse login = new LoginResponse(user);
//        Call<LoginResponse> call1 = apiInterface.createUser(login);
//        call1.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                LoginResponse loginResponse = response.body();
//                Toast.makeText(getApplicationContext(), "after req"+loginResponse.getLocation(), Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
//                call.cancel();
//            }
//        });
//    }

    /*public void volleyGet(){

        String url = "http://52.188.166.61:7000/penalty_user?user_name=user";
        final List<String> jsonResponses = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>()
                {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("REsponse", response.toString());
                    Toast.makeText(getApplicationContext(), "after requestqueue", Toast.LENGTH_LONG).show();
                     JSONArray  jsonArray= response.getJSONArray(user);
                    //JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(getApplicationContext(), jsonArray.getString(1),Toast.LENGTH_LONG).show();
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String date = jsonObject.getString("date");
                        String location = jsonObject.getString("location");
                        String imageurl = jsonObject.getString("img");
                        Bitmap bm=StringToBitMap(imageurl);
                        imageview2.setImageBitmap(bm);
                        textView.append(date + location);
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "in catch error"+ e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("response error:", e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"in volley getMethod error",Toast.LENGTH_LONG).show();
                Log.e("Volley", error.getMessage());
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }
*/



    private void uploadFile() {
        try{
            RequestQueue requestQueue;
            // create upload service client
            FileUploadService service =
                    ServiceGenerator.createService(FileUploadService.class);

            Uri uri = Uri.fromFile(new File(imagepath + "/profile.jpg"));
            Log.i("uri", uri.toString());
            File file = FileUtils.getFile(this, uri);

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            // finally, execute the request
            Call<ResponseBody> call = service.upload(body);
            //Toast.makeText(getApplicationContext(),"Response"+ call.request(),Toast.LENGTH_LONG).show();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {

                    try {
                         user = response.body().string();
                        user= user.substring(1, user.length()-1);
                         Toast.makeText(getApplicationContext(),user,Toast.LENGTH_LONG).show();
                    }

                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Upload error:", t.getMessage());
                }
            });

        }
        catch (Exception e)
        {
            Log.e("upload time error", e.getMessage());
        }


    }

    private String saveToInternalStorage(Bitmap photo){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        /*if(directory.exists()){
            Toast.makeText(getApplicationContext(),"directory exists"+ directory,Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"directory not exists",Toast.LENGTH_LONG).show();
        }*/

        path=new File(directory,"profile.jpg");


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            // Use the compress method on the BitMap object to write image to the OutputStream

            photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

   private void loadImageFromStorage(String path)
    {

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imageView1);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);

             imagepath = saveToInternalStorage(photo);
           // Toast.makeText(getApplicationContext(),imagepath,Toast.LENGTH_LONG).show();
           loadImageFromStorage(imagepath);
            //Uri fileUri = data.getData();
           uploadFile();
        }
    }
}
