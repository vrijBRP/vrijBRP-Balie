<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="stylesheet" type="text/css" href="VAADIN/themes/gba-web/styles.css">
    <script type="text/javascript" src="VAADIN/site/js/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="VAADIN/site/js/highcharts.js"></script>
    <title>Google maps</title>
    <style type="text/css">

        body {
            margin-left: 0;
            margin-top: 0;
            margin-right: 0;
            margin-bottom: 0;
        }

        #marker hr {
            border-top: 1px solid #ddd;
            border-bottom: 0;
        }

    </style>
</head>

<body>

<div id="container" style="margin: 0 auto"></div>

<script type="text/javascript">

    $(function () {
        $(document).ready(function () {
            <%=request.getSession().getAttribute(request.getParameter("key")) %>
        });
    });
</script>
</body>
</html>
