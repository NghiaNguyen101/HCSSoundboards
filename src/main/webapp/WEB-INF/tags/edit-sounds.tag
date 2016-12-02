<%@ attribute name="sound" type="com.hcs.soundboard.data.SoundMetadata" required="true" %>

<%@ include file="/includes.jsp" %>

<p class="edit-view">
    <input type="hidden" name="soundId" value="${sound.id}"/>
    <hcs:play-button sound="${sound}"/>
    &nbsp
    <input type="hidden" name="originalName" value="${sound.name}" maxlength="100"/>
    <input type="text" name="name" value="${sound.name}" maxlength="100"/>
    &nbsp
    <input type="hidden" name="originalBoxColor" value="${sound.boxColor}" />
    <input type="color" name="boxColor" value="${sound.boxColor}" id="boxColor_${sound.id}" />
    &nbsp
    <hcs:trash-button sound="${sound}"/>

</p>