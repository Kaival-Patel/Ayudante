package com.example.kaival.ayudante;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class searchresults extends AppCompatActivity {
    private ViewStub stubgrid;
    private ViewStub stublist;
    private ListView listView;
    private GridView gridView;
    private ListviewAdapter listviewAdapter;
    private GridViewAdapter gridViewAdapter;
    private int currentviewmode = 0;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
    public List<Recipes> recipesList;
    String data="";
    String singleparsed="";
    String dataparsed="";
    String changeddata="";
    String changedsingleparsed="";
    String changeddataparsed="";
    StringBuffer stringBuffer=new StringBuffer();
    //388bfca1942a479e862a3790bff4e52a
    String myurl="https://www.food2fork.com/api/search?key=a03541b929fea3aa0eb4f8cbc4c60ddb&q=";
    String joinwords="";
    URL url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Recipe list");
        }
        stubgrid = findViewById(R.id.stubgrid);
        stublist = findViewById(R.id.stub_list);
        //inflating viewstub
        stublist.inflate();
        stubgrid.inflate();
        listView = findViewById(R.id.mylistview);
        gridView = findViewById(R.id.mygridview);


        SharedPreferences sharedPreferences = getSharedPreferences("Viewmode", MODE_PRIVATE);
        currentviewmode = sharedPreferences.getInt("currentviewmode", VIEW_MODE_LISTVIEW);
        System.out.println("CURRENT VIEW MODE:" + currentviewmode);
        listView.setOnItemClickListener(onItemClickListener);
        gridView.setOnItemClickListener(onItemClickListener);

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new AsyncCaller().execute();

    }

    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == currentviewmode) {
            listviewAdapter = new ListviewAdapter(this, R.layout.list_item,recipesList);
            listView.setAdapter(listviewAdapter);
        } else {
            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item,recipesList);
            gridView.setAdapter(gridViewAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(searchresults.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    private void switchview() {
        if (VIEW_MODE_GRIDVIEW == currentviewmode) {
            stubgrid.setVisibility(View.VISIBLE);
            stublist.setVisibility(View.GONE);
        } else {
            stubgrid.setVisibility(View.GONE);
            stublist.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }
    public List<Recipes> getRecipeList(){

        recipesList = new ArrayList<>();
        /*recipesList.add(new Recipes(R.drawable.ic_camera_black_24dp,"TITLE 1", "SUBTITLE 1", "URL 1"));
        recipesList.add(new Recipes(R.drawable.ic_camera_black_24dp,"TITLE 2", "SUBTITLE 1", "URL 1"));
        recipesList.add(new Recipes(R.drawable.ic_camera_black_24dp,"TITLE 3", "SUBTITLE 1", "URL 1"));
        recipesList.add(new Recipes(R.drawable.ic_camera_black_24dp,"TITLE 4", "SUBTITLE 1", "URL 1"));
        recipesList.add(new Recipes(R.drawable.ic_camera_black_24dp,"TITLE 5", "SUBTITLE 1", "URL 1"));*/

        try {
            String searchtext = MainActivity.searchet.getText().toString();
            String[] searchwords = searchtext.split(",");
            joinwords = searchtext.replace(",", "%20");

                myurl = myurl + joinwords;
                System.out.println("URL :" + myurl);
                url = new URL(myurl);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data += line;
            }
            JSONObject main = new JSONObject(data);
            for (int i = 0; i < main.length() - 1; i++) {
                JSONArray array = main.getJSONArray("recipes");
                singleparsed = "Number of Results:" + main.get("count") + "\n";
                dataparsed += singleparsed + "\n";
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject jo = (JSONObject) array.get(j);
                        recipesList.add(new Recipes(R.drawable.ic_camera_black_24dp, jo.get("title").toString(), jo.get("publisher").toString(), jo.get("source_url").toString(),jo.get("image_url").toString()));
                    }
                }
            }
        }

        catch(Exception e)
        {
            System.out.println("ERROR:"+e.getMessage());
        }
        System.out.println("RECIPE LIST:"+recipesList);
        return recipesList;
    }

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(),"URL:"+recipesList.get(position).getSourceurl(),Toast.LENGTH_LONG).show();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipesList.get(position).getSourceurl()));

            startActivity(browserIntent);
        }
    };

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchpagemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.switchmenu:
                if(VIEW_MODE_LISTVIEW==currentviewmode)
                {
                    currentviewmode=VIEW_MODE_GRIDVIEW;
                }
                else{
                    currentviewmode=VIEW_MODE_LISTVIEW;
                }
                switchview();
                SharedPreferences sharedPreferences=getSharedPreferences("Viewmode",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("currentviewmode",currentviewmode);
                editor.commit();
                break;
        }
        return true;
    }
class AsyncCaller extends AsyncTask<Void, Void, Void> {
    ProgressDialog pdLoading = new ProgressDialog(searchresults.this);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdLoading.setMessage("\tLoading...");
        pdLoading.show();
    }

    @Override
    protected Void doInBackground(Void... params) {


        //list of Recipes
        getRecipeList();

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        pdLoading.dismiss();
        if(recipesList.size()>0) {
            setAdapters();
        }
        else{
            AlertDialog ad=new AlertDialog.Builder(searchresults.this).setTitle("Recipe Not Found").setMessage("Recipe Not Found!Choose the below options to proceed!").setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   onBackPressed();
                }
            }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onResume();
                }
            }).show();
        }

    }


}
}
