package com.esprit.gui;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.pi.MyApplication;
import com.esprit.services.ServiceEvenements;

public class HomeForm extends Form {

    private Button btnAddEvenement;
    private Button btnShowEvenement;
    private Button btnRechercherEvenement;
    private Button btnShowMap;
    private Button btnPosezVosQuestions;
    private Button btnRechercherParNom;
    private Button btnTrierParLocalisation;
    private MyApplication app;
    private ServiceEvenements service;

    public HomeForm() {
        super("Home", BoxLayout.y());
        initGui();
        addActions();
    }
    
    public void init(MyApplication app) {
        this.app = app;
        service = new ServiceEvenements();
    }
    
    private void initGui() {
        btnAddEvenement = new Button("Ajouter", FontImage.createMaterial(FontImage.MATERIAL_ADD, "ButtonIcon", 4.5f));
        btnShowEvenement = new Button("Afficher", FontImage.createMaterial(FontImage.MATERIAL_LIST, "ButtonIcon", 4.5f));
        btnRechercherEvenement = new Button("Rechercher", FontImage.createMaterial(FontImage.MATERIAL_SEARCH, "ButtonIcon", 4.5f));
        btnShowMap = new Button("Afficher la carte", FontImage.createMaterial(FontImage.MATERIAL_MAP, "ButtonIcon", 4.5f));
        btnPosezVosQuestions = new Button("Posez vos questions", FontImage.createMaterial(FontImage.MATERIAL_CHAT, "ButtonIcon", 4.5f));
        btnRechercherParNom = new Button("Rechercher par nom", FontImage.createMaterial(FontImage.MATERIAL_SEARCH, "ButtonIcon", 4.5f));
        btnTrierParLocalisation = new Button("Trier par localisation", FontImage.createMaterial(FontImage.MATERIAL_SORT, "ButtonIcon", 4.5f));
        
        this.addAll(
            new Label("Choisissez une option:"),
            btnAddEvenement,
            btnShowEvenement,
            btnRechercherEvenement,
            btnShowMap,
            btnPosezVosQuestions,
            btnRechercherParNom,
            btnTrierParLocalisation
        );
    }
    
    private void addActions() {
        btnAddEvenement.addActionListener((evt) -> {
            new AjoutEventsForm(this).show();
        });
        
        btnShowEvenement.addActionListener((evt) -> {
            new AffichageEvenementsForm(this).show();
        });
        
        btnRechercherEvenement.addActionListener((evt) -> {
            new RechercherEvenementForm(this).show();
        });
        
        btnShowMap.addActionListener((evt) -> {
            MapForm mapForm = new MapForm();
            mapForm.show();
        });
        
        btnPosezVosQuestions.addActionListener((evt) -> {
            ChatBotForm chatBotForm = new ChatBotForm(this);
            chatBotForm.showForm();
        });
        
        btnRechercherParNom.addActionListener((evt) -> {
            new RechercherParNom(this).show();
        });
        
        btnTrierParLocalisation.addActionListener((evt) -> {
            TrieParLocalisation trieParLocalisation = new TrieParLocalisation(this);
            trieParLocalisation.show();
        });
    }
}
