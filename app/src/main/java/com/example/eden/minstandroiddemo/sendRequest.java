package com.example.eden.minstandroiddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

;

import java.io.ByteArrayOutputStream;


public class sendRequest {

@RequiresApi(api = Build.VERSION_CODES.O)
public static String send(Bitmap bmp, Context c ){


    final String num = null;


    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    byte[] byteArray = stream.toByteArray();





    String encodedImage = java.util.Base64.getEncoder().encodeToString(byteArray);



    String url = "http://192.168.1.91:5000?image="+encodedImage;
    final String[] n = {null};
    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    n[0] =response;

                    //Toast.makeText(context,"at empty method .", Toast.LENGTH_LONG).show();
                }




            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            System.out.println("******************error response******************* \n "+error);
        }
    });
    RequestQueue queue = Volley.newRequestQueue(c,new HurlStack());

    int socketTimeout = 3000000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    stringRequest.setRetryPolicy(policy);
// Add the request to the RequestQueue.
    Toast.makeText(c , "connecting to   "+ url,Toast.LENGTH_SHORT).show();
    queue.add(stringRequest);


    Toast.makeText(c , "connected  "+ url,Toast.LENGTH_SHORT).show();

return n[0];



}
}
