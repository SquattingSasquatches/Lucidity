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
 	public $errors;
 	
 	function add( $message_id, $fatal = false)
 	{
 		$this->messages[] = $this->errors[$message_id];
 		
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
 				case 'cli':
 					foreach( $this->messages as $message )
 					{
 						echo 'Error ' . $message['id'] . ': ' . $message['message'] . "\n";
 					}
 					die();
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
			case 'cli':
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
