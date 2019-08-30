package com.example.kaival.ayudante;

import android.os.AsyncTask;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class searchjson extends AsyncTask<Void, Void, Void> {

    String data="";
    String singleparsed="";
    String dataparsed="";
    String myurl="https://www.food2fork.com/api/search?key=a03541b929fea3aa0eb4f8cbc4c60ddb&q=";
    String joinwords="";
    public static String BASE_URL="https://www.food2fork.com/api/search?key=a03541b929fea3aa0eb4f8cbc4c60ddb";



    @Override
    protected Void doInBackground(Void... voids) {
        try{
            String searchtext=MainActivity.searchet.getText().toString();
            String []searchwords=searchtext.split(",");
            joinwords=searchtext.replace(",","%20");
            myurl=myurl+joinwords;
            System.out.println("URL :"+myurl);
            URL url=new URL(myurl);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream=httpURLConnection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while(line!=null)
            {
                line=bufferedReader.readLine();
                data+=line;
            }
            JSONObject main=new JSONObject(data);
            for(int i=0;i<main.length()-1;i++)
            {
                JSONArray array=main.getJSONArray("recipes");
                singleparsed="Number of Results:"+main.get("count")+"\n";
                dataparsed+=singleparsed+"\n";
                if(array!=null)
                {
                    for(int j=0;j<array.length();j++)
                    {
                        JSONObject jo=(JSONObject)array.get(j);
                        //recipesList.add(new Recipes(R.drawable.ic_camera_black_24dp,jo.get("title").toString(),jo.get("publisher").toString(),jo.get("source_url").toString()));
                        /*singleparsed="Title:"+jo.get("title")+"\n"+
                                "Publisher:"+jo.get("publisher")+"\n"
                                     +"Social Rank:"+jo.get("social_rank")+"\n"
                                      +"F2F_URL:"+jo.get("f2f_url")+"\n"
                                    ;

                        dataparsed+=singleparsed+"\n";*/
                    }
                }
            }
            /*URL url=new URL("http://api.myjson.com/bins/l2nx8");
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream=httpURLConnection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while(line!=null)
            {
                line=bufferedReader.readLine();
                data+=line;
            }
            JSONArray main=new JSONArray(data);
            for(int i=0;i<main.length();i++)
            {
                JSONObject jsonObject=(JSONObject) main.get(i);
                singleparsed="Name:"+jsonObject.get("name")+"\n"+
                "College:"+jsonObject.get("college")+"\n";
                dataparsed=dataparsed+singleparsed+"\n";

            }*/

        }
        catch(Exception e)
        {
            System.out.println("ERROR:"+e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

