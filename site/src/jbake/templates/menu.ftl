<header class="mdl-layout__header">
  <div class="mdl-layout__header-row">
  <#-- Title -->
    <span class="mdl-layout-title">${config.site_name}</span>
  <#-- Add spacer, to align navigation to the right -->
    <div class="mdl-layout-spacer"></div>
  <#-- Navigation. We hide it in small screens. -->
    <nav class="mdl-navigation mdl-layout--large-screen-only">
      <a class="mdl-navigation__link" href="${content.rootpath}">Home</a>
      <a class="mdl-navigation__link" href="">Link</a>
      <a class="mdl-navigation__link" href="">Link</a>
      <a class="mdl-navigation__link" href="">Link</a>
    </nav>
  </div>
</header>
<div class="mdl-layout__drawer">
  <span class="mdl-layout-title">${config.site_name}</span>
  <nav class="mdl-navigation">
    <a class="mdl-navigation__link" href="">Link</a>
    <a class="mdl-navigation__link" href="">Link</a>
    <a class="mdl-navigation__link" href="">Link</a>
    <a class="mdl-navigation__link" href="">Link</a>
  </nav>
</div>
