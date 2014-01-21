<%--
  Created by IntelliJ IDEA.
  User: predtechenskaya
  Date: 21.01.14
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
        <link rel="stylesheet" href="./css/jquery.treeview.css" />

       	<script src="./js/jquery.js" type="text/javascript"></script>
       	<script src="./js/jquery.cookie.js" type="text/javascript"></script>
       	<script src="./js/jquery.treeview.js" type="text/javascript"></script>
       	<script src="./js/jquery.treeview.edit.js" type="text/javascript"></script>
       	<script src="./js/jquery.treeview.async.js" type="text/javascript"></script>
        <script type="text/javascript">

            //Проблема в том, что у нас AJAX !!!1111
            function expandNode(id){
                var li = $("#tree li[id="+id+"]");
                li.find(".hitarea").click();
            }

            $(document).ready(function(){
                $("#tree").treeview({
                    url: "rest/item/children"
                });

                $("#search").click(function(){
                    $.get("/rest/item/search",
                        {"searchtext": $("#searchtext").val()},
                        function(data){
                            for(i=0;i<data.openBranches.length;i++){
                                id=data.openBranches[i];
                                expandNode(id)
                            }

                        },
                        "json"

                    )

                })


            })

        </script>


</head>
<body>

<div>
    Поиск по дереву: <input type="text" id="searchtext" /> <button id="search">search</button>

</div>
<ul id="tree">
</ul>


</body>
</html>