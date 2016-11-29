<%@ attribute name="sound" type="com.hcs.soundboard.data.SoundMetadata" required="true" %>

<%@ include file="/includes.jsp" %>

<span>
    <label id="${sound.id}" class="btn btn-primary" onclick="playSound(${sound.id})">
        <i class="glyphicon glyphicon-play"></i>
    </label>
</span>
<audio id="sound${sound.id}" preload="auto" src="/sound/${sound.id}" onended="switchPlay(${sound.id})"></audio>
