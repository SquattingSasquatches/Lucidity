<?php
/*
 * Created on Nov 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */

class Validation
{
	public $param_names;
	public $validation_function;
	public $fatal;
	public $message_id;
	
	function __construct( $param_names, $function, $message_id, $fatal )
	{
		if( !is_array( $param_names ) ) $param_names = array( $param_names );
		$this->param_names = $param_names;
		$this->validation_function = $function;
		$this->fatal = $fatal;
		$this->message_id = $message_id;
	}
}
?>
