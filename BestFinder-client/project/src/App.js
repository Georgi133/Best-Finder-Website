import { Route, Routes } from "react-router-dom";
import { Home } from "./components/Home/Home";
import { Login } from "./components/Login/Login";
import { Register } from "./components/Register/Register";
import { Footer } from "./components/Footer/Footer";
import { AuthProvider } from "./components/AuthContext/AuthContext";
import { ChangePassword } from "./components/ChangePassword/ChangePassword";
import { EditProfile } from "./components/EditProfile/EditProfile";
import { useEffect, useState } from "react";
import jwt_decode from "jwt-decode";
import { ChangeUserRole } from "./components/ChangeUserRole/ChangeUserRole";
import { AddMenu } from "./components/AddMenu/AddMenu";
import { AdminProvider } from "./components/AdminContext/AdminContext";
import { TorrentProvider } from "./components/TorrentContext.js/TorrentContext";
import { MoviesPage } from "./components/TorrentsPages/MoviesPage";
import { SongsPage } from "./components/TorrentsPages/SongsPage";
import { AnimesPage } from "./components/TorrentsPages/AnimesPage";
import { JokesPage } from "./components/TorrentsPages/JokesPage";
import { GamesPage } from "./components/TorrentsPages/GamesPage";
import { SerialPage } from "./components/TorrentsPages/SerialsPage";
import { MovieDetails } from "./components/TorrentDetails/MovieDetails";
import { SerialDetails } from "./components/TorrentDetails/SerialDetails";
import { GameDetails } from "./components/TorrentDetails/GameDetails";
import { JokeDetails } from "./components/TorrentDetails/JokeDetails";
import { AnimeDetails } from "./components/TorrentDetails/AnimeDetails";
import { SongDetails } from "./components/TorrentDetails/SongDetails";
import { ValidatorProvider } from "./components/ValidatorContext/ValidatorContext";
import { PrivateRoute } from "./components/PrivateRoute/PrivateRoute";
import { PrivateAdminRoute } from "./components/PrivateRoute/PrivateAdminRoute";
import { NotAllowed } from "./components/NotAllowed/NotAllowed";
import { Forgotten } from "./components/Login/Forgotten";
import { ServerError } from "./components/ServerError/ServerError";
import { BanUser } from "./components/BanUser/BanUser";

function App() {
  const [user, setUser] = useState({});
  const token = JSON.parse(localStorage.getItem("token"));

  useEffect(() => {
    if (token) {
      const decoded = jwt_decode(token);
      const { sub, role, exp } = decoded;
      if (new Date(exp * 1000) < new Date()) {
        setUser({});
        localStorage.removeItem("token");
      } else {
        setUser({ role: role, email: sub });
      }
    }
  }, [token]);

  return (
    <AuthProvider userReload={user}>
      <AdminProvider>
        <TorrentProvider>
          <ValidatorProvider>
            <>
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/users/login" element={<Login />} />
                <Route path="/users/register" element={<Register />} />
                <Route path="/users/forgotten" element={<Forgotten />} />

                <Route
                  path="/users/change-password"
                  element={
                    <PrivateRoute>
                      <ChangePassword />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/users/edit-profile"
                  element={
                    <PrivateRoute>
                      <EditProfile />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/admin/add"
                  element={
                    <PrivateAdminRoute>
                      <AddMenu />
                    </PrivateAdminRoute>
                  }
                />
                <Route
                  path="/admin/change-role"
                  element={
                    <PrivateAdminRoute>
                      <ChangeUserRole />
                    </PrivateAdminRoute>
                  }
                />
                 <Route
                  path="/admin/ban-menu"
                  element={
                    <PrivateAdminRoute>
                      <BanUser />
                      </PrivateAdminRoute>
                  }
                />
                <Route
                  path="/movies"
                  element={
                    <PrivateRoute>
                      <MoviesPage />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/songs"
                  element={
                    <PrivateRoute>
                      <SongsPage />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/animes"
                  element={
                    <PrivateRoute>
                      <AnimesPage />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/jokes"
                  element={
                    <PrivateRoute>
                      <JokesPage />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/games"
                  element={
                    <PrivateRoute>
                      <GamesPage />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/serials"
                  element={
                    <PrivateRoute>
                      <SerialPage />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/movies/movie/:movieId"
                  element={
                    <PrivateRoute>
                      <MovieDetails />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/serials/serial/:serialId"
                  element={
                    <PrivateRoute>
                      <SerialDetails />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/games/game/:gameId"
                  element={
                    <PrivateRoute>
                      <GameDetails />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/animes/anime/:animeId"
                  element={
                    <PrivateRoute>
                      <AnimeDetails />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/jokes/joke/:jokeId"
                  element={
                    <PrivateRoute>
                      <JokeDetails />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/songs/song/:songId"
                  element={
                    <PrivateRoute>
                      <SongDetails />
                    </PrivateRoute>
                  }
                />
                <Route
                  path="/not-allowed"
                  element={
                      <NotAllowed />
                  }
                />

                  <Route
                  path="/server-error"
                  element={
                      <ServerError />
                  }
                />

              </Routes>
              <Footer />
            </>
          </ValidatorProvider>
        </TorrentProvider>
      </AdminProvider>
    </AuthProvider>
  );
}

export default App;
