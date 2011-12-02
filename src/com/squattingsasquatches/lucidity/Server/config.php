<?php
	if( PHP_SAPI == "cli" || $_SERVER['SERVER_NAME'] == 'localhost')
	{
		$config['DB_USER'] = 'root';
		$config['DB_PASS'] = 'password';
		$config['DB_HOST'] = 'localhost';
		$config['DB_NAME'] = 'thesouth_lucidity';
		
		
	}
	else
	{
		$config['DB_USER'] = 'root';
		$config['DB_PASS'] = 'lucidity';
		$config['DB_HOST'] = 'http://192.168.1.116:80';
		$config['DB_NAME'] = 'lucidity';
	}
?>