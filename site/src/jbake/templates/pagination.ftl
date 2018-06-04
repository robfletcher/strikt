<#if content.previousPage?? || content.nextPage??>
  <nav class="section">
    <div class="container">
      <nav class="pagination is-medium" role="navigation" aria-label="pagination">
        <a class="pagination-previous" <#if content.previousPage??>href="${content.previousPage}"
           <#else>disabled</#if>>Previous</a>
        <a class="pagination-next" <#if content.nextPage??>href="${content.nextPage}"<#else>disabled</#if>>Next
          page</a>
      </nav>
    </div>
  </nav>
</#if>
