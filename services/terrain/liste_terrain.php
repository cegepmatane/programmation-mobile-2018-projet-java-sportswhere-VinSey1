<?php
	include "../connexion.php";
	include "TerrainDAO.php";
	$terrainDAO = new terrainDAO();
	$listeTerrains = $terrainDAO->lireTerrains();
	header("Content-type: text/xml");
	echo '<?xml version="1.0" encoding="UTF-8"?>';
?>

<terrains>

<?php
	foreach($listeTerrains as $terrain)
	{
	?>
	<terrain>
		<id_terrain><?=$terrain['id_terrain']?></id_terrain>
		<nom><?=$terrain['nom']?></nom>
		<ville><?=$terrain['ville']?></ville>	
		<description><?=$terrain['description']?></description>	
		<images><?=$terrain['image']?></images>
		<latitude><?=$terrain['latitude']?></latitude>
		<longitude><?=$terrain['longitude']?></longitude>
	</terrain>
	<?php
	}
?>

</terrains>