<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */

 include( dirname( __FILE__ ) . '/class.message.php');
 
 class response
 {
 	// To hold any and all errors.
 	public $errors = array();
 	
 	// Holds the data to be transformed into the appropriate response.
 	public $data = array();
 	
 	// Preset messages. Set in the /lang/errors.php file.
 	public $messages;
 	
 	
 	function __construct( $locale = 'en_US')
 	{
 		$this->locale = $locale;	
 		
		include( dirname( __FILE__ ) . '/lang/' . $this->locale . '/errors.php');
		
 	}
 	function addData( $data )
 	{
 	
 		$this->data = array_merge( $this->data, (array)$data );
 	}
 }
 class JSONresponse extends response
 {
 	
 	function send()
 	{
 		if( $this->errors )
 			echo json_encode($this->errors) ;
 		else if( $this->data )	
 			echo json_encode( $this->data );
 		else
 			echo json_encode(get_object_vars($this->messages['success']));
 	}
 	function addError( $message_id, $fatal = false)
 	{
 		$this->errors[] = $this->messages[$message_id];
 		
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
 		$this->errors[] = $this->messages[$message_id];
 		
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
 	function addData( $data )
 	{
 	
 		$this->data = array_merge( $this->data, (array)$data );
 		$this->smarty->assignByRef( 'data', $this->data );
 	}
 	
 	function send()
 	{
 		if( !$this->template )
 		{
 			echo 'Error: Template not set.';
 			die();
 		}
 		if( $this->errors )
 		{
 			$this->smarty->assign('errors', $this->errors);
	 		if( !$this->smarty->templateExists($this->template . '_error.tpl') )
	 		{
	 			echo 'Error: Template not found.';
	 			die();
	 		}
 			$this->smarty->display($this->template . '_error.tpl');
 		}
 		else if( $this->data )
 		{
 			
	 		if( !$this->smarty->templateExists($this->template . '_success.tpl') )
	 		{
	 			echo 'Error: Template not found.';
	 			die();
	 		}
 			$this->smarty->display($this->template . '_success.tpl');
 		}
 		else
 		{
 			if( !$this->smarty->templateExists($this->template . '_success.tpl') )
	 		{
	 			echo 'Error: Template not found.';
	 			die();
	 		}
 			$this->smarty->display($this->template . '_success.tpl');
 		}
 		
 	}
 	function addError( $message_id, $fatal = false)
 	{
 		$this->errors[] = $this->messages[$message_id];
 		
 		if ( $fatal )
 		{
 			if( !$this->template )
 			{
	 			echo 'Error: Template not set.';
	 			die();
 			}
 			
	 		if( !$this->smarty->templateExists($this->template . '_error.tpl') )
	 		{
	 			echo 'Error: Template ' . $this->template . '_error.tpl not found.';
	 			die();
	 		}
	 		
 			$this->smarty->display($this->template . '_error.tpl');	
 			die();
 		}
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
 		$this->smarty->assignByRef( 'errors', $this->errors );
 	}
 }
?>
