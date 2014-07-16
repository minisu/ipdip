<#-- @ftlvariable name="" type="minisu.ipdip.views.DecisionView" -->
<html>
	<head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Bootstrap -->
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
		<script type="text/javascript">
            var source = new EventSource("/subscribe?channel=${decision.id?html}");
            source.addEventListener("newVisitor", function(event) {
                document.getElementById("seenBy").innerHTML += '<li>' + event.data + '</li>'
            });
            source.addEventListener("decisionMade", function(event) {
                document.getElementById("seenBy").innerHTML += '<li>Decision made: ' + event.data + '</li>'
            });
		</script>
	</head>
    <body>
        <div class="container">
            <h1>${decision.name?html}</h1>
            <h2>Alternatives</h2>
            <ul>
                  <#list decision.alternatives as alt>
                        <li>${alt}</li>
               </#list>
            </ul>
            <form action="${decision.id?html}/decide" method="POST">
                <button type="submit" class="btn btn-default btn-lg" <#if decidedAlternative??>disabled</#if>>
                    <span class="glyphicon glyphicon-check"></span> Decide
                </button>
            </form>
            <h2>Seen by</h2>
              <ul id="seenBy">
                 <#list decision.seenBy as user>
                        <li>${user}</li>
                  </#list>
              </ul>
              <script>
            <#if decidedAlternative??>
                    $( "li:contains('${decidedAlternative}')" ).css( "text-decoration", "underline" );
            </#if>
            </script>
            <hr>
            <p><a href="/oauth/request?decisionId=${decision.id?html}">
                    <img src="https://dev.twitter.com/sites/default/files/images_documentation/sign-in-with-twitter-gray.png">
            </a></p>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    </body>
</html>