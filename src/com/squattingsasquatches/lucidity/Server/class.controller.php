<?php
/*
 * Created on Nov 24, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */

// Set configuration vars
include_once( dirname( __FILE__ ) . '/config.php');

// Load error handler.
include_once( dirname( __FILE__ ) . '/class.response.php');

// Load database class
include_once( dirname( __FILE__ ) . '/db/class.database.php');  
	

include_once( dirname( __FILE__ ) . '/class.validation.php');
 //ini_set("error_reporting", E_ALL);
 //ini_set("display_errors", 1);


 abstract class Controller
 {
 	protected $params = array();
 	protected $response;
 	protected $db;
 	protected $validations = array();
 	public $showform = false;
 	
 	function __construct(){
 		
		global $config;
		
		// Pool all parameters into a unified array.
		$this->params = array_merge($this->params, $_REQUEST);
		
		
		
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
			if( $android !== false || ( array_key_exists('json', $this->params) && $this->params['json'] == "1"))
				$this->response = new JSONresponse();
			else
		  		$this->response = new HTMLresponse();
		}
	
		
		// Connect to database and set timeout.
		$this->db = new database();
		$this->db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
		$this->db->maxQueryTime = 10;
		
		
		
		// If it is a form being requested, bypass the operation, obtain the data necessary for the 
		// form(specific to operation, and set in the operation file), and show it.
		if ( (array_key_exists( 'form', $this->params ) && $this->params['form'] == true) )
		{
			$this->onShowForm();
			$this->response->sendForm();
		}
	}
	public function addParam( array $param )
	{
		$this->params = array_merge($this->params, $param);
	}
	private function isValid()
	{
		
		foreach( $this->validations as $validation )
		{
			$function = $validation->validation_function;
			foreach( $validation->param_names as $p )
			{
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
	
 	public function execute(){
 		if( $this->isValid() )
 			$this->onValid();
 		else
 			$this->onInvalid();
 			
 		
 		$this->response->send();
 		$this->db->close();
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
 			if (!isset( $this->params[$param_name] ) && !isset( $this->params[$param_name . "[0]"]) && !isset( $this->params[$param_name . "[0][0]"]))
 				return false;
 		}
 		return true;
 	}
 	abstract protected function onShowForm();
 	abstract protected function onValid();
 	abstract protected function onInvalid();
 }
 
?>
