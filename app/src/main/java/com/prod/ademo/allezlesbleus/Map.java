package com.prod.ademo.allezlesbleus;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class Map extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker marker;
    LocationRequest mLocationRequest;
    List <MarkerOptions> adresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServicesAvailable()) {
            Toast.makeText(this, "Localisation", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_map);
            initMap();
        } else {
            Toast.makeText(this, "Google Service non valide", Toast.LENGTH_LONG).show();
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            //Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to goole play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if(mGoogleMap != null){
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);

                    TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                    //TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                    //TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                    TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

                    LatLng ll = marker.getPosition();
                    tvLocality.setText(marker.getTitle());
                    //tvLat.setText("Latitude: " + ll.latitude);
                    //tvLng.setText("Longitude: " + ll.longitude);
                    tvSnippet.setText(marker.getSnippet());

                    return v;
                }
            });
        }
        /*goToLocationZoom(48.8304715,2.2845654,15);
        MarkerOptions options =new MarkerOptions()
                                    .title("Paris")
                                    .position(new LatLng(48.8304715,2.2845654));
        mGoogleMap.addMarker(options);*/

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);*/
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        adresses();
    }

    private void setMarker(double lat, double lng ) {
        if (marker != null){
            marker.remove();
        }
        //icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        MarkerOptions options =new MarkerOptions()
                .title("Moi")
                .position(new LatLng(lat,lng)) //new LatLng(48.8304715,2.2845654)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.homme));
        marker= mGoogleMap.addMarker(options);
    }

    public void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update= CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    public void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mGoogleMap.moveCamera(update);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Location change", Toast.LENGTH_LONG).show();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 13);
            setMarker(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(update);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setInterval(10000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            while (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                //Toast.makeText(this, "Arret", Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "try", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        0);
                /*ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                        0);
                ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                        0);*/
                //return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else {
            Toast.makeText(this, "Client must have ACCESS_FINE_LOCATION permission to request PRIORITY_HIGH_ACCURACY locations", Toast.LENGTH_LONG).show();
            return ;
        }
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "not connexion", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "not connexion", Toast.LENGTH_LONG).show();
    }

    private void adresses(){
        adresses=new ArrayList<>();
        MarkerOptions options0 =new MarkerOptions()
                .title("Le Hasard Ludique")
                .position(new LatLng(48.8956653,2.3263892))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Le Hasard Ludique 128, avenue de Saint-Ouen, 75018 Paris");
        mGoogleMap.addMarker(options0);

        MarkerOptions options1 =new MarkerOptions()
                .title("LE JARDIN SUSPENDU")
                .position(new LatLng(48.8308099,2.2811848))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Le Jardin Suspendu 40 RUE D'ORADOUR SUR GLANE, 75015 Paris");
        mGoogleMap.addMarker(options1);

        MarkerOptions options2 =new MarkerOptions()
                .title("LE BAR COMMUN")
                .position(new LatLng(48.8952568,2.3505374))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Bar Commun 135, rue des poissonniers, 75018 Paris");
        mGoogleMap.addMarker(options2);

        MarkerOptions options3 =new MarkerOptions()
                .title("LE FLOW")
                .position(new LatLng(48.8633272,2.3126915))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Le Flow 4, port des Invalides, 75007 Paris");
        mGoogleMap.addMarker(options3);

        MarkerOptions options4 =new MarkerOptions()
                .title("La Bellevilloise")
                .position(new LatLng(48.8687237,2.3898563))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("La Bellevilloise 19-21, rue Boyer , 75020 PARIS");
        mGoogleMap.addMarker(options4);

        MarkerOptions options5 =new MarkerOptions()
                .title("LE POINT ÉPHÉMÈRE")
                .position(new LatLng(48.881275,2.3661533))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Point éphémère 200 Quai de Valmy , 75010 PARIS");
        mGoogleMap.addMarker(options5);

        adresses.add(new MarkerOptions()
                .title("PRINCE PALACE")
                .position(new LatLng(48.8720453,2.3027369))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("59 Rue de Ponthieu 75008 Paris"));

        adresses.add(new MarkerOptions()
                .title("Café Oz Châtelet")
                .position(new LatLng(48.8593398,2.3458208))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("18 Rue Saint-Denis 75001 Paris"));


        adresses.add(new MarkerOptions()
                .title("Scoop Café")
                .position(new LatLng(48.8826717,2.3149016))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("117 rue des Dames  75017 Paris"));

        adresses.add(new MarkerOptions()
                .title("AUX TROIS OBUS")
                .position(new LatLng(48.838657,2.2550428))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("120 rue Michel-Ange  75016 Paris"));

        adresses.add(new MarkerOptions()
                .title("Kanon")
                .position(new LatLng(48.8764782,2.2919012))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("9 rue du Général Lanrezac  75017 Paris"));

        adresses.add(new MarkerOptions()
                .title("Absurde imposture")
                .position(new LatLng(48.8909786,2.343887))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("5 rue Eugène Sue  75018 Paris"));

        adresses.add(new MarkerOptions()
                .title("Thirsty Mad Cat")
                .position(new LatLng(48.8678079,2.3439836))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("120, rue Montmartre 75002 PARIS"));

        adresses.add(new MarkerOptions()
                .title("MEDI TERRA NEA")
                .position(new LatLng(48.8723718,2.340791))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("13 Rue du Faubourg Montmartre  75009 PARIS"));

        adresses.add(new MarkerOptions()
                .title("Madeleine")
                .position(new LatLng(48.8704305,2.345948))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("39 boulevard Bonne Nouvelle  75002 Paris"));

        adresses.add(new MarkerOptions()
                .title("The Great Canadian")
                .position(new LatLng(48.8542692,2.3406846))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("25 Quai des Grands Augustins  75006 Paris"));

        adresses.add(new MarkerOptions()
                .title("French Flair")
                .position(new LatLng(48.8839733,2.3286832))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("75 bis boulevard clichy  75009 Paris"));

        adresses.add(new MarkerOptions()
                .title("O'Sullivans by the Mill")
                .position(new LatLng(48.8841613,2.3298399))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("92 boulevard de Clichy  75018 Paris"));

        adresses.add(new MarkerOptions()
                .title("CALIFORNIA AVENUE")
                .position(new LatLng(48.8596277,2.3461001))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("33, bis rue des Lombards  75001 Paris"));

        adresses.add(new MarkerOptions()
                .title("Bambolina Caffé")
                .position(new LatLng(48.8720575,2.3437129))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("13 rue Rougemont  75009 Paris"));

        adresses.add(new MarkerOptions()
                .title("Café Oz Rooftop")
                .position(new LatLng(48.8408402,2.3677762))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("34 Quai d’Austerlitz  75013 Paris"));

        adresses.add(new MarkerOptions()
                .title("Harp Bar")
                .position(new LatLng(48.8845353,2.3281785))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("118 Boulevard de Clichy  75018 Paris"));

        adresses.add(new MarkerOptions()
                .title("La Panthère Ose")
                .position(new LatLng(48.879348,2.3437199))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("42 rue Rochechouart  75009 Paris"));

        adresses.add(new MarkerOptions()
                .title("Pub Saint Michel")
                .position(new LatLng(48.8539863,2.3419146))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("19, Quai Saint-Michel  75005 Paris"));

        adresses.add(new MarkerOptions()
                .title("Heuresup")
                .position(new LatLng(48.8378612,2.284798))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("299 Rue Lecourbe  75015 Paris"));

        adresses.add(new MarkerOptions()
                .title("Café rive droite")
                .position(new LatLng(48.8608257,2.3466075))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("2 rue berger  75001 Paris"));

        adresses.add(new MarkerOptions()
                .title("La Mercerie")
                .position(new LatLng(48.8656897,2.3759273))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("98 rue Oberkampf  75011 Paris"));

        adresses.add(new MarkerOptions()
                .title("La Terrasse d'Italie")
                .position(new LatLng(48.8289951,2.3547054))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("21 avenue d'italie,  75013 Paris"));

        adresses.add(new MarkerOptions()
                .title("La Seine")
                .position(new LatLng(48.8289951,2.3547054))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("32 ter Boulevard de la Bastille  75012 Paris"));

        adresses.add(new MarkerOptions()
                .title("Café OZ Grands Boulevards")
                .position(new LatLng(48.8289951,2.3547054))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("8, Boulevard Montmartre 75009 Paris"));

        adresses.add(new MarkerOptions()
                .title("Belushi's Gare Du Nord")
                .position(new LatLng(48.8793255,2.3558633))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("5 Rue Dunkerque  75010 Paris"));

        adresses.add(new MarkerOptions()
                .title("L'Impasse")
                .position(new LatLng(48.8662723,2.3770648))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("4 Cité Griset  75011 paris"));

        adresses.add(new MarkerOptions()
                .title("La Taverne du Croissant")
                .position(new LatLng(48.8692932,2.3410334))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("146 rue Montmartre  75002 Paris"));

        adresses.add(new MarkerOptions()
                .title("Pizza Hut-Opera")
                .position(new LatLng(48.870928,2.3329345))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("29 boulevard des Italiens  75002 Paris"));

        adresses.add(new MarkerOptions()
                .title("Pizza Hut-Bonne nouvelle")
                .position(new LatLng(48.870543,2.348154))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("26, boulevard Bonne Nouvelle  75010 Paris"));

        adresses.add(new MarkerOptions()
                .title("Hall's Beer Tavern")
                .position(new LatLng(48.861591,2.3471216))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("68 rue saint denis  75001 Paris"));

        for(int i=0;i<adresses.size();i++){
            mGoogleMap.addMarker(adresses.get(i));
        }

    }
}
