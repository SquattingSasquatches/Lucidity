<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
 class response
 {
 	private $log = array();
 	
 	function add( $message_id, $fatal = false)
 	{
 		$this->log[] = $this->messages[$message_id];
 		
 		if ( $fatal )
 		{
 			switch( $this->output )
 			{
 				default:
 				case 'json':
 					die( json_encode($this->log) );
 				break;
 				case 'plain':
 					die( $this->log );
 				break;
 				case 'cli':
 					foreach( $this->log as $message )
 					{
 						echo $message['type'] . ' ' . $message['id'] . ': ' . $message['message'] . "\n";
 					}
 					die();
 				break;
 			}
 		}
 	}
 	function send( $message = false )
 	{
 		if( $message ) $this->add($message, true);	
 	}
 	function display()
 	{
 		switch( $this->output )
 		{
 			default:
			case 'json':
				echo json_encode($this->log) ;
			break;
			case 'plain':
				print_r( $this->log );
			break;
			case 'cli':
			break;
 		}
 		
 	}
 	
 	function __construct( $output = 'json', $locale = 'en_US')
 	{
 		$this->output = $output;
 		$this->locale = $locale;	
 		
		include( dirname( __FILE__ ) . '/lang/' . $this->locale . '/errors.php');
		
 	}
 }
 
?>
