<head>
    <meta charset="utf-8"/>
    <title><#if (content.title)??><#escape x as x?xml>${content.title}</#escape><#else>
        ${config.site_name}</#if></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="<#--${content.description??}-->">
  <meta name="author" content="<#--${content.author}-->">
  <meta name="keywords" content="<#--${content.tags}-->">
  <meta name="generator" content="JBake ${version}">

  <link rel="stylesheet" href="${content.rootpath}/material.min.css">
  <script src="${content.rootpath}/material.min.js"></script>
  <link rel="stylesheet"
        href="https://fonts.googleapis.com/icon?family=Material+Icons">

    <link rel="shortcut icon"
          href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>favicon.ico">
</head>
