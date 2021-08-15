<#if tags?has_content>
Твої ${total} теги

<#list tags as tag>#${tag} </#list>
<#else>
<b>Нема тегів</b>
</#if>