<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.twitter.db.data.post.Post" %>
<%@ page import="com.example.twitter.db.data.user.UserStatistic" %>
<%@ page import="com.example.twitter.db.data.user.UserPost" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin</title>
    <link rel="icon" href="./Logo blue.svg" sizes="16x16 32x32" type="image.svg">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>

</head>

<body class="bg-primary bg-opacity-25">

  <nav class="navbar navbar-light bg-light">
    <a class="navbar-brand" href="#">
      <img src="./Logo blue.svg" width="30" height="30" class="d-inline-block align-top" alt="">
      Twitter - Admin Section
    </a>
      </form>
  </nav>

    <section class="d-flex flex-column min-vh-100 justify-content-center align-items-center">
        <div class="container">
          <div class="col-md-10 mx-auto rounded shadow bg-white m-5">
            <div class="row">
                    <div class="col-sm m-4">
                      <div class="card border-dark mb-3" style="width: 18rem;margin:auto;">
                        <div class="card-header">Cerca post per parola</div>
                        <form method="get" action="/admin">
                          <div class="form-outline">
                            <input class="form-control" type="text" name="word" placeholder="Scrivi una parola..." required>
                          </div>

                        <div>
                            <input type="hidden" name="user" value="<%=request.getParameter("user")%>" />
                            <input type="hidden" name="action" value="word" />
                            <input type="submit" value="Cerca Parola" class="form-control btn btn-primary">
                        </div>
                        </form>
                    </div>
                    </div>
                    <div class="col-sm m-4">
                      <div class="card border-dark mb-3" style="width: 18rem;margin:auto;">
                        <div class="card-header">Visualizza post per hashtag</div>
                        <div>
                          <form method="get" action="/admin">
                            <input type="hidden" name="user" value="<%=request.getParameter("user")%>" />
                            <input type="hidden" name="action" value="hashtag" />
                            <input type="submit" value="Visualizza" class="form-control btn btn-primary">
                          </form>
                        </div>
                    </div>
                    </div>
                    <div class="col-sm m-4">
                      <div class="card border-dark mb-3" style="width: 18rem;margin:auto;">
                        <div class="card-header">Statistiche utenti</div>
                        <div>
                          <form>
                            <input type="hidden" name="user" value="<%=request.getParameter("user")%>" />
                            <input type="hidden" name="action" value="stats" />
                            <input type="submit" value="Visualizza" class="form-control btn btn-primary">
                          </form>
                        </div>
                    </div>
                    </div>
            </div>
        </div>
      </div>

      <div class="container">

              <%

                    String action = request.getParameter("action");

                    if(action != null) {

                      if(action.equals("hashtag")) {

                        //Ottenimento dei dati da visualizzare
                        HashMap<String,ArrayList<Post>> result = (HashMap<String,ArrayList<Post>>) request.getAttribute("result");

                        if(result.isEmpty()) {%>
                         <p style="text-align:center"> Nessun post trovato </p>
                      <%} else {%>
                            <div class="accordion" id="accordionExample">
                        <%}

                        //Visualizzazione dei dati
                        for(String hashtag : result.keySet()) {%>

                              <div class="accordion-item">
                                <h2 class="accordion-header" id="heading<%=hashtag%>">
                                  <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=hashtag%>" aria-expanded="true" aria-controls="collapse<%=hashtag%>">
                                    <%= '#' + hashtag %>
                                  </button>
                                </h2>
                                <div id="collapse<%=hashtag%>" class="accordion-collapse collapse show" aria-labelledby="heading<%=hashtag%>" data-bs-parent="#accordionExample">
                                  <div class="accordion-body">
                                    <div class="col-md-10 mx-auto rounded shadow bg-white m-5">
                                      <div class="row">
                                        <div class="col m-4">
                                        <%
                                          //Ottenimento dei post con lo specifico hashtag
                                          ArrayList<Post> posts = result.get(hashtag);

                                          for(Post post : posts) {%>

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
                                </div>
                              </div>
                        <%}%>
                        </div>

                      <%} else if(action.equals("word")) {%>

                              <div class="col-md-10 mx-auto rounded shadow bg-white m-5">
                                  <div class="row">
                                      <div class="col m-4">

                                          <%
                                              //Ottenimento dei dati da visualizzare
                                              ArrayList<Post> posts = (ArrayList<Post>) request.getAttribute("result");

                                              if(posts.isEmpty()) {%>
                                          <p style="text-align:center"> Nessun post trovato </p>
                                          <%}

                                              //Visualizzazione dei dati
                                              for(Post post : posts) {%>

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
                  <%
                      } else if(action.equals("stats")) {%>

                              <div class="col-md-10 mx-auto rounded shadow bg-white m-5">
                                  <div class="row">
                                      <div class="col m-4">

                                          <%

                                              //Ottenimento dei dati da visualizzare
                                              ArrayList<UserStatistic> stats = (ArrayList<UserStatistic>) request.getAttribute("result");

                                              //Visualizzazione dei dati
                                              for(UserStatistic user : stats) {%>

                                                  <div class="card border-dark mb-3" style="width: 18rem;margin:auto;">
                                                      <div class="card-header">@<%=user.getUsername()%></div>
                                                      <div class="card-body">
                                                          <p class="card-text">
                                                              Post inviati: <%=user.getMessagesSent()%>
                                                          </p>
                                                          <p class="card-text">
                                                              Post ricevuti: <%=user.getMessagesReceived()%>
                                                          </p>
                                                      </div>
                                                  </div>
                                              <%}%>

                                      </div>
                      <%}
                    }%>
      </div>

    </section>
</body>

</html>
