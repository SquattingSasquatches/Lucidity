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
 
class UniSubjectsView extends Controller
{
 	protected function universityExists( $param_names )
 	{
 		$this->db->query('SELECT * FROM `universities` WHERE id = ?', array($this->params['uni_id']));
 		
 		if( $this->db->found_rows ) return true;
 		return false;
 		
 		
 	}
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->query("SELECT s.long_name as subject_name, s.short_name as subject_prefix, subject_id FROM subjects as s, uni_subjects as US WHERE US.uni_id =" . $this->params['uni_id'] . " AND s.id = US.subject_id");
	 	$records = $this->db->fetch_assoc_all();
	 	
		$this->response->addData( $records );
		
	}
 	protected function onInvalid(){
 	}
}
 

 
$controller = new UniSubjectsView();

$controller->addValidation( 'uni_id', 'isParamSet', 'no_uni_id_supplied', true );

// TODO: Remove upon migrating to server-dependent subjects.
$controller->addValidation( 'uni_id', 'universityExists', 'no_university_found', true );

$controller->execute();


	
?>