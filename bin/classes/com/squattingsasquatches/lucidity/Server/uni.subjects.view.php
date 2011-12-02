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
 		//$this->db->select('subjects', '*', 'uni_id', array( $this->params['uni_id'] ));
 		$this->db->query("SELECT s.long_name as subject_name, s.short_name as subject_prefix, subject_id FROM subjects as s, uni_subjects as US WHERE US.uni_id =" . $this->params['uni_id'] . " AND s.id = US.subject_id");
	 	$records = $this->db->fetch_assoc_all();
	
		$this->response->addData( $records );
		
		$this->db->close();
 	}
 	
}
 

 
$controller = new UniCoursesView();

$controller->addValidation( 'uni_id', 'isParamSet', 'no_uni_id_supplied', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

	
?>