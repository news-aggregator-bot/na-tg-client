Ти щойно підписався на #<b>${name}</b>

<#if notes?has_content>
Останні ${total} новин за тегом #<b>${name}</b>

<#list notes as note>
<a href="${note.url}">${note.title}</a>
${note.source} / ${note.date?date} / ${note.region} / ${note.common}

</#list>
<#else>
<b>Нема новостей за тегом #<b>${name}</b></b>
</#if>