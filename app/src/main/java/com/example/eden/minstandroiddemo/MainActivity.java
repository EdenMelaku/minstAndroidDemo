package com.example.eden.minstandroiddemo;
import org.opencv.android.OpenCVLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.divyanshu.draw.widget.DrawView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.opencv.highgui.Highgui.imencode;

public class MainActivity extends AppCompatActivity {
    static DrawView d;
    private Paint mPaint;
    static Mat m;

    Context context=this.getBaseContext();

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        private static final String TAG ="" ;

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    m=new Mat();
                } break;
                default:
                {
                    Log.i(TAG, "error loading opencv");
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         d=(DrawView) findViewById(R.id.draw_view);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        d.setLayerPaint(mPaint);
        Button b=(Button) findViewById(R.id.send);
        final ImageView image = (ImageView) findViewById(R.id.imv);
        final TextView t= (TextView) findViewById(R.id.pred);
        // OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mLoaderCallback);
        View.OnClickListener listner=new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                image.setImageBitmap(getBitmap());
                String n=sendRequest.send(getBitmap(),MainActivity.this);
                t.setText(n);
            }


        };
        b.setOnClickListener(listner);

    }
    public static Bitmap getBitmap()
    {
        //this.measure(100, 100);
        //this.layout(0, 0, 100, 100);
        d.setDrawingCacheEnabled(true);
        d.buildDrawingCache();

        Bitmap bmp = Bitmap.createBitmap(d.getDrawingCache());

        d.setDrawingCacheEnabled(false);


        return bmp;
    }



public void sendImage() throws IOException {



    Bitmap bmp= d.getBitmap();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
   // byte[] byteArray = stream.toByteArray();
    //String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
    //String image = getStringImage(bmp);
    //Toast.makeText(context,"saving bitmap", Toast.LENGTH_LONG).show();



    // encode base64 from image
    //ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
    //byte[] b = baos.toByteArray();
   //String encodedString = Base64.encodeToString(b, Base64.URL_SAFE | Base64.NO_WRAP);
    //String encodedImage =Base64.encodeToString(b,Base64.DEFAULT);

   //String x=MainActivity.get64BaseImage(d.getBitmap());
    //Mat src;
    Bitmap bmp32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
//    Mat mat=new Mat(bmp.getHeight(), bmp.getWidth(), CvType.CV_8UC1);
    //Mat matt=new Mat();
    Mat m=new Mat(bmp.getHeight(),bmp.getWidth(),CvType.CV_8UC3);
    Utils.bitmapToMat(bmp32, m);

    //src = imread(mat,1);

    MatOfByte buf = null;
    imencode(".jpg",m,buf);
    String url = "http://192.168.1.91:5000?image="+buf;

    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(context,"at empty method .", Toast.LENGTH_LONG).show();
                }




            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            System.out.println("******************error response******************* \n "+error);
        }
    });
    RequestQueue queue = Volley.newRequestQueue(this,new HurlStack());

    int socketTimeout = 3000000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    stringRequest.setRetryPolicy(policy);
// Add the request to the RequestQueue.
    Toast.makeText(this , "connecting to   "+ url,Toast.LENGTH_SHORT).show();
    queue.add(stringRequest);


    Toast.makeText(this , "connected  "+ url,Toast.LENGTH_SHORT).show();



}
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
    public static String get64BaseImage (Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }




    }


