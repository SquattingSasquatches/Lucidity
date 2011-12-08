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

$controller->execute();



?>