<!doctype html>
<html lang="en" class="has-spaced-navbar-fixed-top">
<head>
  <#include "head.ftl">
</head>
<body>
  <#include "navbar.ftl">
<main class="section">
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-10-desktop">
        <div class="content is-medium">${content.body}</div>
        <#include "pagination.ftl">
      </div>
    </div>
  </div>
</main>
  <#include "footer.ftl">
</body>
</html>
