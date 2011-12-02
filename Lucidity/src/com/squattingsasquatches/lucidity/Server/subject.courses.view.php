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
 
class SubjectCoursesView extends Controller
{
 	function execute()
 	{
 		 
 		$this->db->select("courses", "*", "subject_id = ?", array($this->params['subject_id']));
 	
	 	$records = $db->fetch_assoc_all();
	
		$this->addData( $records );
		
		$this->db->close();
 	}
 	
}
 

 
$controller = new SubjectCoursesView();

$controller->addValidation( 'subject_id', 'isParamSet', 'no_subject_id_supplied', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

	
?>