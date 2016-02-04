package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.secondhandtradingplatform.Login;
import com.example.user.secondhandtradingplatform.R;
import com.example.user.secondhandtradingplatform.Register;
import com.example.user.secondhandtradingplatform.addGadget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import RealmModel.RealmCamera;
import RealmModel.RealmProduct;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import product.earphone;
import user.UserLocalStore;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserLocalStore userLocalStore;
    private Realm realm;
    private int count = 0;
    public static final String tag = "getProductList";
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            count = savedInstanceState.getInt("count");
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userLocalStore = new UserLocalStore(this);
       if(userLocalStore.getRefreshStatus() == true){
           new loadAllProducts().execute();
           new getProductList().execute();
           userLocalStore.setRefreshStatus(false);
       }
        Fragment frag = new CameraFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, frag);
        fragmentTransaction.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent myIntent = new Intent(Main.this, addGadget.class);
                startActivity(myIntent);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("OnStart");
        authenticate();
    }

    private boolean authenticate(){
        if(userLocalStore.getLoggedInUser() == null){
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // After User login change the login button into logout button
        if(authenticate() == true){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setTitle(getString(R.string.logout));
            menu.findItem(R.id.nav_register).setVisible(false);
            //Update nav_header
            TextView username = (TextView) findViewById(R.id.username);
            TextView email = (TextView) findViewById(R.id.email);
            username.setText(userLocalStore.getLoggedInUser().getUsername().toString());
            email.setText(userLocalStore.getLoggedInUser().getEmail().toString());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
       Fragment fragment = null;
        String title = getString(R.string.app_name);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {

            fragment = new CameraFragment();
            title = getString(R.string.title_Camera);
            // Handle the camera action
        } else if (id == R.id.nav_tablet) {
            title = getString(R.string.title_Tablet);
        } else if (id == R.id.nav_smartphone) {

        } else if (id == R.id.nav_games) {

        } else if (id == R.id.nav_login) {
            if(authenticate() == true){
             logoutMessage();
              NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
              Menu menu = navigationView.getMenu();
              menu.findItem(R.id.nav_login).setTitle(getString(R.string.title_activity_login));
                menu.findItem(R.id.nav_register).setVisible(true);
                //Update nav_header
                TextView username = (TextView) findViewById(R.id.username);
                TextView email = (TextView) findViewById(R.id.email);
                username.setText("");
                email.setText("");
          }else{
              Intent myIntent = new Intent(this, Login.class);
              startActivity(myIntent);
              finish();
          }
        } else if (id == R.id.nav_register) {
            Intent myIntent = new Intent(this, Register.class);
            startActivity(myIntent);
            finish();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutMessage(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Main.this);
        dialogBuilder.setMessage("確定要登出嗎？");
        dialogBuilder.setTitle("提示");
        dialogBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

            }
        });
        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.show();
    }

    public  class loadAllProducts extends AsyncTask<Void, Void, Void>{

        public loadAllProducts() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            realm = Realm.getInstance(getApplicationContext());
            clearDB(realm);
            try {
                URL url = new URL(SERVER_ADDRESS + "retrieveGadget.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                Log.i("loadGadget", "Start Reading from Server");
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String json = sb.toString();
                //Parse JSON
                JSONObject jObject = new JSONObject(json);
                String gadget = jObject.getString("gadgets");
                JSONArray gadgetArray = new JSONArray(gadget);

                Log.i("loadGadget", gadget.toString());

                for(int i=0; i< gadgetArray.length(); i++){
                    JSONObject obj = gadgetArray.getJSONObject(i);
                     int pid = obj.getInt("product_id");
                     String brand = obj.getString("brand");
                     String model =  obj.getString("model");
                     String warranty = obj.getString("warranty");
                     String price = obj.getString("price");
                     String location = obj.getString("location");
                     String type = obj.getString("type");
                    //Base64 encoded gadget image
                     String image =  obj.getString("path");
                     Log.i("loadGadget", pid + " " + brand + " " + model + " " + warranty + " " +price + " " +location + " " +image);
                    if(type.equals("earphone"))
                    //insert into realm
                    {
                        createEarphoneEntry(realm, pid, brand, model, warranty, price, location, image);
                    }
                  //  clearDB(realm);
                }
                reader.close();
                con.disconnect();
                realm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public  class getProductList extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            realm = Realm.getInstance(getApplicationContext());
        //    clearDB(realm);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("type", "smartphone");
            String query = builder.build().getEncodedQuery();
            String JSONResponse = getResponseFromServer("getProductList", query);
            Log.i(tag,JSONResponse);
            try{
                  JSONObject jObject = new JSONObject(JSONResponse);
                  String product = jObject.getString("products");
                  JSONArray productArray = new JSONArray(product);
                 for(int i=0; i < productArray.length();i++){
                     JSONObject obj = productArray.getJSONObject(i);
                     String brand = obj.getString("brand");
                     String model =  obj.getString("model");
                     String type = obj.getString("type");
                     String price = obj.getString("price");
                     String os = obj.getString("os");
                     String monitor = obj.getString("monitor");
                     String storage = obj.getString("storage");
                     String camera = obj.getString("camera");
                     String path = obj.getString("path");

                     createProductEntry(realm, brand, model, type, price, os, monitor, storage, camera, path);
                 }
             }catch (Exception e){
                  e.printStackTrace();
             }
                 return null;
        }
    }

    private String getResponseFromServer(String php, String query){
        String json = null;
        try{
           URL url = new URL(SERVER_ADDRESS + php +".php");
           HttpURLConnection con = (HttpURLConnection) url.openConnection();
           con.setRequestMethod("POST");
           con.setDoInput(true);
            if(query!=null){
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();
            }
           BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
           StringBuilder sb = new StringBuilder();
           String line;
           while ((line = reader.readLine()) != null) {
               sb.append(line + "\n");
           }
          json= sb.toString();
       }catch(Exception e){
           e.printStackTrace();
       }
        return json;
    }
    private void createProductEntry(Realm realm, String brand, String model, String type, String price, String os, String monitor, String storage, String camera, String path){
        realm.beginTransaction();
        RealmProduct rp = realm.createObject(RealmProduct.class);
        rp.setPrice(price);
        rp.setBrand(brand);
        rp.setModel(model);
        rp.setCamera(camera);
        rp.setMonitor(monitor);
        rp.setType(type);
        rp.setOs(os);
        rp.setStorage(storage);
        rp.setPath(path);
        realm.commitTransaction();
        Log.i(tag, "The inserted Products:");
        Log.i(tag, rp.getBrand() + " " + rp.getModel() + " " + rp.getStorage());
    }
    private void createEarphoneEntry(Realm realm, int pid, String brand, String model, String warranty, String price, String location, String image){
        realm.beginTransaction();
        earphone ep = realm.createObject(earphone.class);
        ep.setPid(pid);
        ep.setBrand(brand);
        ep.setModel(model);
        ep.setWarranty(warranty);
        ep.setPrice(price);
        ep.setLocation(location);
        ep.setImage(image);
        realm.commitTransaction();

  //      earphone ep1 = realm.where(earphone.class).findFirst();
        Log.i("loadGadget", "The inserted gadget:");
        Log.i("loadGadget", ep.getBrand() + " " +ep.getModel());

    }
    private void clearDB(Realm realm){
        realm.beginTransaction();
        realm.allObjects(earphone.class).clear();
        realm.allObjects(RealmCamera.class).clear();
 //       realm.allObjects(RealmProduct.class).clear();
        realm.commitTransaction();
    }
}
