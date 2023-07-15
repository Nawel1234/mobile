package com.esprit.gui;

import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.esprit.entities.Evenement;
import com.esprit.services.ServiceEvenements;
import com.esprit.services.ServiceUtilisateur;
import com.esprit.utils.Statics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.codename1.ui.FontImage;

public class AjoutEventsForm extends Form {

    private TextField tfnom;
    private Picker pickerDate;
    private TextField tfdesc;
    private TextField tflocal;
    private ComboBox<String> comboAdherant;
    private Button btnAjouter;
    private Button btnAfficher;
    private Form previousForm;
    private Evenement evenement;

    private String url = Statics.BASE_URL + "/utilisateur";
    private ConnectionRequest request = new ConnectionRequest();

    public AjoutEventsForm(Form f) {
        super("Ajout", BoxLayout.y());
        previousForm = f;
        this.evenement = null;
        initGui();
        addActions();
    }

    public AjoutEventsForm(Form f, Evenement evenement) {
        super("Ajout", BoxLayout.y());
        previousForm = f;
        this.evenement = evenement;
        initGui();
        addActions();
        fillFields(evenement);
    }

    private void initGui() {
        
        tfnom = new TextField("", "Nom");
        pickerDate = new Picker();
        
        tfdesc = new TextField("", "Description");
        tflocal = new TextField("", "Localisation");
        comboAdherant = new ComboBox<>();
        ServiceUtilisateur su = new ServiceUtilisateur();

      btnAjouter = new Button("Ajouter");
FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_ADD, btnAjouter.getStyle());
btnAjouter.setIcon(icon);
btnAjouter.setUIID("AjouterButton");

        btnAfficher = new Button("Afficher");

        request.setUrl(url);
        request.setHttpMethod("GET");
        request.addResponseListener((NetworkEvent evt) -> {
            byte[] responseData = request.getResponseData();
            if (responseData != null) {
                String response = new String(responseData);
                try {
                    InputStreamReader jsonText = new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8");
                    Map<String, Object> result = new JSONParser().parseJSON(jsonText);
                    List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("root");
                    List<Map<String, Object>> utilisateurs = (List<Map<String, Object>>) result.get("utilisateurs");

                    if (list != null) {
                        for (Map<String, Object> utilisateur : list) {
                            String nom = (String) utilisateur.get("Nom");
                            String prenom = (String) utilisateur.get("Prenom");
                            String nomComplet = prenom + " " + nom;
                            int id = ((Double) utilisateur.get("id")).intValue();
                            comboAdherant.addItem(nomComplet);
                            comboAdherant.putClientProperty(nomComplet, id);
                        }
                    } else {
                        System.out.println("");
                    }
                    System.out.println(list);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(request);

        if (evenement != null) {
            tfnom.setText(evenement.getNom());
            pickerDate.setDate(evenement.getDate());
            tfdesc.setText(evenement.getDescription());
            tflocal.setText(evenement.getLocalisation());
            String nomComplet = evenement.getNomAdherant();
            comboAdherant.setSelectedItem(nomComplet);
        }

        Component[] components = {tfnom, pickerDate, tfdesc, tflocal, comboAdherant, btnAjouter};
        Container container = new Container();
        container.setLayout(BoxLayout.y());
        container.addAll(components);

        this.add(container);
    }

    private void addActions() {
        btnAjouter.addActionListener((evt) -> {
            if (tfnom.getText().isEmpty() || pickerDate.getDate() == null || tfdesc.getText().isEmpty() || tflocal.getText().isEmpty()
                    || comboAdherant.getSelectedItem() == null) {
                Dialog.show("Alerte", "Veuillez remplir tous les champs", "OK", null);
            } else {
                ServiceEvenements SE = new ServiceEvenements();
                String nom = tfnom.getText();
                Date date = pickerDate.getDate();
                pickerDate.setType(Display.PICKER_TYPE_DATE);
                //Date date = pickerDate.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formatDate = dateFormat.format(date);
                Date formattedDate;
                String description = tfdesc.getText();
                String localisation = tflocal.getText();
                String nomComplet = (String) comboAdherant.getSelectedItem();
                int idUser = (int) comboAdherant.getClientProperty(nomComplet);

                Evenement newEvenement = new Evenement(nom, date, description, localisation, idUser);

                if (evenement == null) {
                    if (SE.ajouter(newEvenement)) {
                        Dialog.show("SUCCESS", "Evenement ajouté !", "OK", null);
                    } else {
                        Dialog.show("ERROR", "Erreur serveur", "OK", null);
                    }

                    // Rafraîchir l'affichage des evenements
                    AffichageEvenementsForm afficheForm = new AffichageEvenementsForm(previousForm);
                    afficheForm.show();
                } else {
                    newEvenement.setIdEvenement(evenement.getIdEvenement());
                    if (SE.modifier(newEvenement)) {
                        Dialog.show("SUCCESS", "Evenement modifié !", "OK", null);
                    } else {
                        Dialog.show("ERROR", "Erreur serveur", "OK", null);
                    }

                    // Rafraîchir l'affichage des evenements
                    AffichageEvenementsForm afficheForm = new AffichageEvenementsForm(previousForm);
                    afficheForm.show();
                }
            }
        });

        this.getToolbar().addCommandToLeftBar("Return", null, (evt) -> {
            previousForm.showBack();
        });
    }

    public void fillFields(Evenement evenement) {
        tfnom.setText(evenement.getNom());
        pickerDate.setDate(evenement.getDate());
        tfdesc.setText(evenement.getDescription());
        tflocal.setText(evenement.getLocalisation());
        // Set the selected item in the comboAdherant ComboBox
        String nomComplet = evenement.getNomAdherant();
        comboAdherant.setSelectedItem(nomComplet);
    }
}
