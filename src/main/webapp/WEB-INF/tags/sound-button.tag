<%@ attribute name="sound" type="com.hcs.soundboard.data.SoundMetadata" required="true" %>

<%@ include file="/includes.jsp" %>

<button id="${sound.id}" type="button" class="btn btn-default" onclick="new Audio('/sound/${sound.id}').play()">
    <c:out value="${sound.name}" />
</button>
<audio id="sound${sound.id}" preload="auto" src="/sound/${sound.id}"></audio>