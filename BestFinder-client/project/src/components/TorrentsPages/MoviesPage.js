import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { useEffect } from "react";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { MovieArticle } from "./MovieArticle";
import { SortingMenu } from "./SortingMenu";

export const MoviesPage = () => {
  const { onPageMount, torrentInfo, moviesByYear, selectorValue } = useTorrentContext();
  useEffect(() => {
    async function fetchData() {
      return await onPageMount("movie");
    }
    fetchData();
  },[]);


  return (
    <>
      <MyNavBar />
      <section className={style.container}>
        <h2 className={style.header}>Movies</h2>

        <SortingMenu category="movies" torrentInfoSortByYear={moviesByYear}/>

        {selectorValue === 'likes' && torrentInfo.map(movie => 
         <MovieArticle key={movie.id}
         torrent={movie.torrent}
        torrentUrl={movie.pictureUrl}
        torrentName={movie.movieName}
        year={movie.releasedYear}
        actors={movie.actors}
        categories={movie.categories}
        id={movie.id}
        />
        )
         }
         {selectorValue === 'year' && moviesByYear.map(movie => 
         <MovieArticle key={movie.id}
         torrent={movie.torrent}
        torrentUrl={movie.pictureUrl}
        torrentName={movie.movieName}
        year={movie.releasedYear}
        actors={movie.actors}
        categories={movie.categories}
        id={movie.id}
        />
        )
         }
      </section>
    </>
  );
};
