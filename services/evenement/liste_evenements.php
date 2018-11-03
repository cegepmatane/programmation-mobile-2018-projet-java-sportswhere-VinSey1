<?php

	header("Content-type: text/xml");
	include "../connexion.php";
	$SQL_LISTE_EVENEMENTS = "SELECT * FROM evenement";
	$requeteListeEvenements = $basededonnees->prepare($SQL_LISTE_EVENEMENTS);
	$requeteListeEvenements->execute();
	$listeEvenements = $requeteListeEvenements->fetchAll(PDO::FETCH_OBJ);
	
?>

<evenements>

<?php
	foreach($listeEvenements as $evenement)
	{
	?>
	<evenement>
		<id_evenement><?=$evenement->id_evenement?></id_evenement>
		<nom><?=$evenement->nom?></nom>	
		<description><?=$evenement->description?></description>	
		<date><?=$evenement->date?></date>
		<terrain><?=$evenement->terrain?></terrain>
	</evenement>
	<?php
	}
?>

</evenements>