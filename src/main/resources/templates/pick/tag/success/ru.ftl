Ты только что подписался на #<b>${name}</b>

<#if notes?has_content>
Последние ${total} новостей по тэгу #<b>${name}</b>

<#list notes as note>
<a href="${note.url}">${note.title}</a>
${note.source} / ${note.date}

</#list>
<#else>
<b>Нет новостей по тэгу #<b>${name}</b></b>
</#if>