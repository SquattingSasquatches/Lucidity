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
 	
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->select("courses", "*", "subject_id = ?", array($this->params['subject_id']));
 	
	 	$records = $this->db->fetch_assoc_all();
	
		$this->response->addData( $records );
		
 	}
 	protected function onInvalid(){
 	}
}
 

 
$controller = new SubjectCoursesView();

$controller->addValidation( 'subject_id', 'isParamSet', 'no_subject_id_supplied', true );

$controller->execute();



	
?>