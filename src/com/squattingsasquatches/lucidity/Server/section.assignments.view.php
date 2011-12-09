<?php
/*
 * Created on Nov 24, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: course_id
 * 
 */
 include('class.controller.php');
 
class SectionAssignmentsView extends Controller
{
 	function isStudentOfSection( $param_names )
	{
		$this->db->query(	'SELECT * FROM `quizzes` AS q, `lectures` AS l, `sections` AS s, `user_devices` AS ud, `users` AS u, `student_courses` AS sc WHERE u.id = ud.user_id AND ud.device_id = ? AND l.id = q.lecture_id AND sc.student_id = u.id AND sc.section_id = l.section_id AND l.section_id = ? ',
							array('device_id' => $this->params['device_id'], 'section_id' => $this->params['section_id'] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->select('*', 'assignments', 'section_id = ?', false, false, array($this->params['section_id']));
 	
	 	$records = $this->db->fetch_assoc_all();
	
		$this->response->addData( $records );
		
 	}
 	protected function onInvalid(){}
}
 

 
$controller = new SectionAssignmentsView();

$controller->addValidation( 'section_id', 'isParamSet', 'no_section_id_supplied', true );
$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'section_id', 'isStudentOfSection', 'user_not_student_of_section', true );

$controller->execute();



?>