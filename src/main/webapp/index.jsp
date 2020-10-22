<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Evaluation</title>
    <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</head>
<body>
<nav class="red lighten-1">
    <div class="nav-wrapper">
        <a href="#" class="brand-logo center">Evaluation</a>
    </div>
</nav>

<div class="section">
    <div class="row center">
        <div class="col s6 m5 offset-m1">
            <div class="card-panel">
                <i class="material-icons large">keyboard_tab</i>
                <h3><c:out value="${count}"/> entries </h3>
                <button id="clickHere" class="btn green">Click Here</button>
            </div>
        </div>
    </div>
</div>


<script>
  function vote(team) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
      if (this.readyState == 4) {
        if (!window.alert(this.responseText)) {
          window.location.reload();
        }
      }
    };
    xhr.open("POST", "/", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send("team=" + team);
  }

  document.getElementById("clickHere").addEventListener("click", function () {
    vote();
  });
</script>
</body>
</html>

