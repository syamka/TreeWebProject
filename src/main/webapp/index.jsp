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
        <style>
            #tree li.green > span {color: green; font-weight: bold;}
            #tree li > span {cursor: pointer}

            #info button {cursor: pointer;}
        </style>

       	<script src="./js/jquery.js" type="text/javascript"></script>
       	<script src="./js/jquery.cookie.js" type="text/javascript"></script>
       	<script src="./js/jquery.treeview.js" type="text/javascript"></script>
       	<script src="./js/jquery.treeview.edit.js" type="text/javascript"></script>
       	<script src="./js/jquery.treeview.async.js" type="text/javascript"></script>
        <script type="text/javascript">

            var searchAfterInit = "";

            /**
            *   Поскольку jquery treeview не дает callback-функции, а исходники править я не хочу,
             *  делаем такой финт: при каждой загрузке дочерних узлов, проверяем, не болтаются ли у нас
             *  номера узлов, которые надо открыть при поиске.
             */
            function initSearchListener(){
                $(document).ajaxComplete(function(event, xhr, settings){
                    if(settings.url.indexOf("rest/item/children") == 0){
                        doOpenBranches();
                        if(settings.url.indexOf("root=source") != -1){
                            alert(searchAfterInit);
                            if(searchAfterInit != "")
                                search(searchAfterInit);
                        }

                    };

                })
            }

            //Глобальные переменные - в массивах что-то есть только в процессе загрузки результатов поиска
            var openBranches = [];
            var searchResults = [];

            //Открыть узлы, которые мы можем открыть, из openBranches
            function doOpenBranches(){
                if(openBranches.length > 0){
                    var ids =  $.merge([], openBranches);
                    for(var i=0; i<ids.length; i++){
                        var li = $("#tree li[id="+ids[i]+"]");
                        if(li.length > 0 && li.is(":visible")){
                            if(li.hasClass("expandable")){
                                li.find(".hitarea").click();
                            }
                            openBranches.splice(openBranches.indexOf(ids[i]),1);
                        }
                    }
                }

                //Это все происходит в поиске, поэтому здесь же раскрашиваем результаты
                var ids = $.merge([], searchResults);
                for(var i=0;i<ids.length;i++){
                    var li = $("#tree li[id="+ids[i]+"]");
                    if(li.length > 0 && li.is(":visible")){
                        li.addClass("green");
                        searchResults.splice(searchResults.indexOf(ids[i]),1);
                    }

                }
            }

            function search(text){
                $("#tree li").removeClass("green");
                $.get("/rest/item/search",
                        {searchtext: text},
                        function(data){
                            openBranches = data.openBranches;
                            searchResults = data.results;
                            doOpenBranches();
                        },
                        "json"
                )
            }

            function initTree(){
                //Инициализация дерева
                $("#tree").treeview({
                    url: "rest/item/children"
                });
            }

            $(document).ready(function(){
                initTree();

                //Запускаем листенер, который поможет при поиске
                initSearchListener();

                //Поиск !
                $("#search").click(function(){
                    if($("#searchtext").val() == ""){
                        alert("Введите значение !");
                        return;
                    }

                    search($("#searchtext").val());
                })

                //В проект затесалась jQuery версии 1.4, поэтому использую live, а не on (лень перезаливать более новую версию)
                $("#tree li span").live("click", function(){
                    var id = $(this).parent().attr("id");
                    var text = $(this).text();
                    $("#selected_item").attr("value", id);
                    $("#selected_item_text").text(text);
                    $("#info").show();
                })

                $("#create_child_btn").click(function(){
                    $("#child_name").parent().toggle();
                });

                $("#save_child_btn").click(function(){
                    if($("#child_name").val() == ""){
                        alert("Введите текст дочернего узла !");
                        return;
                    }
                    $.post(
                        "/rest/item/add",
                        {parent_id: $("#selected_item").val(), text: $("#child_name").val()},
                        function(response){
                            if(response.status){
                                alert("Дочерний узел успешно создан");
                                $("#tree").empty();
                                searchAfterInit = $("#child_name").val();
                                initTree();
                                $("#child_name").attr("value", "");
                                $("#child_name").parent().hide();
                            }
                            else alert(response.message);
                        },
                        "json"
                    )
                })

            })

        </script>


</head>
<body>
<h3>Дерево текстовых элементов</h3>
<span style="font-size:14px;">Для управления узлом дерева, кликните на него мышкой</span>
<br /><br />
<table id="main">
    <tr>
        <td style="vertical-align: top">
            <div>
                Поиск по дереву: <input type="text" id="searchtext" /> <button id="search">Поиск</button>
            </div>
            <ul id="tree">
            </ul>
        </td>
        <td style="vertical-align: top; width: 300px">
            <div id="info" style="margin-left: 40px; display:none; ">
                <input type="hidden" id="selected_item" />
                <h4><div id="selected_item_text"></div></h4>
                <button id="create_child_btn">Создать дочерний</button>  <button id="delete_btn">Удалить</button>
                <div style="display: none;">
                    <br />
                    Текст дочернего узла: <input type="text" id="child_name" />
                    <button id="save_child_btn">Сохранить</button>

                </div>

            </div>
        </td>

    </tr>
</table>




</body>
</html>