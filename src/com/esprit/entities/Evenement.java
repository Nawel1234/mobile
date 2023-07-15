/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.entities;

import java.util.Date;



/**
 *
 * @author Nawel
 */
public class Evenement {
    private int idEvenement;
    private String nom;
    private Date date;
    private String description;
    private String localisation;
    private int idUser;
    private String nomAdherant;
    
     public Evenement(){};

    public String getNomAdherant() {
        return nomAdherant;
    }

    public void setNomAdherant(String nomAdherant) {
        this.nomAdherant = nomAdherant;
    }

    public Evenement(int idEvenement, String nom, Date date, String description, String localisation, String nomAdherant) {
        this.idEvenement = idEvenement;
        this.nom = nom;
        this.date = date;
        this.description = description;
        this.localisation = localisation;
        this.nomAdherant = nomAdherant;
    }
     
     

    public Evenement(String nom, Date date, String description, String localisation, int idUser) {
        this.nom = nom;
        this.date = date;
        this.description = description;
        this.localisation = localisation;
        this.idUser = idUser;
    }

    public Evenement(int idEvenement, String nom, Date date, String description, String localisation, int idUser) {
        this.idEvenement = idEvenement;
        this.nom = nom;
        this.date = date;
        this.description = description;
        this.localisation = localisation;
        this.idUser = idUser;
    }

    public Evenement(String nom, Date date, String description, String localisation) {
        this.nom = nom;
        this.date = date;
        this.description = description;
        this.localisation = localisation;
    }
    

    public int getIdEvenement() {
        return idEvenement;
    }

    public String getNom() {
        return nom;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLocalisation() {
        return localisation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Evenement{" + "idEvenement=" + idEvenement + ", nom=" + nom + ", date=" + date + ", description=" + description + ", localisation=" + localisation + ", idUser=" + idUser + '}';
    }

    public String getCreateur() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
 }