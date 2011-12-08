<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */

 include( dirname( __FILE__ ) . '/class.message.php');
 //ini_set("error_reporting", E_ALL);
 //ini_set("display_errors", 1);
 abstract class response
 {
 	// To hold any and all errors.
 	public $errors;
 	
 	// Holds the data to be transformed into the appropriate response.
 	public $data = array();
 	
 	
 	// Preset messages. Set in the /lang/errors.php file.
 	public $messages;
 	
 	protected $showEmptySet = false;
 	
 	function __construct( $locale = 'en_US')
 	{
 		$this->locale = $locale;	
 		
		include( dirname( __FILE__ ) . '/lang/' . $this->locale . '/errors.php');
		
 	}
 	function clearData()
 	{
 		$this->data = array();
 	}
 	function addSuccessMessage()
 	{
 		$this->addData( $this->messages['success'] );
 	}
 	function addData( $data, $name = false )
 	{
 		$this->data = array_merge( $this->data, (array)$data );
 	}
 	function addError( $message_id )
 	{
 		if( !array_key_exists( $message_id, $this->messages ) )
 			die( $message_id . " error message missing." );
 			
 		$this->errors[] = $this->messages[$message_id];
 	}
 	function showEmpty( $empty )
 	{
 		$this->showEmptySet = $empty;
 	}
 	function sendErrors(){}
 	function sendData(){}
 	function sendForm(){}
 	function sendEmpty(){}
 	function sendSuccess(){}
 	function send()
 	{
 		if( $this->showEmptySet )
 			$this->sendEmpty();
 		
 		else if( $this->errors )
 			$this->sendErrors();
 		
 		else if( $this->data )
 			$this->sendData();
 		
 		else 
 			$this->sendSuccess();
 		
 		die();
 	}
 	
 }
 class JSONresponse extends response
 {
 	
 	function sendErrors(){
 		echo json_encode( $this->errors );
 	}
 	function sendData(){
 		echo json_encode( $this->data );
 	}
 	function sendEmpty(){
 		echo json_encode( array() );
 	}
 	function sendSuccess(){
 		echo json_encode(get_object_vars($this->messages['success']));
 	}
 	function addError( $message_id, $fatal = false)
 	{
 		parent::addError( $message_id );
 		
 		if ( $fatal )
 		{
 			die( json_encode($this->errors) );
 		}
 	}
 	
 }
 
 class CLIresponse extends response
 {
 	function send()
 	{
 		if( $this->errors )
 		{
	 		foreach( $this->errors as $error )
			{
				echo $error['type'] . ' ' . $error['id'] . ': ' . $error['message'] . "\n";
			}
 		}
 		else if( $this->data )
 			print_r( $this->data );
 		else 
 			echo json_encode(get_object_vars($this->messages['success']));
 	}
 	function addError( $message_id, $fatal = false)
 	{
 		parent::addError( $message_id );
 		
 		if ( $fatal )
 		{
 			foreach( $this->errors as $error )
			{
				echo $error['type'] . ' ' . $error['id'] . ': ' . $error['message'] . "\n";
			}
			die();
 		}
 	}
 	
 }
 class HTMLresponse extends response
 {
 	private $template;
 	private $smarty;
 	private $formData;
 	
 	function addData( $data )
 	{
 	
 		$this->data = array_merge( $this->data, (array)$data );
 		$this->smarty->assignByRef( 'data', $this->data );
 	}
 	function addFormData( $formData, $name )
 	{
 		
 		$this->formData[$name] = $formData;
 		$this->smarty->assignByRef( 'formData', $this->formData );
 	}
 	function showTemplate( $template_name )
 	{
 		
 		if( !$this->template )
		{
 			echo 'Error: Template not set.';
 			die();
		}
 		if( !$this->smarty->templateExists($template_name) )
 		{
 			echo 'Error: Template not found.';
 			die();
 		}
		$this->smarty->display($template_name);
 	}
 	function sendForm(){
 		$this->showTemplate($this->template . '_error.tpl');
 		die();
 	}
 	
 	
 	function sendErrors(){
 		$this->showTemplate($this->template . '_error.tpl');
 	}
 	
 	
 	function sendData(){
 		$this->showTemplate($this->template . '_success.tpl');
 	}
 	
 	
 	function sendEmpty(){
 		echo "";
 	}
 	
 	
 	function sendSuccess(){
 		$this->showTemplate($this->template . '_success.tpl');
 	}
 	
 	
 	function addError( $message_id, $fatal = false)
 	{
 		parent::addError( $message_id );
 		
 		if ( $fatal )
 			$this->sendErrors();
 	}
 	
 	function setTemplate( $template )
 	{
 		$this->template = $template;
 	}
 	function __construct( $locale = 'en_US', $template = false )
 	{
 		$this->locale = $locale;	
 		
 		if( !$template )
 		{
 			$this->template = substr( $_SERVER['REQUEST_URI'], 1, strpos($_SERVER['REQUEST_URI'], ".php")-1 );
 		}
 		else	
 			$this->template = $template;
		
		
		include( dirname( __FILE__ ) . '/lang/' . $this->locale . '/errors.php');
		
		require( dirname( __FILE__ ) . '/smarty/Smarty.class.php');
		
 		$this->smarty = new Smarty();
 		
 		$this->smarty->assignByRef('errors', $this->errors);
 	}
 }
?>
