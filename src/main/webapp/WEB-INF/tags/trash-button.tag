<%@ attribute name="sound" type="com.hcs.soundboard.data.SoundMetadata" %>

<%@ include file="/includes.jsp" %>
<span class="">
    <label id="deleteLabel_${sound.id}" class="btn btn-default">
        <i id="checkedState_${sound.id}" class="glyphicon glyphicon-unchecked"></i>
        <i class="fa fa-trash"></i>
            <input type="checkbox" class="hidden" name="deleted" value="${sound.id}"
                   onclick="switchDelete(this, ${sound.id})">

    </label>
</span>
