<?php
    $usager = 'sportif';
    $motdepasse = 'password';
    $hote = 'localhost';
    $base = 'sportswhere';
    $dsn = 'mysql:dbname='.$base.';host=' . $hote;
    $basededonnees = new PDO($dsn, $usager, $motdepasse);
?>