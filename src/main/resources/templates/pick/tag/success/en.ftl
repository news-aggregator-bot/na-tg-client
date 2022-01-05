You've just subscribed for tag #<b>${name}</b>

<#if notes?has_content>
Latest ${total} news by #<b>${name}</b>

<#list notes as note>
<a href="${note.url}">${note.title}</a>
${note.source} / ${note.date?date}

</#list>
<#else>
<b>No news by #<b>${name}</b></b>
</#if>