<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Edit Form</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
  <h1 class="text-center mb-4">Edit Post</h1>
  <div class="card shadow p-4">
    <form:form modelAttribute="imageVO" action="../editok" method="post" enctype="multipart/form-data">
      <form:hidden path="id"/>

      <div class="mb-3">
        <label for="title" class="form-label">Title <span class="text-danger">*</span></label>
        <form:input path="title" cssClass="form-control" id="title" placeholder="Enter post title" required="true"/>
      </div>

      <div class="mb-3">
        <label for="description" class="form-label">Description <span class="text-danger">*</span></label>
        <form:textarea path="description" cssClass="form-control" id="description" rows="5"
                       placeholder="Enter post description" required="true"></form:textarea>
      </div>

      <div class="mb-3">
        <label for="image" class="form-label">Image</label>
        <input type="file" class="form-control" id="image" name="image" accept="image/*">
      </div>

      <div class="d-flex justify-content-between">
        <button type="submit" class="btn btn-primary">Edit Post</button>
        <button type="button" class="btn btn-secondary" onclick="history.back()">Cancel</button>
      </div>
    </form:form>
  </div>
</div>

<!-- Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>