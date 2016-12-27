package com.customCRM;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Map_Search_Activity extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    String strAdd = "";
    SessionManager manager;
    ProgressDialog pd;

    EditText addressEt;
    EditText distEt;
    Button mapSearch;
    String title;

    double latitude, longitude;
    double Cur_latitude, Cur_longitude;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    SupportMapFragment mapFragment;


    List<Map_Data> recordsList;
//    List<Map_Data> latitudeList ;
//    List<Map_Data> longitudeList;
//    List<Map_Data> distanceList;

//    List<List<Map_Data>> containerList;

    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_map__search, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager = new SessionManager();

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.map_ll);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.maps_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.maps_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.maps_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.maps_parent), mainFont);
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

//                ACTION_UP
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

//                    getFragmentManager().popBackStack();
                    Fragment fragment = new Serches_Activity();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Log.e("frag 3", "" + fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, fragment, null);
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
            }
        });


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_display1);

        recordsList = new ArrayList<Map_Data>();
//        latitudeList=new ArrayList<Map_Data>();
//        longitudeList=new ArrayList<Map_Data>();
//        distanceList=new ArrayList<Map_Data>();
//
//        containerList=new ArrayList<List<Map_Data>>();


        addressEt = (EditText) view.findViewById(R.id.address_map_search);
        distEt = (EditText) view.findViewById(R.id.area_map_search);
        mapSearch = (Button) view.findViewById(R.id.search_btn_mapSearch);


        mapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationFromAddress(getActivity(), addressEt.getText().toString());

                GetSearchMapResult getSearchMapResult = new GetSearchMapResult(latitude, longitude, distEt.getText().toString());
                getSearchMapResult.execute();


            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(Map_Search_Activity.this)
                .addOnConnectionFailedListener(Map_Search_Activity.this)
                .addApi(LocationServices.API)
                .build();


        return view;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        final String encp = encrptVal();

        mMap = googleMap;

//        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        double recordLat;
        double recordLong;
        String recordNo, address;
//        Geocoder geocoder;
//        List<Address> addresses = null;


        for (int i = 0; i < recordsList.size(); i++) {
            recordLat = Double.parseDouble(recordsList.get(i).getLatitude());
            recordLong = Double.parseDouble(recordsList.get(i).getLongitude());
            address = recordsList.get(i).getMapAddress();
            recordNo = recordsList.get(i).getRecordNo();

            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(recordLat, recordLong);

            mMap.addMarker(new MarkerOptions().position(sydney).title(recordNo));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(recordLat, recordLong), 12.0f));
        }

//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//
//                String url = manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_frmSalesProgress.asp&RECDNO=" + marker.getTitle() + "&pagetype=account";
//
//                Toast.makeText(getActivity(),url,Toast.LENGTH_LONG).show();
//            }
//        });

//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Toast.makeText(getActivity(),marker.getTitle().toString(),Toast.LENGTH_LONG);
//            }
//        });
//
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//
//                // Getting view from the layout file info_window_layout
//                View v = getActivity().getLayoutInflater().inflate(R.layout.marker_snippet_layout, null);
//
//                // Getting the position from the marker
//                LatLng latLng = marker.getPosition();
//
//                // Getting reference to the TextView to set latitude
//                TextView tvLat = (TextView) v.findViewById(R.id.address_map);
//
//                // Getting reference to the TextView to set longitude
//                final TextView tvLng = (TextView) v.findViewById(R.id.recordId_map);
//
//                // Setting the latitude
//
//                // Setting the longitude
//                tvLng.setText(""+ marker.getTitle());
//
//                // Returning the view containing InfoWindow contents
//                return v;
//            }
//        });

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//
//        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            LayoutInflater inflater;

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // Getting view from the layout file info_window_layout
                View v = inflater.inflate(R.layout.marker_snippet_layout, null);

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude


                // Getting reference to the TextView to set longitude
                TextView tvaddres = (TextView) v.findViewById(R.id.addressInfoWin);

                // Setting the latitude

                // Setting the longitude
                String faddress = getAddressFromLocation(getContext(), latLng.latitude, latLng.longitude);

                if (arg0.getTitle().equals("You Are Here !")) {
                    Log.e("Address", "Called");
                    tvaddres.setText("You Are Here !");
                } else {
                    tvaddres.setText(faddress);
                }

                Log.e("Address", faddress);
                // Returning the view containing InfoWindow contents
                return v;

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

               /* if(marker.getTitle().equals("You Are Here !"))
                {

                }
                else
                {
                    String url = manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_frmSalesProgress.asp&RECDNO=" + marker.getTitle() + "&pagetype=account";

//                Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();

                    Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg", "map");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView, webviewFragment, null);
                    fragmentTransaction.commit();
                }
//*/
                return false;

            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Toast.makeText(getContext(),"InfoClicked",Toast.LENGTH_SHORT).show();
                if (!marker.getTitle().equals("You Are Here !")) {
                    String url = manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_frmSalesProgress.asp&RECDNO=" + marker.getTitle() + "&pagetype=account";

//                Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg", "map");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView, webviewFragment, null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(getActivity(), CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "map");

                    getActivity().startActivity(intent);
                }
            }
        });


