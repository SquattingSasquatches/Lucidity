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
		$config['DB_USER'] = 'androiduser';
		$config['DB_PASS'] = 'alphatest';
		$config['DB_HOST'] = 'db4free.net';
		$config['DB_NAME'] = 'lucidity';
	}
?>