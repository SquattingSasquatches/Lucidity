<?php
/*
 * Created on Oct 26, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id
 * 
 */
 
include('class.controller.php');



class AddCourse extends Controller
{
	function isNotDuplicateCourse( $param_names )
	{
	 	$this->db->query('select id from `courses` where course_number = ? and uni_id = ? and subject_id = ?', array('course_number' => $this->params[$param_names[0]], 'uni_id' => $this->params[$param_names[1]], 'subject_id' => $this->params[$param_names[2]] ));
	 	
	 	if( $this->db->found_rows ) return true;
		
	 	return false;
	}
	function isProfessor( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p WHERE ud.device_id = ? AND p.user_id = ud.user_id', array('device_id' => $this->params[$param_names[0]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	function showView()
	{
		$this->db->select('*', 'subjects');
		
		if( $records = $this->db->fetch_assoc_all() )
			$this->response->addData( $records,'subjects' );
		
		
		$this->db->select('*', 'unis');
		
		
		if( $records = $this->db->fetch_assoc_all() )
			$this->response->addData( $records, 'universities' );
		
		
		
		
		$this->response->addData( $this->params );
	 	
		$this->response->send();
	}
	protected function onShowForm()
	{
		$this->db->select('*', 'subjects');
		
		if( $records = $this->db->fetch_assoc_all() )
			$this->response->addFormData( $records,'subjects' );
		
		
		$this->db->select('*', 'unis');
		
		
		if( $records = $this->db->fetch_assoc_all() )
			$this->response->addFormData( $records, 'universities' );
		
		
		
		
		$this->response->addData( $this->params );
		
 		$this->response->sendForm();
	}
	
 	protected function onValid(){
 		$db->insert('courses', array('course_number' => $this->params['course_number'], 'subject_id' => $this->params['subject_id'], 'uni_id' => $this->params['uni_id'], 'name' => $this->params['course_name'], start_date => $this->params['start_date'], 'end_date' => $this->params['end_date']     ) );
		
 	}
 	protected function onInvalid(){
 	}
}


/* Main */

$controller = new AddCourse();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'subject_id', 'isParamSet', 'no_subject_id_supplied', true );
$controller->addValidation( 'course_name', 'isParamSet', 'no_course_name_supplied', true );
$controller->addValidation( 'course_number', 'isParamSet', 'no_course_number_supplied', true );
$controller->addValidation( 'course_description', 'isParamSet', 'no_course_description_supplied', true );
$controller->addValidation( 'start_date', 'isParamSet', 'no_start_date_supplied', true );
$controller->addValidation( 'end_date', 'isParamSet', 'no_end_date_supplied', true );
$controller->addValidation( array( 'course_number', 'uni_id', 'subject_id' ), 'isNotDuplicateCourse', 'course_already_exists', true );
$controller->addValidation( 'device_id', 'isProfessor', 'user_not_professor_of_course', true );

$controller->execute();

 	
 	
 	
 	
 	
 	
	
?>
