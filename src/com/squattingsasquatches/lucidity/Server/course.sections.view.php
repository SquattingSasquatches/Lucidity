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
 
class CourseSectionsView extends Controller
{
 	
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->query('SELECT s.id, s.name as section_name, s.course_id, course_number, short_name as subject_prefix, u.name as professor_name, location, days, start_time, end_time FROM sections AS s, courses AS c, subjects as sub, users as u WHERE s.course_id = ? AND s.course_id = c.id AND c.subject_id = sub.id AND u.id = s.professor_id', array("course_id" => $this->params['course_id'] ));
 		//$this->db->query('select Sc.id, Sc.name as section_name, is_verified, course_id, course_number, short_name as subject_prefix, P.name as professor_name, location, days, start_time, end_time from `sections` as Sc inner join `courses` as C on course_id = C.id inner join `subjects` as Sb on subject_id = Sb.id inner join `users` as P on professor_id = P.id where course_id = ' . $this->params['course_id']);
 	
	 	$records = $this->db->fetch_assoc_all();
	
		$this->response->addData( $records );
		
 	}
 	protected function onInvalid(){}
}
 

 
$controller = new CourseSectionsView();

$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );

$controller->execute();



?>