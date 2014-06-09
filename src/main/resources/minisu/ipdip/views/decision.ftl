<#-- @ftlvariable name="" type="minisu.ipdip.views.DecisionView" -->
<html>
    <body>
        <h1>${decision.name?html}</h1>
        <h2>Alternatives</h2>
        <ul>
        	  <#list decision.alternatives as alt>
        	  		<li>${alt}</li>
           </#list>
        </ul>
        <h2>Seen by</h2>
		  <ul>
		     <#list decision.seenBy as user>
					<li>${user}</li>
			  </#list>
		  </ul>
    </body>
</html>