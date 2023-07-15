package com.esprit.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.entities.Evenement;
import com.esprit.services.ServiceEvenements;

import java.util.List;

public class RechercherParNom extends Form {

    private TextField txtNom;
    private Button btnRechercher;
    private Container resultContainer;
    private ServiceEvenements service;
    private Form previousForm;

    public RechercherParNom(Form previous) {
        super("Rechercher par nom", BoxLayout.y());
        previousForm = previous;
        initGui();
        addActions();
        service = new ServiceEvenements();
    }

    private void initGui() {
        txtNom = new TextField("", "Nom de l'événement");
        btnRechercher = new Button("Rechercher");
        resultContainer = new Container(BoxLayout.y());

        this.addAll(
                new Label("Rechercher un événement par nom:"),
                txtNom,
                btnRechercher,
                resultContainer
        );
    }

    private void addActions() {
        btnRechercher.addActionListener((evt) -> {
            String nom = txtNom.getText();
            rechercherEvenementsParNom(nom);
        });

        Command backCommand = new Command("Retour") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                previousForm.showBack();
            }
        };
        getToolbar().addCommandToLeftBar(backCommand);
    }

    private void rechercherEvenementsParNom(String nom) {
        resultContainer.removeAll();

        List<Evenement> evenements = service.afficher();

        for (Evenement evenement : evenements) {
            if (evenement.getNom().equalsIgnoreCase(nom)) {
                Label lblNom = new Label("Nom: " + evenement.getNom());
                Label lblDate = new Label("Date: " + evenement.getDate().toString());
                Label lblDescription = new Label("Description: " + evenement.getDescription());
                Label lblLocalisation = new Label("Localisation: " + evenement.getLocalisation());

                resultContainer.addAll(lblNom, lblDate, lblDescription, lblLocalisation);
            }
        }

        resultContainer.revalidate();
    }
}
