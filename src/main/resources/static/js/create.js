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
            $("#pass_error").html("Password must be at least 8 chracters long!"); 
            is_error = 1;
        }

        // See if the warning is cleared
        if($("#nameTaken").html() != '')
            is_error = 1;

        return is_error == 0; 
    });

    /*
     * Use Ajax to check if the user name is taken
     * This function will get call when the user is typing in the field user name
     * Set Warning nameTaken if is Taken
     * Clear Warning if good
     */
    $("#username").change(function () {
        $.ajax({
            url: "/checkUsername",
            data: "username=" + $("#username").val(),
            type: "GET",
            success: function(data){
                if(data == "Taken")
                    $("#nameTaken").html("Username already exists!");
                else
                    $("#nameTaken").html("");
            },
            error: function (e) {
                alert("Error: " + e);
            }
        });
    });
 });
