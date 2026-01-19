<!DOCTYPE html>
<html lang="en">

<head>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign-Up</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>

</head>

<body class="bg-primary bg-opacity-25">

    <section class="d-flex flex-column min-vh-100 justify-content-center align-items-center">
        <div class="container">
            <div class="row">
                <div class="col-md-10 mx-auto rounded shadow bg-white">
                    <div class="row">
                        <div class="col-md-6">
                             <div class="m-5 text-center">
                                <h1>Benvenuto su Twitter!</h1>
                             </div>
                             <form class="needs-validation m-5" method="post" action="/register">

                                 <div class="form-group">
                                     <div class="mb-3">
                                         <label class="form-label" for="fullname">Nome</label>
                                         <input class="form-control" name="fullname" type="text" id="fullname" required>
                                         <div class="invalid-feedback">
                                             Inserire un nome
                                         </div>
                                     </div>
                                 </div>

                                 <%if(request.getParameter("username") == null){%>
                                    <%=  "<div class=\"form-group\">" %>
                                 <%}else{%>
                                    <%=  "<div class=\"form-group was-validated\">" %>
                                 <%}%>
                                    <div class="mb-3">
                                    <label class="form-label" for="username">Username</label>
                                    <input class="form-control" name="username" type="text" id="username" required>
                                    <div class="invalid-feedback">
                                        Nome utente gi&agrave; esistente
                                    </div>
                                    </div>
                                    </div>
                                <div class="form-group">
                                <div class="mb-3">
                                <label class="form-label" for="password">Password</label>
                                <input class="form-control" name="password" type="password" id="password" required>
                                <div class="invalid-feedback">
                                    Inserire una password
                                </div>
                                </div>
                                </div>
                                <div class="row mb-3">
                                    <div>
                                        <input type="submit" value="Iscriviti" class="form-control btn btn-primary mt-3">
                                    </div>
                                </div>
                             </form>
                             </div>
                             <div class="col-md-6">
                                <div>
                                    <img src="./Twitter-logo.svg" alt="login" class="img-fluid p-5">
                             </div>
                        </div>
                </div>
            </div>
        </div>
    </div>

    </section>


</body>

</html>