<?php
  include_once "../connexion.php";
  class EvenementDAO
  {
    function lireEvenements(){
      global $basededonnees;
      $SQL_LIRE_EVENEMENTS = "SELECT * FROM evenement";
      $requete = $basededonnees->prepare($SQL_LIRE_EVENEMENTS);
      $requete->execute();
      $evenement = $requete->fetchAll();

      return $evenement;
    }

    function ajouterEvenement($evenement){
      global $basededonnees;
      $SQL_AJOUTER_EVENEMENT = "INSERT INTO evenement(nom, description, date, terrain) VALUES(:nom, :description, :date, :terrain);";
      $requeteAjouter = $basededonnees->prepare($SQL_AJOUTER_EVENEMENT);
      $requeteAjouter->bindParam(':nom', $evenement['nom'], PDO::PARAM_STR);
      $requeteAjouter->bindParam(':description', $evenement['description'], PDO::PARAM_INT);
      $requeteAjouter->bindParam(':date', $evenement['date'], PDO::PARAM_INT);
      $requeteAjouter->bindParam(':terrain', $evenement["terrain"], PDO::PARAM_BOOL);

      return $requeteAjouter->execute();
    }
  }
?>
