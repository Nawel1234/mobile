/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.ComboBox;
import com.esprit.utils.Statics;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @Dridi Nawel
 */
public class ServiceUtilisateur {
      String url = Statics.BASE_URL + "/utilisateur"; // Utilisation de l'URL de base
       ConnectionRequest request = new ConnectionRequest();
 
  private ComboBox<String> comboAdherant;
    
     public void Adherants() {
    request.setUrl(url);
    request.setHttpMethod("GET");
    request.addResponseListener((NetworkEvent evt) -> {
        byte[] responseData = request.getResponseData();
        if (responseData != null) {
            String response = new String(responseData);
            try {
                JSONParser parser = new JSONParser();
                Map<String, Object> result = parser.parseJSON(new CharArrayReader(response.toCharArray()));
                List<Map<String, Object>> utilisateurs = (List<Map<String, Object>>) result.get("utilisateurs");

                // Parcourir la liste des utilisateurs et extraire les noms et prénoms
                for (Map<String, Object> utilisateur : utilisateurs) {
                    String nom = (String) utilisateur.get("Nom");
                    String prenom = (String) utilisateur.get("Prenom");
                    String nomComplet = prenom + " " + nom;
                    comboAdherant.addItem(nomComplet);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    });

    NetworkManager.getInstance().addToQueueAndWait(request);
}
    
    
}
