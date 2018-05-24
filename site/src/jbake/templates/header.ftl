<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="viewport"
      content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
<title><#if (content.title)??><#escape x as x?xml>${content.title}</#escape><#else> ${config.site_name}</#if></title>

<!-- CSS  -->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
<link href="${content.rootpath}css/materialize.css" type="text/css"
      rel="stylesheet" media="screen,projection"/>
<link href="${content.rootpath}css/style.css" type="text/css" rel="stylesheet"
      media="screen,projection"/>

<meta name="description" content="<#--${content.description??}-->">
<meta name="author" content="<#--${content.author}-->">
<meta name="keywords" content="<#--${content.tags}-->">
<meta name="generator" content="JBake ${version}">
