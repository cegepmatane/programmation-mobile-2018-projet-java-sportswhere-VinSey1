<?php 
	header("Content-type: text/xml");
	include "../connexion.php";
	
	$SQL_TERRAIN = "SELECT * FROM terrain WHERE id = " . $_GET['terrain'];
	$requeteTerrain = $basededonnees->prepare($SQL_TERRAIN);
	$requeteTerrain->execute();
	$terrain = $requeteTerrain->fetch(PDO::FETCH_OBJ);
	
?>

<terrain>
		<id_terrain><?=$terrain->id_terrain?></id_terrain>
		<nom><?=$terrain->nom?></nom>
		<ville><?=$terrain->ville?></ville>	
		<description><?=$terrain->description?></description>	
		<images><?=$terrain->image?></images>
		<latitude><?=$terrain->latitude?></latitude>
		<longitude><?=$terrain->longitude?></longitude>
</terrain>