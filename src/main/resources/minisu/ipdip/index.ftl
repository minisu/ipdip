<html>
	<head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Bootstrap -->
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
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
        <form class="navbar-form navbar-left" method="POST">
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
            <button type="submit" class="btn btn-default btn-lg">
                <span class="glyphicon glyphicon-ok-sign"></span> Create
            </button>
        </form>
    <button id="addScnt" class="btn btn-default btn-lg">
        <span class="glyphicon glyphicon-plus"></span> Add alternative
    </button>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    </body>
</html>