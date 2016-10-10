$(document).ready(function () {  
    /* 
     * Check password in front-end before the form submit to the system 
     * Password has to be greater than 8 characters 
     * Use id register from form and id password input 
     * Need a div to append the reminder text for user to know 
     */ 
    $("#register").submit(function () { 
        var pass = $("#password").val(); 
        var is_error = 0;
        if(pass.length < 8) { 
            //replace this with append notes later! 
            //alert("Password must be at least 8 chracters long!"); 
            $("#pass_error").html("Password must be at least 8 chracters long"); 
            is_error = 1;
        }

        return is_error == 0; 
    });

    /*
     * Use Ajax to check if the user name is taken
     * This function will get call when the user is typing in the field user name
     */
    $("#username").change(function () {
        
    });
 });
