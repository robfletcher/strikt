<!doctype html>
<html lang="en" class="has-spaced-navbar-fixed-top">
  <#include "head.ftl">
<body>
  <#include "navbar.ftl">
<main class="section">
  <div class="container">
    <div class="content is-medium">${content.body}</div>
    <#include "pagination.ftl">
  </div>
</main>
  <#include "footer.ftl">
</body>
</html>
