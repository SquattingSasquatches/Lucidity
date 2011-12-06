<?php
/*
 * Created on Nov 24, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */

// Set configuration vars
include( dirname( __FILE__ ) . '/config.php');

// Load error handler.
include( dirname( __FILE__ ) . '/class.response.php');

// Load database class
require ( dirname( __FILE__ ) . '/db/class.database.php');  
	

include( dirname( __FILE__ ) . '/class.validation.php');

 class Controller
 {
 	protected $params = array();
 	protected $response;
 	protected $db;
 	protected $validations = array();
 	
 	function __construct(){

		global $config;
			
		if( PHP_SAPI == 'cli')
		{
			$this->response = new CLIresponse();
			
			// Add CLI params to the array.
			for( $i = 1; $i < $argc; $i++ ) {
				$things = split( "=", $argv[$i] );
				$this->addParam( array( $things[0] => $things[1] ) );
			}
		}
		else
		{
			$android = stripos(strtolower($_SERVER['HTTP_USER_AGENT']),'android');
			if( $android !== false )
				$this->response = new JSONresponse();
			else
		  		$this->response = new HTMLresponse();
		}
	
	
		
		// Connect to database and set timeout.
		$this->db = new database();
		$this->db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
		$this->db->maxQueryTime = 10;
		
		$this->params = array_merge($this->params, $_REQUEST);
	}
	function addParam( array $param )
	{
		$this->params = array_merge($this->params, $param);
	}
 	function showView()
 	{
 		$this->response->send();
 	}
 	function execute(){}

	public function validate()
	{
		foreach( $this->validations as $validation )
		{
			$function = $validation->validation_function;
			foreach( $validation->param_names as $p )
			{
				if ($function == 'isNotAlreadyRegistered') echo $p . ' | ';
				if( !is_array( $p )) $p = array( $p );
				if( !$this->$function( $p ) )
				{
					$this->response->addError( $validation->message_id );
					
					if( $validation->fatal )
						return false;
				}
			}
		}
		return true;
	}
	public function addValidation( $param_names, $function, $message_id, $fatal )
	{
		if( !is_array( $param_names )) $param_names = array( $param_names );
		$this->validations[] = new Validation( $param_names, $function, $message_id, $fatal);
	}
 	
 	protected function isParamSet( $param_names )
 	{
 		if( !is_array( $param_names )) $param_names = array( $param_names );	
 		
 		foreach( $param_names as $param_name )
 		{
 			if (!isset( $this->params[$param_name] ))
 				return false;
 		}
 		return true;
 	}
 }
 
?>
