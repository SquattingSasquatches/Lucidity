<?php
/*
 * Created on Nov 24, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: uni_id
 * 
 */
 include('class.controller.php');
 
class UniCoursesView extends Controller
{
 	function execute()
 	{
 		$this->db->query('select id as course_id, course_number, name as course_name from `courses` where subject_id = ' . $this->params['subject_id'] .  ' and uni_id = ' . $this->params['uni_id']);
 	
	 	$records = $this->db->fetch_assoc_all();
	
		$this->response->addData( $records );
		
		$this->db->close();
 	}
 	
}
 

 
$controller = new UniCoursesView();

$controller->addValidation( 'uni_id', 'isParamSet', 'no_uni_id_supplied', true );
$controller->addValidation( 'subject_id', 'isParamSet', 'no_subject_id_supplied', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

	
?>