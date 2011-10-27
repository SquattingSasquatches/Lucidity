<?php

	include('init.php');
	
	global $db;
	
	extract( $_REQUEST );
 
 	// Device id not supplied? Get outta here.
 	
 	if( !isset( $device_id ) )
 	{
 		echo json_encode($errors['no_device_id_supplied']);
 		return;
 	}
 	
 	$db->query('SELECT FROM `user_devices` AS ud, `student_courses` AS sc, `courses` AS c WHERE ud.user_id = sc.student_id AND courses.id = sc.course_id') )
 	
 	$records = $db->fetch_assoc_all()
	
	echo json_encode( $records );
	
?>
