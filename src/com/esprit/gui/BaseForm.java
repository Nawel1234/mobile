package com.esprit.gui;

import com.codename1.db.Database;
import com.codename1.io.Log;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.Layout;
import java.io.IOException;

public class BaseForm extends Form {

    protected Database db;

    public BaseForm(String title, Layout l) {
        super(title, l);
        addActions();
        initializeDatabase();
    }

    private void addActions() {
        getToolbar().addMaterialCommandToSideMenu("Home", FontImage.MATERIAL_HOME, e -> {
            new HomeForm().show();
        });
        // Ajoutez d'autres actions au menu latéral ici
    }

    private void initializeDatabase() {
        try {
            db = Database.openOrCreate("projet_pi");
            createContactTable();
        } catch (IOException ex) {
            Log.e(ex);
        }
    }

    private void createContactTable() {
        try {
            db.execute("CREATE TABLE IF NOT EXISTS contact (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, num TEXT, image TEXT)");
        } catch (IOException ex) {
            Log.e(ex);
        }
    }
}
