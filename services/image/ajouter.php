<?php
	include "./ImageDAO.php";
	$imageDAO = new ImageDAO();
	$photo = new stdClass();

	$nom = $_POST['nom'];
	$image = $_POST['image'];

	$adresse = "http://158.69.192.249/sportswhere/image/stockage/".$nom.".jpg";
	$photo->url = $adresse;

	$listePhotos = $imageDAO->ajouterPhoto($photo);

	$imageDecodee = base64_decode("$image");
	file_put_contents("stockage/".$nom.".jpg", $imageDecodee);
?>