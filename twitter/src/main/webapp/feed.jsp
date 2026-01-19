<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.twitter.db.data.post.Post" %>
<%@ page import="com.example.twitter.db.data.user.UserPost" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Feed</title>
    <link rel="icon" href="./Logo blue.svg" sizes="16x16 32x32" type="image.svg">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>

</head>

<body class="bg-primary bg-opacity-25">


<%
    String failed = request.getParameter("failed");
    String user = request.getParameter("user");
    String success = request.getParameter("success");
%>

<nav class="navbar navbar-light bg-light">
    <a class="navbar-brand">
      <img src="./Logo blue.svg" width="30" height="30" class="d-inline-block align-top" alt="">
      Twitter
    </a>

        <form class="form-inline" action="/feed" method="post">
        <div class="input-group">
          <div class="input-group-prepend">
            <span class="input-group-text" id="basic-addon1">@</span>
          </div>
            <input type="hidden" name="action" value="follow"/>
            <input type="hidden" name="user" value="<%= user %>"/>
            <input type="text" name="to_follow" class="form-control mr-sm-2" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1" required>
            <button class="btn btn-outline-primary my-2 my-sm-0" type="submit">Follow</button>
        </div>
      </form>
  </nav>

    <section class="d-flex flex-column min-vh-100 justify-content-center align-items-center">
        <div class="container">

            <%if(success != null && success.equals("post")){%>
            <p style="color: green">Post creato con successo!</p>
            <%} else if(failed != null && failed.equals("post")) {%>
            <p style="color: red">Il post deve avere meno di 140 caratteri</p>
            <%}%>

            <% if(success != null && success.equals("follow")){%>
            <p style="color: green">Utente aggiunto con successo ai seguiti!</p>
            <%} else if(failed != null && failed.equals("follow")) {%>
            <p style="color: red">Utente non esistente!</p>
            <%}%>

                <div class="col-md-10 mx-auto rounded shadow bg-white m-5">
                    <div class="row">
                    <div class="col m-4">
                    <div class="card border-dark mb-3" style="width: 18rem;margin:auto;">
                        <div class="card-header">Crea un post</div>
                            <form action="/feed" method="post">
                              <div class="form-outline">
                                <textarea class="form-control" name="message" id="validationTextarea" placeholder="Scrivi un messaggio..." required></textarea>
                              </div>
                        <div>
                            <input type="hidden" name="action" value="post"/>
                            <input type="hidden" name="user" value="<%= user %>"/>
                            <input type="submit" value="Pubblica" class="form-control btn btn-primary" style="width: 18rem">
                        </div>
                        </form>
                    </div>

                        <%
                            ArrayList<Post> posts = (ArrayList<Post>) request.getAttribute("posts");

                            for(Post post : posts)
                            {%>
                                    <div class="card border-dark mb-3" style="width: 18rem;margin:auto;">
                                        <%
                                            UserPost usr = post.getOwner();
                                        %>
                                        <div class="card-header"><%=usr.getFullName() + " @" + usr.getUsername()%></div>
                                        <div class="card-body">
                                            <p class="card-text"><%=post.getMessage()%></p>
                                        </div>
                                    </div>
                            <%}%>
                </div>
                </div>
            </div>
         </div>

    </section>


</body>

</html>
