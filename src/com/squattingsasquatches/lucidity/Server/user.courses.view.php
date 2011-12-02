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
	function execute()
	{
		
 	$this->db->query('SELECT * `student_courses` AS sc, `courses` AS c WHERE sc.student_id = "' . $this->params['user_id'] . '" AND c.id = sc.course_id');
 	
 	$records = $this->db->fetch_assoc_all();
 	
 	$this->response->addData( $records );
 	
	$this->db->close();
	
	}
}


/* Main function */

$controller = new ViewCourses();


$controller->addValidation( 'user_id', 'isParamSet', 'no_user_id_supplied', true );


if( $controller->validate() ) $controller->execute();

$controller->showView();

?>