<#if tags?has_content>
Ваши ${total} тэги

<#list tags as tag>#${tag} </#list>
<#else>
<b>Нет тэгов</b>
</#if>