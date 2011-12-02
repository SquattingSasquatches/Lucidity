<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>

    <head>

        <title>Zebra_Database Example</title>

        <base href="">

        <meta http-equiv="content-type" content="text/html;charset=UTF-8">

        <meta http-equiv="Content-Script-Type" content="text/javascript">

        <meta http-equiv="Content-Style-Type" content="text/css">


    </head>

    <body>

    <p>
        Prior to running this example you must first download and install the <a href="http://dev.mysql.com/doc/index-other.html">
        <em>world</em></a> test database from MySQL's website.<br>
        Then, you must edit this file (example.php) and change the settings of <em>host</em>, <em>user name</em>, <em>password</em>
        and <em>database</em> to match your configuration.
    </p>

    <?php

        // THIS EXAMPLE IS VERY BRIEF!
        // CHECK THE DOCUMENTATION TO SEE WHAT METHODS ARE AVAILABLE!

        // include the wrapper class
        require '../Zebra_Database.php';

        // create a new database wrapper object
        $db = new Zebra_Database();

        // turn debugging on
        $db->debug = true;

        // connect to the MySQL server and select the database
        $db->connect(
            'localhost',    // host
            'root',         // user name
            '',             // password
            'world'         // database
        );

        $db->set_charset();

        // Update or save data
        $description = $db->escape('l\'écriture');

        $update = $db->update(
            'test',
            array('value' => $description)
        );

        // show debug console.
        // THIS SHOULD ALWAYS BE PRESENT AT THE END OF YOUR SCRIPTS!
        // debugging should be controlled by setting the "debug" property to TRUE/FALSE
        $db->show_debug_console();

    ?>

    </body>

</html>
