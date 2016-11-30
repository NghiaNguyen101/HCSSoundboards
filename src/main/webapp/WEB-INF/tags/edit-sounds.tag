<%@ attribute name="sound" type="com.hcs.soundboard.data.SoundMetadata" required="true" %>

<%@ include file="/includes.jsp" %>

<p class="edit-view">
    <input type="hidden" name="soundId" value="${sound.id}">
    <hcs:play-button sound="${sound}"/>
    &nbsp
    <input type="hidden" name="originalName" value="${sound.name}">
    <input type="text" name="name" value="${sound.name}">
    &nbsp
    <hcs:trash-button sound="${sound}"/>

</p>