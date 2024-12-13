<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>View Post</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
  <div class="card shadow p-4">
    <h1 class="card-title text-center">${post.title}</h1>
    <p class="text-muted text-center">By: ${post.userid}</p>

    <div class="mt-4">
      <p class="lead">${post.description}</p>
    </div>

    <ul class="list-group list-group-flush">
      <li class="list-group-item"><strong>Created:</strong> <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></li>
      <li class="list-group-item"><strong>Views:</strong> ${post.views}</li>
      <li class="list-group-item"><strong>Likes:</strong> ${post.likes}</li>
    </ul>

    <div class="text-center mt-4">
      <c:if test="${not empty post.filename}">
        <img src="${pageContext.request.contextPath}/upload/${post.filename}" alt="Post Image" class="img-fluid rounded" style="max-width: 100%; height: auto;">
        <form action="${pageContext.request.contextPath}/download/${post.id}" method="get" class="mt-3">
          <button type="submit" class="btn btn-secondary">Download Image</button>
        </form>
      </c:if>
    </div>

    <div class="text-center mt-4">
      <form action="like/${post.id}" method="post">
        <button type="submit" class="btn btn-primary">Like</button>
      </form>
    </div>

    <div class="text-center mt-4">
      <a href="../list" class="btn btn-outline-secondary">Back to List</a>
    </div>
  </div>
</div>

<!-- Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>