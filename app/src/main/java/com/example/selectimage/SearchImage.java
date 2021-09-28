package com.example.selectimage;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchImage extends AppCompatActivity {
    ImageView imageView;
    Button button;
    RequestQueue requestQueue;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        BasicNetwork network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        imageView=findViewById(R.id.imageViewSearch);
        button=findViewById(R.id.searchButton);
        editText=findViewById(R.id.searchText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().equals("")){
                    editText.setError("Enter the text");
                }else{
                    searchImage(editText.getText().toString());
                }
            }
        });
    }
    private void searchImage(String input){
        String url="http://www.omdbapi.com/?i=tt3896198&apikey=a1c2576";
        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("Response") == "False"){
                        Log.e("image","cannot be retrive the image");
                        Toast.makeText(SearchImage.this, "Incorrect details", Toast.LENGTH_SHORT).show();
                    }else{
                        if(input.equals(response.get("Title"))){
                            Log.d("image","Image find!");
                            Glide.with(SearchImage.this).load(response.getString("Poster")).into(imageView);

                        }
                       }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Toast.makeText(SearchImage.this, "Failed :"+error.networkResponse, Toast.LENGTH_SHORT).show());
        requestQueue.add(objectRequest);
    }
}