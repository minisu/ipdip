<html>
	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>  
		<script type="text/javascript">
          $(function() {
                  var scntDiv = $('#alternatives');
                  
                  $('#addScnt').live('click', function() {
                          $('<p><input type="text" id="p_scnt" size="20" name="alternative"> <a href="#" id="remScnt">Remove</a></p>').appendTo(scntDiv);
                          return false;
                  });

                  $('#remScnt').live('click', function() {
						$(this).parents('p').remove();
                        return false;
                  });
          });
      </script>
	</head>
    <body>
        <h1>Create decision</h1>
        <form method="POST">
			<h2>Name</h2>	
			<input name="name" autofocus required>

			<h2>Alternatives</h2>	
			<div id="alternatives">
				<p>
					<input type="text" id="p_scnt" size="20" name="alternative" required>
				</p>
				<p>
					<input type="text" id="p_scnt" size="20" name="alternative" required>
				</p>
			</div>
			<h2><a href="#" id="addScnt">+</a></h2>
			<input type="submit" value="Create">
        </form>
    </body>
</html>