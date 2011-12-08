<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id
 */
 
include_once(dirname(  __FILE__ ) . '/class.c2dm.message.php');
include('class.controller.php');

class EndClass extends Controller
{
	function isProfessorOfSection( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = s.professor_id AND s.id = ?', array('device_id' => $this->params['device_id'], 'section_id' => $this->params['section_id'] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	
	}
	
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->query('UPDATE `users` SET in_class = 0 WHERE id in ( SELECT student_id FROM student_courses WHERE section_id = ?)', array($this->params['section_id']));
 		
		$data['action'] = 'CHECK_OUT';
		
		c2dmMessage::sendToSection( $this->params['section_id'], $data);
		
		
 	}
 	protected function onInvalid(){
 		
 	}
}


/* Main */

$controller = new EndClass();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'section_id', 'isParamSet', 'no_section_id_supplied', true );
//$controller->addValidation( 'device_id', 'isProfessorOfSection', 'user_not_professor_of_section', true );

$controller->execute();


	
?>