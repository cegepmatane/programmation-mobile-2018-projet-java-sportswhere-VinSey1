<?php

	header("Content-type: text/xml");
	include "../connexion.php";
	$SQL_EVENEMENT = "SELECT * FROM evenement WHERE id = ". $_GET['evenement'];
	$requeteEvenement = $basededonnees->prepare($SQL_EVENEMENT);
	$requeteEvenement->execute();
	$evenement = $requeteEvenement->fetchAll(PDO::FETCH_OBJ);

?>


	<evenement>
		<id_evenement><?=$evenement->id_evenement?></id_evenement>
		<nom><?=$evenement->nom?></nom>	
		<description><?=$evenement->description?></description>	
		<date><?=$evenement->date?></date>
		<nb_interesse><?=$evenement->nb_interesse?></nb_interesse>
		<terrain><?=$evenement->terrain?></terrain>
	</evenement>


