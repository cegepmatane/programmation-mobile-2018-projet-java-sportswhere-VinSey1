<?php
	include "../connexion.php";
	include "EvenementDAO.php";
	$evenementDAO = new EvenementDAO();
	$filtreEvenement = array();
	header("Content-type: text/xml");
	echo '<?xml version="1.0" encoding="UTF-8"?>';

	$filtreEvenement['nom'] = FILTER_SANITIZE_STRING;
	$filtreEvenement['description'] = FILTER_SANITIZE_STRING;
	$filtreEvenement['date'] = FILTER_SANITIZE_NUMBER_INT;
	$filtreEvenement['terrain'] = FILTER_SANITIZE_NUMBER_INT;
	$evenement = filter_var_array($_POST, $filtreEvenement);
	$succes = $evenementDAO->ajouterEvenement($evenement);

?>
<action>
	<type>Ajouter</type>
	<moment><?=time()?></moment>
	<succes><?=$succes?></succes>
</action>