<?php
	if( PHP_SAPI == "cli" || $_SERVER['SERVER_NAME'] == 'localhost')
	{
		$config['DB_USER'] = 'root';
		$config['DB_PASS'] = 'password';
		$config['DB_HOST'] = 'localhost';
		$config['DB_NAME'] = 'thesouth_lucidity';
		if( PHP_SAPI == "cli" ) {
			for($i=1;$i<$argc;$i++) {
				$things = split("=",$argv[$i]);
				$_REQUEST[$things[0]] = $things[1];
			}
		}
		
	}
	else
	{
		$config['DB_USER'] = 'androiduser';
		$config['DB_PASS'] = 'alphatest';
		$config['DB_HOST'] = 'db4free.net';
		$config['DB_NAME'] = 'lucidity';
	}
?>