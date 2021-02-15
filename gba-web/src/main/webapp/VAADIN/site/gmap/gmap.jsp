<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="stylesheet" type="text/css" href="VAADIN/themes/gba-web/styles.css">
    <script type="text/javascript"
            src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<%= request.getParameter("key") %>"></script>
    <script type="text/javascript" src="VAADIN/site/js/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="VAADIN/site/js/jquery.gmap-1.1.0-min.js"></script>
    <title>Google maps</title>
    <style type="text/css">

        html, body {
            height: 100%;
            width: 100%;
        }

        body {
            margin-left: 0px;
            margin-top: 0px;
            margin-right: 0px;
            margin-bottom: 0px;
        }

        #marker {
            font-family: arial;
            font-size: 12px;
        }

        #marker hr {
            border-top: 1px solid #ddd;
            border-bottom: 0px;
        }

    </style>
</head>

<body>
<script type="text/javascript">

    var contentString = '<div id="marker"><%= request.getParameter("content") %></div>';
    var address = '<%= request.getParameter("address") %>';

    $(function () {
        $("#map").gMap({
            markers: [{
                address: address,
                html: contentString
            }],
            address: address, zoom: 15
        });
    });
</script>

<div id="map" style="width: 100%; height: 100%"></div>
</body>
</html>