//         Current Position
        LatLng currentLoc = new LatLng(Cur_latitude, Cur_longitude);
        mMap.addMarker(new MarkerOptions().position(currentLoc).title("You Are Here !").snippet("You Are Here !").icon(BitmapDescriptorFactory.fromResource(R.mipmap.cur_marker)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            Log.e("Latitude,Longitude", location.getLatitude() + "," + location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return p1;
    }

    @SuppressLint("LongLogTag")
    public String getAddressFromLocation(final Context context, final double lat, final double longi) {

        final double getLat = lat;
        final double getLon = longi;

        final String[] result = {null};

//        Thread thread = new Thread() {
//            @Override
//            public void run() {

               /* Geocoder geocoder = new Geocoder(context, Locale.getDefault());


                try {

                    List<Address> addressList = geocoder.getFromLocation(getLat, getLon, 1);

                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }

                        sb.append(address.getLocality()).append("\n");

                        result[0] = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e("Error", "Unable connect to Geocoder", e);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                }
            }
*/

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, longi, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.e("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current loction address", "Canont get Address!");
        }
//
//    }
//};

        //thread.start();

        //  Log.e("Address :", result[0]);
//
        return strAdd;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                Log.e("PermissionResult : ","If shouldShowRequestPermissionRationale");
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        2);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else
            {
                Log.e("PermissionResult : ","Else shouldShowRequestPermissionRationale");

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }



//            Toast.makeText(getActivity(),"Please check your location permission in your device settings",Toast.LENGTH_LONG).show();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return;
        }
        else
        {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation != null)
            {
//            addressEt.setText(String.valueOf(mLocation.getLatitude()+","+(String.valueOf(mLocation.getLongitude()))));
                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();

                Cur_latitude=latitude;
                Cur_longitude=longitude;

                GetSearchMapResult getSearchMapResult = new GetSearchMapResult(Cur_latitude, Cur_longitude, "5");
                getSearchMapResult.execute();
            }
            else
            {
                Toast.makeText(getActivity(), "Location not Detected", Toast.LENGTH_SHORT).show();
            }
        }

//        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Tag:", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Error", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    //    @Override
//    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }


    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected())
