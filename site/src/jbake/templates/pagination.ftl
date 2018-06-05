<#if content.previousPage?? || content.nextPage??>
  <nav class="pagination is-medium" role="navigation" aria-label="pagination">
    <a class="pagination-previous button is-large is-link is-outlined"
       <#if content.previousPage??>href="${content.previousPage}" <#else>disabled</#if>>
      <span class="icon">
        <i class="fas fa-arrow-left"></i>
      </span>
    </a>
    <a class="pagination-next button is-large is-link is-outlined" <#if content.nextPage??>href="${content.nextPage}"
       <#else>disabled</#if>>
      <span class="icon">
        <i class="fas fa-arrow-right"></i>
      </span>
    </a>
  </nav>
</#if>
