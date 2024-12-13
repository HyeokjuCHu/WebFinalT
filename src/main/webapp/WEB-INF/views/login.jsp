<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="vh-100 d-flex align-items-center justify-content-center bg-light">
<div class="col-md-3">
  <div class="card shadow-sm">
    <div class="card-body">
      <h5 class="card-title text-center mb-4">Login</h5>

      <!-- 회원가입 완료 메시지 출력 -->
      <c:if test="${not empty msg}">
        <div class="alert alert-success" role="alert">
            ${msg}
        </div>
      </c:if>

      <form method="post" action="./loginOk">
        <div class="mb-3">
          <label for="userid" class="form-label">User ID</label>
          <input type="text" id="userid" name="userid" class="form-control" placeholder="Enter your user ID" required>
        </div>
        <div class="mb-5">
          <label for="password" class="form-label">Password</label>
          <input type="password" id="password" name="password" class="form-control" placeholder="Enter your password" required>
        </div>
        <button type="submit" class="btn btn-primary w-100 mb-3">Login</button>
        <div class="text-center">
          <a href="./signup" class="text-muted small">Don't have an account? Sign up</a>
        </div>
      </form>
    </div>
  </div>
</div>
<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>