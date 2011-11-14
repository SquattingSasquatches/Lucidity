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
 	public $messages;
 	
 	function add( $message_id, $fatal = false)
 	{
 		$this->messages[] = $this->messages[$message_id] 
 		
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
 						echo $message['type'] . ' ' . $message['id'] . ': ' . $message['message'] . "\n";
 					}
 					die();
 				break;
 			}
 		}
 	}
 	function send( $message = false )
 	{
 		if( $message ) $this->add($message);	
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
 		
		include( dirname( __FILE__ ) . '/lang/' . $this->locale . '/errors.php');
		
 	}
 }
 
?>
