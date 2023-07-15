package com.esprit.gui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.entities.Evenement;
import com.esprit.services.ServiceEvenements;

import java.text.SimpleDateFormat;
import java.util.List;

public class AffichageEvenementsForm extends Form {

    private Form previousForm;
    private ServiceEvenements serviceEvenements;

    public AffichageEvenementsForm(Form previousForm) {
        super("Affichage", BoxLayout.y());
        this.previousForm = previousForm;
        this.serviceEvenements = new ServiceEvenements();
        initGui();
    }

    private void initGui() {
        List<Evenement> evenements = serviceEvenements.afficher();

        this.removeAll();

        for (Evenement evenement : evenements) {
            String nom = evenement.getNom();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormat.format(evenement.getDate());
            String description = evenement.getDescription();
            String localisation = evenement.getLocalisation();
            String nomAdherant = evenement.getNomAdherant();

            String labelText = "Nom: " + nom + ", Date: " + date + ", Description: " + description + ", Localisation: " + localisation + ", Nom Adhérant: " + nomAdherant;
            SpanLabel spanLabel = new SpanLabel(labelText);
            this.add(spanLabel);

            Button modifierButton = new Button(FontImage.createMaterial(FontImage.MATERIAL_EDIT, "Button", 4.0f));
            modifierButton.addActionListener(e -> modifierEvenement(evenement));
            this.add(modifierButton);

            Button supprimerButton = new Button(FontImage.createMaterial(FontImage.MATERIAL_DELETE, "Button", 4.0f));
            supprimerButton.addActionListener(e -> supprimerEvenement(evenement));
            this.add(supprimerButton);
        }

        addActions();
        this.revalidate();
    }

    private void addActions() {
        this.getToolbar().addCommandToLeftBar("Retour", null, (evt) -> {
            previousForm.showBack();
        });
    }

    private void modifierEvenement(Evenement evenement) {
        boolean userConfirmed = Dialog.show("Confirmation", "Voulez-vous modifier cet événement ?", "Oui", "Non");

        if (userConfirmed) {
            AjoutEventsForm ajoutForm = new AjoutEventsForm(previousForm, evenement);
            ajoutForm.show();

            // Mettre à jour l'événement dans le service après la modification
            serviceEvenements.modifier(evenement);

            // Rafraîchir l'affichage de l'événement après la modification
            initGui();
        }
    }

    private void supprimerEvenement(Evenement evenement) {
        boolean userConfirmed = Dialog.show("Confirmation", "Voulez-vous supprimer cet événement ?", "Oui", "Non");

        if (userConfirmed) {
            if (serviceEvenements.supprimer(evenement)) {
                Dialog.show("SUCCÈS", "Événement supprimé !", "OK", null);
                initGui(); // Rafraîchir l'affichage des événements après la suppression
            } else {
                Dialog.show("ERREUR", "Erreur serveur", "OK", null);
            }
        }
    }

}
