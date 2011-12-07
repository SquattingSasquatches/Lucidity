<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
include('class.controller.php');

class AddLecture extends Controller
{
	function isProfessorOfCourse( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = ?', array('device_id' => $this->params[$param_names[0]], 'course_id' => $this->params[$param_names[1]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->insert('lectures', array('course_id' => $this->params['course_id'], 'lecture_name' => $this->params['lecture_name'] ) );
		
		$this->db->show_debug_console();
		
 	}
 	protected function onInvalid(){
 	}
}


/* Main */

$controller = new AddLecture();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'lecture_name', 'isParamSet', 'no_lecture_name_supplied', true );
$controller->addValidation( array( 'device_id', 'course_id' ), 'isProfessorOfCourse', 'user_not_professor_of_course', true );

$controller->execute();



?>
