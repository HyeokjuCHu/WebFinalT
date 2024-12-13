<%@ page language="java" contentType="text/html; charset=UTF-8"
         isELIgnored="false"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Image Sharing Board</title>
  <!-- Add Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script>
    function delete_ok(id) {
      var a = confirm("Are you sure you want to delete this post?");
      if (a) location.href = './delete/' + id;
    }
  </script>
</head>
<body>
<div class="container my-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="text-center mb-0">Image Sharing Board</h1>
    <a href="../login/logout" class="btn btn-dark">Logout</a>
  </div>
  <div class="d-flex justify-content-between mb-3">
    <a href="./add" class="btn btn-primary">Add New Post</a>
    <!-- Search Bar -->
    <form class="d-flex" action="./list" method="get">
      <input class="form-control me-2" type="text" name="keyword" placeholder="Search..." value="${param.keyword}">
      <button class="btn btn-primary" type="submit">Search</button>
      <button class="btn btn-secondary ms-2" type="button" onclick="location.href='./list';">Reset</button>
    </form>
  </div>

  <!-- Cards Layout for Posts -->
  <div class="row row-cols-1 row-cols-md-3 g-4">
    <c:forEach items="${list}" var="u">
      <div class="col">
        <div class="card h-100"><a href="view/${u.id}" class="text-decoration-none">
          <c:if test="${not empty u.filename}">
            <img src="${pageContext.request.contextPath}/upload/${u.filename}" class="card-img-top" alt="Image">
          </c:if>
          <c:if test="${empty u.filename}">
            <img src="https://via.placeholder.com/300x200" class="card-img-top" alt="No Image">
          </c:if>
          <div class="card-body">
            <h5 class="card-title">${u.title}</h5>
            <p class="card-text">${u.description}</p>
            <p class="text-muted mb-0">By: ${u.userid}</p>
            <small class="text-muted">Posted: <fmt:formatDate value="${u.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></small><br/>
            <small class="text-muted">Updated: <fmt:formatDate value="${u.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/></small>
          </div>
          <div class="card-footer d-flex justify-content-between align-items-center">
            <div>
              <small class="text-muted">Views: ${u.views}</small>
              <small class="text-muted ms-2">Likes: ${u.likes}</small>
            </div>
            <div>
              <a href="edit/${u.id}" class="btn btn-secondary btn-sm me-2">Edit</a>
              <button class="btn btn-dark btn-sm" onclick="delete_ok('${u.id}')">Delete</button>
            </div>
          </div>
        </a></div>
      </div>
    </c:forEach>
  </div>
</div>

<!-- Add Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>