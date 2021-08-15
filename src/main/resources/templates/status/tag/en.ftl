<#if tags?has_content>
Your ${total} tags

<#list tags as tag>#${tag} </#list>
<#else>
No tags
</#if>
