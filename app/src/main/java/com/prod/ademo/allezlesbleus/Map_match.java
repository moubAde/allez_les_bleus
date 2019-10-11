package com.prod.ademo.allezlesbleus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.api.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

public class Map_match extends AppCompatActivity {

    private MapView osm;
    private MapController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_match);

        osm =(MapView) findViewById(R.id.map);
        osm.setTileSource(TileSourceFactory.MAPNIK);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);

        mc= (MapController) osm.getController();
        mc.setZoom(12);

        GeoPoint center= new GeoPoint(-20.1698,-402487);
        mc.animateTo(center);
    }

    public void addMarker(GeoPoint center){
        //Marker marker= new Marker(osm);
        Marker marker= new Marker(-20.1698,-402487);
        //marker.setPosition(center);
        //marker.setAnchor(Marker.ANCHOR CENTER, Marker.ANCHOR BOTTOM)
        //marker.setIcon(getRessources().getDrawable(R.drawable.ic_launcher));

        osm.getOverlays().clear();
        //osm.getOverlays().add(marker);
        osm.invalidate();

    }
}
