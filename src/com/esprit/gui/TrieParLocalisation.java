package com.esprit.gui;

import com.codename1.ui.Command;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.entities.Evenement;
import com.esprit.services.ServiceEvenements;

import java.util.List;

public class TrieParLocalisation extends Form {

    private final ServiceEvenements service;
    private Form previousForm;

    public TrieParLocalisation(Form previous) {
        super("Événements par Localisation", BoxLayout.y());
        previousForm = previous;
        service = new ServiceEvenements();
        afficherEvenementsTries();
        addBackCommand();
    }

    private void afficherEvenementsTries() {
        List<Evenement> evenements = service.afficherTries();

        for (Evenement evenement : evenements) {
            String nom = evenement.getNom();
            String date = evenement.getDate().toString();
            String description = evenement.getDescription();
            String localisation = evenement.getLocalisation();

            Label label = new Label("Nom: " + nom);
            this.add(label);

            label = new Label("Date: " + date);
            this.add(label);

            label = new Label("Description: " + description);
            this.add(label);

            label = new Label("Localisation: " + localisation);
            this.add(label);

            label = new Label("----------------------");
            this.add(label);
        }
    }
    
    private void addBackCommand() {
        Command backCommand = new Command("Retour") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                previousForm.showBack();
            }
        };
        getToolbar().addCommandToLeftBar(backCommand);
    }
}
