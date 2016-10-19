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
function randomSounds() {
    var sounds = $('audio');
    var sound = sounds[Math.floor(Math.random() * sounds.length)];
    $(sound).hover();
    new Audio(sound.src).play();
    setTimeout(randomSounds, 650)
}