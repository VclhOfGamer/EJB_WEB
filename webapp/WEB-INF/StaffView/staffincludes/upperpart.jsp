<%
    String staffsession = (String) session.getAttribute("staffName");
    if (staffsession != null) {
        
    } else {
        
        %> 
        <p id="sessionStats">NoSession</p>
        <script>
            var status = document.getElementById("sessionStats").innerHTML;
            console.log(status);
            if (status == "NoSession"){
                window.location.replace("StaffLogin");
            }
        </script>
        <%                
    }

%>


<!DOCTYPE html>
<html lang="en">

 <jsp:include page="head.jsp" />  

    <body>
        <!--Main Navigation-->
        <jsp:include page="mainnavigation.jsp" />  
        <!--Main Navigation-->

        <!--Main layout-->
        <main style="margin-top: 58px">
            <div class="container-fluid" style="margin-top:80px;">
