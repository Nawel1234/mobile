package com.esprit.gui;

import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.maps.Coord;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
//import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapForm {
    private Form f;
    private MapContainer cnt;

    public MapForm() {
        f = new Form();
        cnt = null;

        try {
            cnt = new MapContainer("AIzaSyCy-fMWerzvXcPCV0FDI07hW2DAzs_mnpY");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Button btnMoveCamera = new Button("Mon Pays");
        btnMoveCamera.addActionListener(e -> {
            cnt.setCameraPosition(new Coord(36.8189700, 10.1657900));
        });

        Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
        FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s, Display.getInstance().convertToPixels(3));

        cnt.addTapListener(e -> {
            cnt.clearMapLayers();
            cnt.addMarker(
                    EncodedImage.createFromImage(markerImg, false),
                    cnt.getCoordAtPosition(e.getX(), e.getY()),
                    "" + cnt.getCameraPosition().toString(),
                    "",
                    e3 -> {
                        ToastBar.showMessage("You clicked " + cnt.getName(), FontImage.MATERIAL_PLACE);
                    }
            );

            ConnectionRequest r = new ConnectionRequest();
            r.setPost(false);
            r.setUrl("http://maps.google.com/maps/api/geocode/json?latlng=" + cnt.getCameraPosition().getLatitude() + "," + cnt.getCameraPosition().getLongitude() + "&oe=utf8&sensor=false");
            NetworkManager.getInstance().addToQueueAndWait(r);

            JSONParser jsonp = new JSONParser();
            try {
                Map<String, Object> tasks = jsonp.parseJSON(new CharArrayReader(new String(r.getResponseData()).toCharArray()));
                List<Map<String, Object>> list1 = (List<Map<String, Object>>) tasks.get("results");

                // Traiter les données JSON pour obtenir les informations souhaitées
                // ...
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button btnGoHome = new Button("Accueil");
        btnGoHome.addActionListener(e -> {
            HomeForm homeForm = new HomeForm();
            homeForm.show();
        });

        Container root = new Container();
        f.setLayout(new BorderLayout());
        f.addComponent(BorderLayout.CENTER, cnt);
        f.addComponent(BorderLayout.SOUTH, btnMoveCamera);
        f.addComponent(BorderLayout.NORTH, btnGoHome);
        f.show();
    }

    public void show() {
        f.show();
    }
}
