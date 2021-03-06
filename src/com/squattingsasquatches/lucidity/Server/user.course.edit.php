<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id
 */
 
include('class.controller.php');

class EditCourse extends Controller
{
	function isProfessorOfCourse( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = ?', array('device_id' => $this->params[$param_names[0]], 'course_id' => $this->params[$param_names[1]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	
	}
	
	protected function onShowForm(){}
 	protected function onValid(){
 		$db->update('courses', array('course_name' => $this->params['course_name'], 'course_department_prefix' => $this->params['course_department_prefix'],'uni_id' => $this->params['uni_id'], 'start_date' => $this->params['start_date'],'end_date' => $this->params['end_date']     ), 'id = ?', array( $this->params['course_id']) );
		
 	}
 	protected function onInvalid(){
 		
 	}
}


/* Main */

$controller = new EditCourse();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'course_department_prefix', 'isParamSet', 'no_course_department_prefix_supplied', true );
$controller->addValidation( 'course_number', 'isParamSet', 'no_course_number_supplied', true );
$controller->addValidation( array( 'device_id', 'course_id' ), 'isProfessorOfCourse', 'user_not_professor_of_course', true );

$controller->execute();


	
?>