<?php

	// Set configuration vars
	include( dirname( __FILE__ ) . '/config.php');
	
	// Load database class
	require ( dirname( __FILE__ ) . '/db/class.database.php');  
	
	// Connect to database and set timeout.
	$db = new database();
	$db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
	$db->maxQueryTime = 10;
	
?>