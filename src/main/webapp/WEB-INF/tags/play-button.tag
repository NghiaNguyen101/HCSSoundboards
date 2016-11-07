<%@ attribute name="sound" type="com.hcs.soundboard.data.SoundMetadata" required="true" %>

<%@ include file="/includes.jsp" %>

<%--<button id="${sound.id}" type="button" class="btn btn-default play" onclick="playSound(${sound.id})">
    <c:out value="Play" />
</button>--%>
<span class="" data-toggle="buttons">
    <label id="${sound.id}" class="btn btn-primary" onclick="playSound(${sound.id})">
        <input type="radio" name="options" id="option5" checked>
        <i class="glyphicon glyphicon-play"></i>
        <label> Play </label>
    </label>
    <label id="${sound.id}" class="btn btn-primary active" onclick="stopSound()" >
        <input type="radio" name="options" id="option6">
        <i class="glyphicon glyphicon-stop"></i>
        <label> Stop </label>

    </label>
</span>
<audio id="sound${sound.id}" preload="auto" src="/sound/${sound.id}"></audio>

<%--<input type="checkbox" checked data-toggle="toggle" data-on="<i class='fa fa-play'></i> Play" data-off="<i class='fa fa-pause'></i> Pause">--%>