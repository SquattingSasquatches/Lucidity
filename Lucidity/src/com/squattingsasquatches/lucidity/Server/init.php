<?php

	// Set configuration vars
	include( dirname( __FILE__ ) . '/config.php');
	
	// Load error handler.
	include( dirname( __FILE__ ) . '/class.response.php');
	if( PHP_SAPI == 'cli')
	{
		$response = new response( 'cli' );
	}
	else
	{
		$response = new response();
	}
	
	// Load database class
	require ( dirname( __FILE__ ) . '/db/class.database.php');  
	
	// Connect to database and set timeout.
	$db = new database();
	$db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
	$db->maxQueryTime = 10;
	
?>
