<?php
/*
 * Created on Nov 21, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id
 * 
 */
include('class.controller.php');
 
class UnisView extends Controller
{
 	function execute()
 	{
 		$this->db->query('SELECT * FROM `unis`');
 	
	 	$records = $this->db->fetch_assoc_all();
	
		$this->response->addData( $records );
		
		$this->db->close();
 	}
 	
}
 

 
$controller = new UnisView();

// No validation needed.
$controller->execute();

$controller->showView();
	
?>