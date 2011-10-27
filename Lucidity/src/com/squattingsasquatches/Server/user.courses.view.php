<?php

	include('init.php');
	
	global $db;
	
	extract( $_REQUEST );
 
 	// Device id not supplied? Get outta here.
 	
 	if( !isset( $device_id ) )
 	{
 		echo '1';
 		return;
 	}
 	
 	if( !$db->query('SELECT FROM `user_devices` AS ud, `student_courses` AS sc, `courses` AS c WHERE ud.user_id = sc.student_id AND courses.id = sc.course_id') )
 	{
 		echo '2';
		return; 		
 	}
 	
 	if( !$records = $db_fetch_assoc_all() )
	{
		echo '3';
		return;
	}
	
	echo json_encode( $records );
	
?>
