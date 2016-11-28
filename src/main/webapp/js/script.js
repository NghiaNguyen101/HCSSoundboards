$(document).ready(function () {  
    /* 
     * Check password in front-end before the form submit to the system 
     * Password has to be greater than 8 characters 
     * Use id register from form and id password input 
     * Check password and confirm password is matched
     */ 
    $("#register").submit(function () { 
        var pass = $("#password").val(); 
        var confirm_pass = $("#confirm-password").val();
        var is_error = 0;

        //check length of the password
        if(pass.length < 8) { 
            $("#pass_length_error").html("Password must be at least 8 chracters long!"); 
            is_error = 1;
        }
        else
            $("#pass_length_error").html("");

        //check password and confirm password are the same
        if(pass != confirm_pass) {
            $("#pass_not_match").html("Password does not match");
            $("#password").val("");
            $("#confirm-password").val("");
            is_error = 1;
        }
        else
            $("#pass_not_match").html("");


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

    //Submit the resolved report form
    $("#confirm_resolved").click(function () {
        $("#resolved_report_form").submit();
    });

    //Display warning to save notes, if changes
    $("#report_notes").change(function () {
        var original_notes = $("#oroginal_notes").val();
        var new_notes = $("#report_notes").val();
        if (new_notes != original_notes)
            $("#warning_save_notes").removeClass("hidden");
        else
            $("#warning_save_notes").addClass("hidden");

    });

    //save report notes
    $("#save_notes_button").click(function () {
       // var reportId = $("#report_id").val();
        var notes = $("#report_notes").val();
        $("#save_notes_report_form").submit();
       // alert(reportId + "\n" + notes + "\n" +"/report/" + reportId + "/save_notes");

       /* $.ajax({
            url: "/report/save_notes",
            data: "reportId=" + reportId + "&notes=" + notes,
            type: "GET",
            success: function (data) {
                alert("Save successful");
            },
            error: function (e) {
                alert("Error: " + e);
            }
        });*/
    });
   /* $("#fixed_title").click(function () {
        $("#fixed_title").css("display", "none");
        $("#input_tile").css("display", "block");
        $("#input_tile").focus();
    });
*/


    var konamiCode = ['ArrowUp', 'ArrowUp', 'ArrowDown', 'ArrowDown', 'ArrowLeft', 'ArrowRight', 'ArrowLeft', 'ArrowRight'];
    var codeIndex = 0;

    $(document).keyup(function(event) {
        if (event.key === konamiCode[codeIndex]) {
            codeIndex++;
            if (codeIndex === 8) {
                randomSounds();
                codeIndex = 0;
            }
        } else {
            codeIndex = 0
        }
    });
 });

//var current;
function playSound(soundId) {
    //current = new Audio('/sound/' + soundId)
    //current.play()
    new Audio('/sound/' + soundId).play()
}

function switchDelete(checkbox, value) {
    if (checkbox.checked == false)  {
        document.getElementById("deleteLabel_" + value).className = "btn btn-default";
        document.getElementById("checkedState_" + value).className = "glyphicon glyphicon-unchecked";
    }
    else {
        document.getElementById("deleteLabel_" + value).className = "btn btn-danger";
        document.getElementById("checkedState_" + value).className = "glyphicon glyphicon-check";
    }
}

/*function switchPlay(soundId) {
    alert("halp")
    document.getElementById(soundId).checked = true;

}*/

function randomSounds() {
    var sounds = $('audio');
    var sound = sounds[Math.floor(Math.random() * sounds.length)];
    $(sound).hover();
    new Audio(sound.src).play();
    setTimeout(randomSounds, 650)
}