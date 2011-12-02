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
 		$this->db->query('select C.course_number, C.id as course_id from `courses` as C inner join `subjects` as S on S.id = "' . $this->params['subject_id'] . '"');
 	
	 	$records = $this->db->fetch_assoc_all();
	
		$this->response->addData( $records );
		
		$this->db->close();
 	}
 	
}
 

 
$controller = new UniCoursesView();

$controller->addValidation( 'subject_id', 'isParamSet', 'no_subject_id_supplied', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

	
?>