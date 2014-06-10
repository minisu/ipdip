<#-- @ftlvariable name="" type="minisu.ipdip.views.DecisionView" -->
<html>
	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>  
		<script type="text/javascript">
			var scntDiv = $('#seenBy');
			
			function connect() {
				websocket = new WebSocket("ws://localhost:9010/websocket");
				websocket.onopen = function(evt) { onOpen(evt) };
				websocket.onmessage = function(evt) { 
					$('<li>' + evt.data + '</li>').appendTo(seenBy);
				};
			}
			
			function onOpen(evt) { websocket.send( "${decision.id?html}" ); }
			
			window.addEventListener("load", connect, false);
		</script>
	</head>
    <body>
        <h1>${decision.name?html}</h1>
        <h2>Alternatives</h2>
        <ul>
        	  <#list decision.alternatives as alt>
        	  		<li>${alt}</li>
           </#list>
        </ul>
        <h2>Seen by</h2>
		  <ul id="seenBy">
		     <#list decision.seenBy as user>
					<li>${user}</li>
			  </#list>
		  </ul>
    </body>
</html>