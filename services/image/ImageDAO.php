<?php
	include_once "../connexion.php";
	class ImageDAO
	{
		function ajouterPhoto($image){
			$SQL_AJOUTER_PHOTO = "INSERT into images(lien) VALUES('$image->lien')";
			echo ($SQL_AJOUTER_PHOTO);
			global $basededonnees;
			$requeteAjouterPhoto = $basededonnees->prepare($SQL_AJOUTER_PHOTO);
			$reussite = $requeteAjouterPhoto->execute();
			return $reussite;
		}
	}
?>