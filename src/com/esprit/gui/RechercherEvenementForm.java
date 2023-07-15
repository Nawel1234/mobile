package com.esprit.gui;

import com.codename1.components.SpanLabel;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.esprit.entities.Evenement;
import com.esprit.services.ServiceEvenements;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.codename1.ui.FontImage;
import java.io.IOException;
import java.util.List;

public class RechercherEvenementForm extends Form {

    private Form previousForm;
    private Picker dateDebutPicker;
    private Picker dateFinPicker;
    private ServiceEvenements SE;
    private Button afficherButton;
    private Button genererPDFButton;

    public RechercherEvenementForm(Form f) {
        super("Rechercher", BoxLayout.y());

        previousForm = f;

        dateDebutPicker = new Picker();
        dateDebutPicker.setType(Display.PICKER_TYPE_DATE);
        dateFinPicker = new Picker();
        dateFinPicker.setType(Display.PICKER_TYPE_DATE);

        this.add(dateDebutPicker);
        this.add(dateFinPicker);

        afficherButton = new Button("Afficher");
        FontImage afficherIcon = FontImage.createMaterial(FontImage.MATERIAL_SEARCH, afficherButton.getStyle());
        afficherButton.setIcon(afficherIcon);
        afficherButton.setUIID("AfficherButton");

        afficherButton.addActionListener((evt) -> {
            afficherTries();
        });
        this.add(afficherButton);

        genererPDFButton = new Button("Générer PDF");
        FontImage pdfIcon = FontImage.createMaterial(FontImage.MATERIAL_FILE_DOWNLOAD, genererPDFButton.getStyle());
        genererPDFButton.setIcon(pdfIcon);
        genererPDFButton.setUIID("GenererPDFButton");

        genererPDFButton.addActionListener((evt) -> {
            try {
                telechargerEvenement();
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }
        });
        this.add(genererPDFButton);

        SE = new ServiceEvenements();
    }

    private void afficherTries() {
        SE.setDateDebut(dateDebutPicker.getDate());
        SE.setDateFin(dateFinPicker.getDate());

        List<Evenement> evenements = SE.afficherTries();
        SE.trierParDateDebutFin();

        this.removeAll();

        for (Evenement evenement : evenements) {
            if (SE.isDateInRange(evenement.getDate(), dateDebutPicker.getDate(), dateFinPicker.getDate())) {
                String nom = evenement.getNom();
                String date = evenement.getDate().toString();
                String description = evenement.getDescription();
                String localisation = evenement.getLocalisation();
                String nomAdherant = String.valueOf(evenement.getNomAdherant());

                String labelText = "Nom: " + nom + ", Date: " + date + ", Description: " + description + ", Localisation: " + localisation + ", nomAdherant: " + nomAdherant;
                SpanLabel spanLabel = new SpanLabel(labelText);
                this.add(spanLabel);
            }
        }

        this.add(new SpanLabel()); // Ajout d'un espace pour une meilleure mise en page

        Button retourButton = new Button("Retour");
        FontImage retourIcon = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, retourButton.getStyle());
        retourButton.setIcon(retourIcon);
        retourButton.setUIID("RetourButton");
        retourButton.addActionListener((evt) -> {
            showBack();
        });
        this.add(retourButton);

        this.revalidate();
        this.getToolbar().addCommandToLeftBar("Return", null, (evt) -> {
            previousForm.showBack();
        });
    }

    public void show() {
        super.show();
    }

    public void showBack() {
        previousForm.showBack();
    }

    private void telechargerEvenement() throws IOException, DocumentException {
        SE.setDateDebut(dateDebutPicker.getDate());
        SE.setDateFin(dateFinPicker.getDate());

        List<Evenement> evenements = SE.afficherTries();
        SE.trierParDateDebutFin();

        if (!evenements.isEmpty()) {
            // Chemin de sauvegarde du fichier PDF sur le périphérique
            String filePath = FileSystemStorage.getInstance().getAppHomePath() + "evenements.pdf";

            // Création du document PDF
            Document document = new Document();
            try {
                // Initialisation du writer PDF
                PdfWriter.getInstance(document, FileSystemStorage.getInstance().openOutputStream(filePath));

                // Ouverture du document
                document.open();

                // Ajout des paragraphes contenant les informations des événements triés
                for (Evenement evenement : evenements) {
                    if (SE.isDateInRange(evenement.getDate(), dateDebutPicker.getDate(), dateFinPicker.getDate())) {
                        String nom = evenement.getNom();
                        String date = evenement.getDate().toString();
                        String description = evenement.getDescription();
                        String localisation = evenement.getLocalisation();
                        String nomAdherant = String.valueOf(evenement.getNomAdherant());

                        String labelText = "Nom: " + nom + ", Date: " + date + ", Description: " + description + ", Localisation: " + localisation + ", nomAdherant: " + nomAdherant;
                        document.add(new Paragraph(labelText));
                    }
                }

                // Fermeture du document
                document.close();

                // Affichage d'une notification de succès
                Dialog.show("Téléchargement terminé", "Le fichier a été téléchargé avec succès.", "OK", null);

                // Ouvrir le fichier PDF avec l'application par défaut du périphérique
                Display.getInstance().execute(filePath);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } else {
            // Gérer le cas où la liste d'événements est vide
            System.out.println("La liste d'événements est vide.");
        }
    }
}