//        {
//            mGoogleApiClient.disconnect();
//        }
//    }


    private class GetSearchMapResult extends AsyncTask<Void, Void, Void> {
        double latitude, longitude;
        String distance;
        SoapObject resultRequestSOAP = null;

        public GetSearchMapResult(double latitude, double longitude, String s) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = s;
            Log.e("latitude,longitude,add", latitude + "," + longitude + "," + distance);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData", "GetData");
//            pd=new ProgressDialog(Map_Search_Activity.this);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();

        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... params)
        {
//            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetGeoLocation";
            String METHOD_NAME = "GetGeoLocation";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                Log.e("Execute1", "success");

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("longitude", "" + longitude);
                Request.addProperty("latitude", "" + latitude);
                Request.addProperty("Distance", distance);
                Request.addProperty("company_id", manager.getWG(getActivity()));

                Log.e("longitude,latitude,Distance,company_id", longitude + "," + latitude + "," + distance + "," + manager.getWG(mActivity));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                Log.e("Execute2", "success");

                HttpTransportSE transport = new HttpTransportSE(URL, 30000);
                Log.e("Execute3", "success");
                transport.call(SOAP_ACTION, soapEnvelope);
                Log.e("Execute4", "success");

                resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();

//                Log.e("recordid", (String) resultRequestSOAP.getProperty("RECDNO"));
                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP.getProperty(i);
                    String recno = TypeEventData.getProperty("RECDNO").toString();
                    String latitude1 = TypeEventData.getProperty("Latitude").toString();
                    String longitude1 = TypeEventData.getProperty("Longitude").toString();
                    String distance = TypeEventData.getProperty("Distance").toString();
                    String mapAddress = TypeEventData.getProperty("MapAddress").toString();


//                    if(recno.equals("anyType{}"))
//                    {
//                        recno="none";
//                    }else
//                    {
//                        recno=recno+"";
//                    }

                    Map_Data map_data = new Map_Data();

                    map_data.setRecordNo(recno);
                    map_data.setDistance(distance);
                    map_data.setLatitude(latitude1);
                    map_data.setLongitude(longitude1);
                    map_data.setMapAddress(mapAddress);

//                    getAddressFromLocation(Map_Search_Activity.this,Double.parseDouble(latitude1),Double.parseDouble(longitude1));

                    recordsList.add(map_data);

                    Log.e("recordid", recno);
                    Log.e("lat_long", latitude1 + "," + longitude1);
                    Log.e("distance", distance);
                    Log.e("recordlist==>", "" + recordsList);

                }


                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

            } catch (Exception ex) {
                Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>", URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            pd.cancel();

//            Thread thread=new Thread()
//            {
//                @Override
//                public void run()
//                {
//                    getAddressFromLocation(Map_Search_Activity.this,recordsList);
//                }
//            };
//
//            thread.start();

//            mapFragment.getMapAsync(Map_Search_Activity.this);
            if(resultRequestSOAP==null || resultRequestSOAP.toString().equals("anyType{}"))
            {
                Log.e("true","Map");
                new AlertDialog.Builder(getActivity())
                        .setTitle("Result")
                        .setMessage("You have not any record exist near by this location")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })

                        .setIcon(R.drawable.delete_alert)
                        .show();

                mapFragment.getMapAsync(Map_Search_Activity.this);
            }
            else
            {
                Log.e("else","Map");
                mapFragment.getMapAsync(Map_Search_Activity.this);
            }


        }
    }

    //Base64 Encryption...
    private String encrptVal() {
        String encrptVal = null;

        String userName = manager.getUserName(getActivity());
        String pass = manager.getUserPass(getActivity());
        String dbId = manager.getDB(getActivity());
        String wgId = manager.getWG(getActivity());


        try {
            byte[] userNamedata = new byte[0];
            userNamedata = userName.getBytes("UTF-8");
            String userNameBase64 = Base64.encodeToString(userNamedata, Base64.DEFAULT);

            byte[] pass_data = new byte[0];
            pass_data = pass.getBytes("UTF-8");
            String passBase64 = Base64.encodeToString(pass_data, Base64.DEFAULT);

            byte[] DB_data = new byte[0];
            DB_data = dbId.getBytes("UTF-8");
            String dbBase64 = Base64.encodeToString(DB_data, Base64.DEFAULT);

            byte[] WG_data = new byte[0];
            WG_data = wgId.getBytes("UTF-8");
            String wgBase64 = Base64.encodeToString(WG_data, Base64.DEFAULT);

            userNameBase64 = userNameBase64.replace("==", "@");
            passBase64 = passBase64.replace("==", "@");
            wgBase64 = wgBase64.replace("==", "@");
            dbBase64 = dbBase64.replace("==", "@");

            String encrptVaule = userNameBase64.replace("=", "$") + "^" + passBase64.replace("=", "$") + "^" + wgBase64.replace("=", "$") + "^" + dbBase64.replace("=", "$");

            return encrptVaule;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return encrptVal;
    }

    @Override
    public void onResume() {
        super.onResume();

//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
//                {
//                    // handle back button's click listener
//
//                    Fragment fragment = new Dashboard_Activity();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    Log.e("frag 3",""+fragmentManager.getBackStackEntryCount());
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.containerView,fragment,null);
//                    fragmentTransaction.commit();
//
//                    return true;
//                }
//                return false;
//            }
//        });
    }
    @Override
    public void onPause() {
        super.onPause();


        final Fragment fragment = getChildFragmentManager().findFragmentById(R.id.map_display1);
        if(fragment!=null){
            getChildFragmentManager().beginTransaction().remove(fragment).commit();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        final Fragment fragment = getChildFragmentManager().findFragmentById(R.id.map_display1);
        if(fragment!=null){
            getChildFragmentManager().beginTransaction().remove(fragment).commit();

        }

        //       getFragmentManager().beginTransaction().remove(mapFragment).commit();
//        try {
//            SupportMapFragment fragment = (SupportMapFragment) getActivity()
//                    .getSupportFragmentManager().findFragmentById(
//                            R.id.map_display1);
//            if (fragment != null) getFragmentManager().beginTransaction().remove(fragment).commit();
//
//        } catch (IllegalStateException e) {
//            //handle this situation because you are necessary will get
//            //an exception here :-(
//        }

    }


}
