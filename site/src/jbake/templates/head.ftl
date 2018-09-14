<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="viewport"
      content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
<title><#if (content.title)??><#escape x as x?xml>${content.title}</#escape><#else> ${config.site_name}</#if></title>

<!-- CSS  -->
<link href="/css/bulma.min.css" type="text/css"
      rel="stylesheet" media="screen,projection">
<link href="/css/prism.css" type="text/css"
      rel="stylesheet" media="screen,projection">

<meta name="description" content="<#--${content.description??}-->">
<meta name="author" content="<#--${content.author}-->">
<meta name="keywords" content="<#--${content.tags}-->">
<meta name="generator" content="JBake ${version}">

<script defer src="https://use.fontawesome.com/releases/v5.0.13/js/all.js"
        integrity="sha384-xymdQtn1n3lH2wcu0qhcdaOpQwyoarkgLVxC/wZ5q7h9gHtxICrpcaSUfygqZGOe"
        crossorigin="anonymous"></script>
<script defer src="/js/prism.js"></script>
<style>
  /* Fix some conflicts with Prism and Bulma */
  pre[class*="language-"] {
    margin: inherit;
  }

  code[class*="language-"], pre[class*="language-"],
  code, pre {
    font-family: 'Fira Code', 'Source Code Pro', Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace;
  }

  :not(pre) > code[class*="language-"], pre[class*="language-"],
  pre {
    background: #363636;
    color: #ccc;
  }

  pre[class*="language-"] .number {
    align-items: inherit;
    background-color: inherit;
    border-radius: inherit;
    display: inherit;
    font-size: inherit;
    height: inherit;
    justify-content: inherit;
    margin-right: inherit;
    min-width: inherit;
    padding: inherit;
    text-align: inherit;
    vertical-align: inherit;
  }

  pre.notification {
    padding: 1em;
  }
</style>
