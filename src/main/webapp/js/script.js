$(document).ready(function () {

    /*
     * Needed for ajax post
     */
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

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
            $("#pass_length_error").html("Password must be at least 8 chracters long!").removeClass("hidden") ;
           // $("#pass_length_error").removeClass("hidden");
            is_error = 1;
        }
        else
            $("#pass_length_error").html("").addClass("hidden");

        //check password and confirm password are the same
        if(pass != confirm_pass) {
            $("#pass_not_match").html("Password does not match").removeClass("hidden");
            $("#password").val("");
            $("#confirm-password").val("");
            is_error = 1;
        }
        else
            $("#pass_not_match").html("").addClass("hidden");


        // See if the warning is cleared
        if($("#nameTaken").html() != '')
            is_error = 1;
        if (is_error)
            $("#register_warning").removeClass("hidden");

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
            data: {username: $("#username").val()},
            type: "POST",
            success: function(data){
                if(data == "Taken") {
                    $("#nameTaken").html("Username already exists!");
                    $("#register_warning, #nameTaken").removeClass("hidden");
                }
                else {
                    $("#nameTaken").html("").addClass("hidden");
                    $("#register_warning").addClass("hidden");
                }
            },
            error: function (e) {
                alert("Error: " + e);
            }
        });
    });

    //Display warning to save notes, if changes
    //Display up to date if not
    $("#report_notes").bind('input',function () {
        var original_notes = $("#original_notes").val();
        var new_notes = $("#report_notes").val();
        if (new_notes != original_notes) {
            $("#warning_save_notes").removeClass("hidden");
            $("#success_save_notes").addClass("hidden");
            $("#save_notes_button").removeClass("disabled");
        }
        else {
            $("#warning_save_notes").addClass("hidden");
            $("#success_save_notes").removeClass("hidden");
            $("#save_notes_button").addClass("disabled");
        }

    });

    //save report notes with ajax post
    //update new notes
    $("#save_notes_button").click(function () {
        var notes = $("#report_notes").val();
        var reportId = $("#reportId").val();
        $.ajax({
            url: "/report/" + reportId +"/save_notes",
            data: {notes : notes},
            type: "POST",
            success: function(data){
                $("#warning_save_notes").addClass("hidden");
                $("#success_save_notes").removeClass("hidden");
                $("#save_notes_button").addClass("disabled");
                $("#original_notes").val(notes);
            },
            error: function (e) {
                alert("Failed to save notes. Please try again!");
            }
        });
    });


    //Submit the resolved report form
    $("#confirm_resolved").click(function () {
        $("#resolved_report_form").submit();
    });

    //radio box for report "Other" -> enable text
    $("[id^=report_], #other_report").change(function () {
        if (this.id.startsWith("report_")){
            $("#other_report_text").prop("disabled", true);
        } else{
            $("#other_report_text").prop("disabled", false);
        }
    });

    /*
     *  Submit report ajax with post
     *  Hide the form for report modal
     *  Popup the status of the submission
     */
    $("#send_report").click(function () {
        $("#modal_report_form").modal("hide");

        var reportDesc = $("#other_report_text").val();
        var boardId = $("#boardId").val();

        if ($("#other_report").prop("checked") != true) {
            reportDesc = $("input[name=reportDesc]:checked").val();
        }

        //submit ajax
        $.ajax({
            url: "/board/" + boardId + "/create-report",
            data: {reportDesc : reportDesc},
            type: "POST",
            success: function(data){
                //popup successful modal here
                $("#report_status").html("Submit Report Successful! \n" +
                    "Please alow us at least 24 hours to process your report. \n " +
                    "Enjoy!");
                $("#report_status_modal").modal("show");
            },
            error: function (e) {
                $("#report_status").html("Failed to submit report. Please Try Again!");
                $("#report_status_modal").modal("show");
            }
        });
    });

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

function playSound(soundId) {
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

function randomSounds() {
    var sounds = $('audio');
    var sound = sounds[Math.floor(Math.random() * sounds.length)];
    $(sound).hover();
    new Audio(sound.src).play();
    setTimeout(randomSounds, 650);
}


var myAudios = [];
window.URL = window.URL || window.webkitURL;
function setFileInfo(files) {
    var num_files = document.getElementById("audio_upload_input").files.length;
    myAudios = []; 
    updateInfos();
    for (i=0; i<num_files; i++){ 
        myAudios.push(files[i]); 
        getAudioInfo(files, i); 
    }
}

function getAudioInfo(files, i) { 
    var audio = document.createElement('audio'); 
    audio.preload = 'metadata'; 
    audio.onloadedmetadata = function() { 
        window.URL.revokeObjectURL(this.src) 
        var duration = audio.duration; 
        myAudios[i].duration = duration; 
        updateInfos(); 
    } 
    audio.src = URL.createObjectURL(files[i]);
 }

function updateInfos(){
    var soundInfo =  document.querySelector('#infos');
    if (myAudios.length > 0)
        soundInfo.className = "alert alert-success";
    else
        soundInfo.className = "alert alert-success hidden";

    soundInfo.innerHTML="";
    for(i=0;i<myAudios.length;i++){
        soundInfo.innerHTML += "<div>"+myAudios[i].name+" duration: "+myAudios[i].duration+'</div>';
    }
}