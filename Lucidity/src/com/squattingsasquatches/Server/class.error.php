<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
 class error
 {
 	private $messages = array();
 	
 	function add( $message_id, bool $fatal = false)
 	{
 		global $errors;
 		$messages[] = $errors[$message_id];
 		
 		if ( $fatal )
 		{
 			switch( $this->output )
 			{
 				default:
 				case 'json':
 					die( json_encode($this->messages) );
 				break;
 				case 'plain':
 					die( $this->messages );
 				break;
 			}
 		}
 	}
 	
 	function display()
 	{
 		switch( $this->output )
 		{
 			default:
			case 'json':
				echo json_encode($this->messages) ;
			break;
			case 'plain':
				print_r( $this->messages );
			break;
 		}
 		
 	}
 	
 	function __construct( $output = 'json', $locale = 'en_US')
 	{
 		$this->output = $output;
 		$this->locale = $locale;	
 		
		include( dirname( __FILE__ ) . '/errors.' . $this->locale . '.php');
 	}
 }
 
?>
