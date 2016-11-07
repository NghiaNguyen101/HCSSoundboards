<%--
  Created by IntelliJ IDEA.
  User: chatt
  Date: 11/04/16
  Time: 11:34
  To change this template use File | Settings | File Templates.
--%>
<%@ attribute name="sound" type ="com.hcs.soundboard.data.SoundMetadata" %>

<%@ include file="/includes.jsp" %>

<span class="" data-toggle="buttons">
    <label class="btn btn-success" >
        <input type="radio" name="options" id="option5" checked>
        <i class="fa fa-trash"></i>
    </label>
    <label class="btn btn-danger active" >
        <input type="radio" name="options" id="option6">
        <i class="fa fa-trash"></i>
    </label>
</span>

