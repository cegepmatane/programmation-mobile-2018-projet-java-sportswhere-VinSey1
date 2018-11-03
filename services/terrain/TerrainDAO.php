<?php
  include_once "../connexion.php";
  class TerrainDAO
  {
    function lireTerrains(){
      global $basededonnees;
      $SQL_LIRE_TERRAINS = "SELECT * FROM terrain";
      $requete = $basededonnees->prepare($SQL_LIRE_TERRAINS);
      $requete->execute();
      $terrains = $requete->fetchAll();

      return $terrains;
    }
  }
?>