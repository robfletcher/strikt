<!doctype html>
<html lang="en">
    <#include "header.ftl">
<body>

<#-- Always shows a header, even in smaller screens. -->
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <#include "menu.ftl">

  <main class="mdl-layout__content">
    <div class="mdl-grid">
      <div class="mdl-cell mdl-cell--12-col">
      ${content.body}
      </div>
    </div>
  </main>

    <#include "footer.ftl">
</div>

</body>
</html>
