<?php
/*
 * Created on Nov 24, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
 class Message
 {
 	// Public scope needed for templates.
 	public $id;
 	public $message;
 	public $type;
 	
 	function __construct( $type, $id, $message )
 	{
 		
 		$this->id = $id;
 		$this->type = $type;
 		$this->message = $message;
 	}
 }
 
 class MessageType
 {
 	const Notice = 1;
 	const Error = 2;
 }
?>
