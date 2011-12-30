<?php
/*
 * Created on Oct 26, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id
 * 
 */

include('class.controller.php');

class ViewCourses extends Controller
{
 	protected function onValid(){
 		$this->db->query(	'SELECT p.user_id AS professor_id, c.id AS course_id, start_time, end_time, is_verified, location, days, checked_in, course_number, section_id, short_name AS subject_prefix, u.name AS professor_name, s.name AS section_number FROM `subjects` AS sub, `student_courses` AS sc, `courses` AS c, `sections` AS s, `professors` AS p, `users` AS u WHERE sc.student_id = ? AND s.course_id = c.id AND sc.section_id = s.id AND c.subject_id = sub.id AND p.user_id = s.professor_id AND p.user_id = u.id', 
 						array($this->params['user_id']));
 	
 	
	 	if ( !$records = $this->db->fetch_assoc_all() )
			$this->response->showEmpty(true);
		else
	 		$this->response->addData( $records );
	 	
		
 	}
 	protected function onInvalid(){
 	}
	protected function onShowForm(){
		
	}
}

$controller = new ViewCourses();

$controller->addValidation( 'user_id', 'isParamSet', 'no_user_id_supplied', true );

$controller->execute();


?>