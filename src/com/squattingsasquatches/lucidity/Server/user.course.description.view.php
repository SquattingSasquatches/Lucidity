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
 
class CourseDescriptionView extends Controller
{
 	function execute()
 	{
 		$this->db->query('select course_number, name as course_name, description as course_description, short_name as subject_prefix from `courses` as C inner join `subjects` as S on subject_id = S.id where C.id = ' . $this->params['course_id']);
 	
	 	$records = $this->db->fetch_assoc_all();
	
		$this->response->addData( $records );
		
		$this->db->close();
 	}
 	
}
 

 
$controller = new CourseDescriptionView();

$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

?>