package com.esprit.services;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.entities.Evenement;
import com.esprit.utils.Statics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

public class ServiceEvenements implements IService<Evenement> {

    private boolean responseResult;
    private List<Evenement> evenements;

    private final String URI = Statics.BASE_URL + "/evenement/";
    private Date dateDebut;
    private Date dateFin;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate;

    public ServiceEvenements() {
        evenements = new ArrayList<>();
    }

    public boolean ajouter(Evenement t) {
        ConnectionRequest request = new ConnectionRequest();

        request.setUrl(URI);
        request.setHttpMethod("POST");
        formattedDate = format.format(t.getDate());
        request.addArgument("nom", t.getNom());
        request.addArgument("date", formattedDate);
        request.addArgument("description", t.getDescription());
        request.addArgument("localisation", t.getLocalisation());
        request.addArgument("idUser", String.valueOf(t.getIdUser()));

        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 201; // HTTP 201 CREATED
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }

    public boolean modifier(Evenement t) {
        ConnectionRequest request = new ConnectionRequest();

        request.setUrl(URI + t.getIdEvenement());
        request.setHttpMethod("PUT");

        request.addArgument("nom", t.getNom());
        request.addArgument("date", String.valueOf(t.getDate()));
        request.addArgument("description", t.getDescription());
        request.addArgument("localisation", t.getLocalisation());
        request.addArgument("idUser", String.valueOf(t.getIdUser()));

        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 200; // HTTP 200 OK
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }

    public boolean supprimer(Evenement t) {
        ConnectionRequest request = new ConnectionRequest();

        request.setUrl(URI + t.getIdEvenement());
        request.setHttpMethod("DELETE");

        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 200; // HTTP 200 OK
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }

    public List<Evenement> afficher() {
        ConnectionRequest request = new ConnectionRequest();
        request.setUrl(URI);
        request.setHttpMethod("GET");

        request.addResponseListener((evt) -> {
            try {
                InputStreamReader jsonText = new InputStreamReader(new ByteArrayInputStream(request.getResponseData()),
                        "UTF-8");
                Map<String, Object> result = new JSONParser().parseJSON(jsonText);
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("root");

                for (Map<String, Object> obj : list) {
                    int idEvenement = ((Double) obj.get("IdEvenement")).intValue();
                    String nom = obj.get("nom").toString();
                    String dateString = obj.get("date").toString();

                    Date date = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    date = dateFormat.parse(dateString);

                    String description = obj.get("description").toString();
                    String localisation = obj.get("localisation").toString();
                    String prenom = obj.get("Prenom").toString();
                    String nomA = obj.get("Nom").toString();
                    int idUser = ((Double) obj.get("idUser")).intValue();

                    evenements
                            .add(new Evenement(idEvenement, nom, date, description, localisation, nomA + ' ' + prenom));
                }

                trierParDateDebutFin();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(request);

        return evenements;
    }

    public List<Evenement> afficherTries() {
        List<Evenement> evenementsTries = afficher();
        trierParDateDebutFin();
        return evenementsTries;
    }

    public boolean isDateInRange(Date date, Date dateDebut, Date dateFin) {
        if (dateDebut == null || dateFin == null) {
            return true; // Si l'un des sélecteurs de date est vide, renvoie true pour ne pas filtrer
        }

        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);

        Calendar calDateDebut = Calendar.getInstance();
        calDateDebut.setTime(dateDebut);

        Calendar calDateFin = Calendar.getInstance();
        calDateFin.setTime(dateFin);

        return !calDate.before(calDateDebut) && !calDate.after(calDateFin);
    }

    public void setDateDebut(Date date) {
        this.dateDebut = date;
    }

    public void setDateFin(Date date) {
        this.dateFin = date;
    }

    public void trierParDateDebutFin() {
        Collections.sort(evenements, new Comparator<Evenement>() {
            @Override
            public int compare(Evenement h1, Evenement h2) {
                long timestamp1 = h1.getDate().getTime();
                long timestamp2 = h2.getDate().getTime();

                if (timestamp1 < timestamp2) {
                    return -1;
                } else if (timestamp1 > timestamp2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    // // Méthode pour trier les événements par nom
    // public void trierParNom() {
    // Collections.sort(evenements, (Evenement e1, Evenement e2) ->
    // e1.getNom().compareToIgnoreCase(e2.getNom()));
    //
    // // Mettre à jour l'affichage des événements triés ici
    // }

    // Méthode pour rechercher un événement par nom
    public Evenement rechercherParNom(String nom) {
        for (Evenement evenement : evenements) {
            if (evenement.getNom().equalsIgnoreCase(nom)) {
                return evenement;
            }
        }
        return null; // Retourne null si aucun événement correspondant n'est trouvé
    }

    public void trierParLocalisation() {
        Collections.sort(evenements, new Comparator<Evenement>() {
            @Override
            public int compare(Evenement e1, Evenement e2) {
                return e1.getLocalisation().compareToIgnoreCase(e2.getLocalisation());
            }
        });
    }

}